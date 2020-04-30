package com.example.unitconverter.subclasses

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.RadioButton
import android.widget.TextView
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.inflate
import com.example.unitconverter.miscellaneous.isNull

class PreferencesAdapter : BaseExpandableListAdapter() {

    lateinit var headers: List<String>
    lateinit var map: Map<Int, List<String>>
    private var colorInt: Int? = null
    private lateinit var checkedChildren: MutableMap<Int, List<Int>>

    fun setCheckedChildren(mutableMap: MutableMap<Int, List<Int>>) {
        checkedChildren = mutableMap
    }

    fun setColor(color: Int) {
        colorInt = color
    }

    fun getSelectedItems() = viewHolder.getSelectedItems()

    private val viewHolder = ViewHolder(3)

    class ViewHolder(int: Int) {
        private val map = mutableMapOf<Int, MutableMap<Int, View>>()

        init {
            for (i in 0..int)
                map[i] = mutableMapOf()
        }

        fun getSelectedItems(): LinkedHashMap<Int, List<Int>> {
            val finalMap = LinkedHashMap<Int, List<Int>>(map.size)
            for ((groupPosition, listOfViews) in map) {
                val list = ArrayList<Int>(listOfViews.size)
                for (items in listOfViews) {
                    val isChecked = items
                        .value
                        .findViewById<RadioButton>(R.id.decimal_notation)
                        .isChecked
                    if (isChecked)
                        list.add(items.key)
                }
                finalMap[groupPosition] = list
            }
            return finalMap
        }

        fun addView(groupPosition: Int, childPosition: Int, view: View) {
            map[groupPosition]?.put(childPosition, view)
        }

        fun getView(groupPosition: Int, childPosition: Int): View? {
            return map[groupPosition]?.get(childPosition)
        }

        fun getViewsInGroup(groupPosition: Int): MutableCollection<View>? {
            return map[groupPosition]?.values
        }
    }

    override fun getGroup(groupPosition: Int): Any? =
        headers[groupPosition]

    override fun isChildSelectable(groupPosition: Int, childPosition: Int) =
        true

    override fun hasStableIds(): Boolean = true

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var viewGroup = convertView
        if (viewGroup.isNull()) {
            viewGroup = LayoutInflater.from(parent?.context).inflate {
                resourceId = R.layout.preferences_group
            }
        }
        val textView = viewGroup.findViewById<TextView>(R.id.group_header)
        textView.text = headers[groupPosition]
        /*if (isExpanded) {
            viewGroup.findViewById<ImageView>(R.id.image)
                .apply {
                    RotateAnimation(
                        0f,
                        180f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                    ).apply {
                        duration = 400
                        startAnimation(this)
                        fillAfter = true
                    }
                }
        }*/
        return viewGroup
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        Log.e("child", " $groupPosition called")
        return if (groupPosition == 4) 1 else (map[groupPosition] ?: error("no view here")).size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? =
        viewHolder.getView(groupPosition, childPosition)

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    var i = 0
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val inflater = LayoutInflater.from(parent?.context)
        var nullView = viewHolder.getView(groupPosition, childPosition)
        if (nullView.isNull()) {
            //1,3,1,2
            when (groupPosition) {
                0, 1, 2, 3 -> {
                    nullView = inflater.inflate {
                        resourceId = R.layout.notation
                    }
                    nullView
                        .findViewById<RadioButton>(R.id.decimal_notation)
                        .apply {
                            text = map[groupPosition]?.get(childPosition)
                            setOnClickListener {
                                Log.e("radio", "radio")
                                //uncheck the others in the group
                                val views = viewHolder.getViewsInGroup(groupPosition)
                                    ?.map { it.findViewById<RadioButton>(R.id.decimal_notation) }
                                views?.run {
                                    for (view in this) {/*
                                findViewById<RadioButton>(R.id.decimal_notation)
                                    ?.isChecked = false*/
                                        Log.e(
                                            "equal",
                                            "${view === it}  ${view?.text}  ${(it as? RadioButton)?.text}"
                                        )
                                        if (view == it) continue
                                        view.isChecked = false
                                    }
                                }
                            }
                            /*if (checkedChildren[groupPosition]?.getOrNull(childPosition) ==
                                childPosition
                            ) isChecked = true*/
                        }
                    Log.e("null", "${map[groupPosition]?.get(childPosition)}")
                }
                4 -> {
                    nullView = inflater.inflate {
                        resourceId = R.layout.decimal_place
                    }
                }
                else -> TODO()
            }
            viewHolder.addView(groupPosition, childPosition, nullView)
        }
        return nullView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long =
        childPosition.toLong()

    override fun getGroupCount(): Int {
        return headers.size
    }
}