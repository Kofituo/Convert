package com.example.unitconverter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.example.unitconverter.AdditionItems.pkgName
import com.example.unitconverter.Utils.dpToInt
import com.example.unitconverter.miscellaneous.editPreferences
import com.example.unitconverter.miscellaneous.get
import com.example.unitconverter.miscellaneous.layoutParams
import com.example.unitconverter.miscellaneous.put
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.math.round

class BottomSheetFragment : DialogFragment() {

    private lateinit var firstGroup: RadioGroup
    private lateinit var secondGroup: RadioGroup
    private lateinit var doneButton: MaterialButton
    private lateinit var cancelButton: MaterialButton
    private lateinit var useDefault: SwitchMaterial
    private var checked = false
    private lateinit var listener: SortDialogInterface

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dial = Dialog(requireContext(), R.style.sortDialogStyle)

        val screenWidth = resources.displayMetrics.widthPixels
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, null)
        dial.setContentView(view)

        view.layoutParams<ViewGroup.MarginLayoutParams> {
            width = if (isPortrait) screenWidth - 16.dpToInt() else round(screenWidth / 1.7).toInt()
            bottomMargin = if (isPortrait) 15.dpToInt() else 2.dpToInt()
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        view.apply {
            firstGroup = findViewById(R.id.firstGroup)
            secondGroup = findViewById(R.id.secondGroup)
            doneButton = findViewById(R.id.done)
            cancelButton = findViewById(R.id.cancel_button)
            useDefault = findViewById(R.id.default_button)
        }

        val sharedPreferences = activity?.getSharedPreferences(
            "$pkgName.sortingSelection",
            Context.MODE_PRIVATE
        )?.apply {
            firstGroup.check(if (get("titleIsChecked")) R.id.titleButton else R.id.recent)
            secondGroup.check(if (get("ascendingIsChecked")) R.id.ascending else R.id.descending)
        }
        useDefault.setOnCheckedChangeListener { _, isChecked ->
            firstGroup.setStates(state = !isChecked)
            secondGroup.setStates(state = !isChecked)
            checked = isChecked
        }
        sharedPreferences?.get<Boolean>("useDefault") {
            if (this) useDefault.isChecked = true
        }
        // get original values
        val originalFirstSelection = firstGroup.checkedRadioButtonId

        val originalSecondSelection = secondGroup.checkedRadioButtonId
        val switchValue = useDefault.isChecked

        cancelButton.setOnClickListener {
            firstGroup.check(originalFirstSelection)
            secondGroup.check(originalSecondSelection)
            useDefault.isChecked = switchValue
            dismiss()
        }
        doneButton.setOnClickListener {
            listener.selection(
                if (!checked) firstGroup.checkedRadioButtonId else -1,
                if (!checked) secondGroup.checkedRadioButtonId else -1
            )
            saveData()
            dismiss()
        }
        dial.setCanceledOnTouchOutside(true)
        dial.window?.setGravity(if (isPortrait) Gravity.BOTTOM else Gravity.CENTER)
        return dial
    }

    interface SortDialogInterface {
        fun selection(firstSelection: Int, secondSelection: Int) {
        }
    }

    private fun saveData() {
        val sharedPreferences = activity?.getSharedPreferences(
            "$pkgName.sortingSelection",
            Context.MODE_PRIVATE
        ) ?: return

        editPreferences(sharedPreferences) {
            put<Boolean> {
                key = "titleIsChecked"
                value = firstGroup.checkedRadioButtonId == R.id.titleButton
            }
            put<Boolean> {
                key = "ascendingIsChecked"
                value = secondGroup.checkedRadioButtonId == R.id.ascending
            }
            put<Boolean> {
                key = "useDefault"
                value = useDefault.isChecked
            }
            apply()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as SortDialogInterface
    }

    private fun RadioGroup.setStates(state: Boolean) {
        for (i in 0 until this.childCount) this.getChildAt(i).isEnabled = state
    }

    private fun Int.dpToInt(): Int = dpToInt(requireContext())
}