package com.example.unitconverter

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
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
    lateinit var listener: SortDialogInterface


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dial = Dialog(context!!, R.style.sortDialogStyle)
        val screenWidth = resources.displayMetrics.widthPixels
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, null)
        dial.setContentView(view)
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        params.apply {
            width = if (isPortrait) screenWidth - 16.dpToInt() else round(screenWidth / 1.7).toInt()
            bottomMargin = if (isPortrait) 15.dpToInt() else 2.dpToInt()
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        view.apply {
            layoutParams = params
            firstGroup = findViewById(R.id.firstGroup)
            secondGroup = findViewById(R.id.secondGroup)
            doneButton = findViewById(R.id.done)
            cancelButton = findViewById(R.id.cancel_button)
            useDefault = findViewById(R.id.default_button)
        }

        val sharedPreferences = activity?.getSharedPreferences(
            "com.example.unit_converter.first_selection",
            Context.MODE_PRIVATE
        )


        val firstSelection = sharedPreferences?.getInt("firstSelection", -1)
        val secondSelection = sharedPreferences?.getInt("secondSelection", -1)
        if (firstSelection != -1) {
            firstGroup.check(firstSelection!!)
            secondGroup.check(secondSelection!!)
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
        useDefault.setOnCheckedChangeListener { _, isChecked ->
            firstGroup.setStates(state = !isChecked)
            secondGroup.setStates(state = !isChecked)
            checked = isChecked
        }
        doneButton.setOnClickListener {
            listener.selection(
                if (!checked) firstGroup.checkedRadioButtonId else -1,
                if (!checked) secondGroup.checkedRadioButtonId else -1
            )
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val sharedPreferences = activity?.getSharedPreferences(
            "com.example.unit_converter.first_selection",
            Context.MODE_PRIVATE
        ) ?: return

        with(sharedPreferences.edit()) {
            putInt("firstSelection", firstGroup.checkedRadioButtonId)
            putInt("secondSelection", secondGroup.checkedRadioButtonId)
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

    private fun Int.dpToInt(): Int = dpToInt(context!!)
}