package com.example.unitconverter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.subclasses.ConvertViewModel
import com.example.unitconverter.subclasses.MyAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.round


class ConvertDialog : DialogFragment() {

    lateinit var cancelButton: MaterialButton
    lateinit var searchBar: TextInputLayout
    lateinit var recyclerView: RecyclerView
    lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    var myDataset = arrayOf("kofi", "akua", "abena", "akua")

    @SuppressLint("InflateParams")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val dialog = Dialog(context!!, R.style.sortDialogStyle)
        val view = LayoutInflater.from(context).inflate(R.layout.items_list, null)
        dialog.setContentView(view)
        view.apply {
            cancelButton = findViewById(R.id.cancel_button)
            searchBar = findViewById(R.id.search_bar)
            val params = layoutParams
            params.width =
                if (isPortrait) (screenWidth / 7) * 6 else round(screenWidth / 1.6).toInt()

            params.height =
                if (isPortrait) round(screenHeight * 0.9).toInt() else ViewGroup.LayoutParams.WRAP_CONTENT
            layoutParams = params
            recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
                setHasFixedSize(true)
                viewManager = LinearLayoutManager(context)
                layoutManager = viewManager
                viewAdapter = MyAdapter(myDataset)

                adapter = viewAdapter
            }
        }

        activity?.run {
            ViewModelProviders.of(this)[ConvertViewModel::class.java]
        }?.apply {
            setDialogColors(randomInt)
        }
        Log.e("sdad","${screenHeight * 0.8}")
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
                Log.e("ad","${recyclerView.height}")
                dismiss()
            }
        }
    }
}