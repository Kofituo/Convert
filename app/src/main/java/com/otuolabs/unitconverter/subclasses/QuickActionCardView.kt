package com.otuolabs.unitconverter.subclasses

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.otuolabs.unitconverter.AdditionItems.animationEnd
import com.otuolabs.unitconverter.AdditionItems.card
import com.otuolabs.unitconverter.AdditionItems.popupWindow
import com.otuolabs.unitconverter.R
import com.otuolabs.unitconverter.Utils.performVibration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuickActionCardView(context: Context, attributeSet: AttributeSet) :
    MaterialCardView(context, attributeSet) {

    private lateinit var selectItems: SelectItems

    init {
        setOnClickListener {
            popupWindow.dismiss()
            when (this.id) {
                R.id.firstCont -> {
                    //Toast.makeText(app_context, "Convert clicked", Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        animationEnd?.apply {
                            while (isRunning) continue
                        }
                        card?.apply {
                            startActivity()
                            updateArray()
                        }
                    }
                }
                R.id.selectItems -> {
                    selectItems.initiateSelections()
                }
                R.id.favourite -> {
                    selectItems.addOneToFavorites()
                }
                R.id.info -> {
                    selectItems.convertInfo()
                }
            }
            ///pw.dismiss()
        }

        setOnLongClickListener {
            performVibration(context)
            performClick()
        }
    }

    interface SelectItems {
        fun initiateSelections()
        fun addOneToFavorites()
        fun convertInfo()
    }

    fun setSelectionLister(selectItems: SelectItems) {
        this.selectItems = selectItems
    }
}