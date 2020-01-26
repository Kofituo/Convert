package com.example.unitconverter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.AdditionItems.pkgName
import com.example.unitconverter.recyclerViewData.Mass
import com.example.unitconverter.recyclerViewData.Prefix
import com.example.unitconverter.recyclerViewData.Temperature
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.MyAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator
import kotlin.math.round

class ConvertFragment : DialogFragment(), MyAdapter.OnRadioButtonsClickListener {

    private lateinit var cancelButton: MaterialButton
    private lateinit var searchBar: TextInputLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewModel: ConvertViewModel
    //Prefixes and their units
    private lateinit var sharedPreferences: SharedPreferences
    //
    private var isPrefix = false
    private var viewId: Int = -1
    private lateinit var viewName: String
    private var lastPosition = 1
    private var whichButton = -1
    private lateinit var string: String
    private lateinit var convertDialogInterface: ConvertDialogInterface
    private lateinit var positionKey: String

    //comparator for re usability
    //eg
//    private val comparator =
//        Comparator { first: RecyclerDataClass, second: RecyclerDataClass ->
//            first.unit.compareTo(second.unit)
//        }

    private val comparator =
        Comparator { first: RecyclerDataClass, second: RecyclerDataClass ->
            first.quantity.compareTo(second.quantity)
        }
    var start = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        start = System.currentTimeMillis()
        super.onCreate(savedInstanceState)
        arguments?.apply {
            viewId = getInt("viewId")
            viewName = getString("viewName", "")
            whichButton = getInt("whichButton")
        }
        string = if (whichButton == R.id.top_button) "topButton" else "bottomButton"
        positionKey = string.substringBefore("B") + "Position"
        isPrefix = viewId == R.id.prefixes
        activity?.run {
            viewModel = ViewModelProviders.of(this)[ConvertViewModel::class.java]
            sharedPreferences = getSharedPreferences(pkgName + viewName, Context.MODE_PRIVATE)
        }
        viewModel.dataSet = whichView(viewId)
        lastPosition = sharedPreferences.getInt(string, -1)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val dialog = Dialog(context!!, R.style.sortDialogStyle)
        //view
        LayoutInflater.from(context).inflate(R.layout.items_list, null).apply {

            dialog.setContentView(this)
            cancelButton = findViewById(R.id.cancel_button)
            searchBar = findViewById(R.id.search_bar)
            val params = layoutParams
            params.width =
                if (isPortrait) (screenWidth / 8) * 7 else round(screenWidth / 1.6).toInt()

            params.height =
                if (isPortrait) round(screenHeight * 0.92).toInt() else ViewGroup.LayoutParams.WRAP_CONTENT
            layoutParams = params

            recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
                setHasFixedSize(true)
                viewManager = LinearLayoutManager(context)
                layoutManager = viewManager
                this@ConvertFragment.viewAdapter = // add comparator to params
                    MyAdapter(viewModel.dataSet, viewModel.randomInt, comparator)
                        .apply { setOnRadioButtonsClickListener(this@ConvertFragment) }

                adapter = (viewAdapter as MyAdapter).apply {
                    add(viewModel.dataSet)

                    lastPosition = this@ConvertFragment.lastPosition
                    if (lastPosition != -1) smoothScrollToPosition(lastPosition)

                    searchBar.findViewById<TextInputEditText>(R.id.searchEditText)?.apply {

                        addTextChangedListener(object : TextWatcher {
                            var called = false
                            override fun afterTextChanged(s: Editable?) {
                                val filteredList =
                                    filter(viewModel.dataSet, s.toString())
                                Log.e("fil", "$filteredList")
                                replaceAll(filteredList)
                                if (s.isNullOrEmpty()) {
                                    boolean = false
                                    called = false
                                    notifyItemRangeChanged(0, itemCount)
                                }
                                smoothScrollToPosition(0)
                            }

                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                                boolean = true
                                if (!called) notifyItemRangeChanged(0, itemCount)
                                called = true
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) = Unit
                        })
                    }
                }
            }
        }

        setDialogColors(viewModel.randomInt)
        Log.e("time", "${System.currentTimeMillis() - start}")
        return dialog
    }

    private fun setDialogColors(colorInt: Int) {
        searchBar.boxStrokeColor = colorInt
        cancelButton.apply {
            ColorStateList.valueOf(colorInt).also {
                rippleColor = it
                setTextColor(it)
            }
            setOnClickListener {
                dismiss()
            }
        }
    }

    override fun radioButtonClicked(position: Int, text: String, unit: String) {
        lastPosition = position
        convertDialogInterface.apply {
            getOtherValues(position, positionKey)
            texts(text, unit)
        }
        GlobalScope.launch {
            delay(25)
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        convertDialogInterface = context as ConvertDialogInterface
    }

    private fun whichView(id: Int): MutableList<RecyclerDataClass> {
        val context = context as Context
        return when (id) {

            R.id.Mass -> Mass(context).getList()

            R.id.prefixes -> Prefix(context).getList()

            R.id.Temperature -> Temperature(context).getList()

            else -> mutableListOf()
        }
    }

    private fun saveData() {
        sharedPreferences.apply {
            with(edit()) {
                putInt(string, lastPosition)
                apply()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        saveData()
        super.onDismiss(dialog)
    }

    private fun filter(
        dataSet: MutableList<RecyclerDataClass>,
        searchText: String
    ): MutableList<RecyclerDataClass> {
        val locale = Locale.getDefault()
        val mainText = searchText.trim().toLowerCase(locale)
        return mutableListOf<RecyclerDataClass>().apply {
            for (i in dataSet) {
                val text = i.quantity.toLowerCase(locale)
                val unit = i.correspondingUnit.toLowerCase(locale)
                if (text.contains(mainText) || unit.contains(mainText))
                    add(i)
            }
        }
    }

    interface ConvertDialogInterface {
        fun texts(text: String, unit: String)
        fun getOtherValues(position: Int, positionKey: String)
    }
}