package com.example.unitconverter.subclasses

import android.content.res.ColorStateList
import android.graphics.Rect
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.unitconverter.R
import com.example.unitconverter.RecyclerDataClass
import com.example.unitconverter.Utils.dpToInt
import com.example.unitconverter.miscellaneous.inflate
import com.example.unitconverter.miscellaneous.layoutParams
import com.google.android.material.radiobutton.MaterialRadioButton
import java.util.*

class MyAdapter(
    private val dataSet: MutableList<RecyclerDataClass>,
    private val colorInt: Int,
    private val comparator: Comparator<RecyclerDataClass>
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var lastPosition: Int = -1

    /**If true use the filtered data set else use the original one*/
    var useFilteredList = false

    private lateinit var listener: OnRadioButtonsClickListener

    /** View holder class **/
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val radioButton: MyRadioButton
        val radioTextView: TextView

        init {
            val isRTL =
                TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL

            view.apply {
                radioButton = findViewById<MyRadioButton>(R.id.radioButtons)
                    .also {
                        post {
                            val parentHitArea = Rect()
                            val thisArea = Rect()
                            it.getHitRect(thisArea)
                            getHitRect(parentHitArea)
                            thisArea.left = parentHitArea.left
                            thisArea.right = parentHitArea.right
                            touchDelegate = TouchDelegate(thisArea, it)
                        }
                    }
                radioTextView = findViewById<TextView>(R.id.radioText).apply {
                    if (isRTL)
                        layoutParams<ViewGroup.MarginLayoutParams> {
                            marginEnd = 20.dpToInt(context)
                        }
                }
            }
            radioButton.setOnClickListener {
                //lastPosition = adapterPosition
                //lastPosition = radioButton.myId
                //notifyItemRangeChanged(0, itemCount)
                listener.radioButtonClicked(
                    radioButton.myId,
                    radioButton.text,
                    radioTextView.text
                )
            }
        }
    }

    // rest of the class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate {
                resourceId = R.layout.recycler_view_items
                root = parent
                attachToRoot = false
            }
        view.findViewById<MaterialRadioButton>(R.id.radioButtons).buttonTintList =
            ColorStateList.valueOf(colorInt)

        return MyViewHolder(view)
    }

    override fun getItemCount() = mSortedList.size()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.radioButton.apply {
            text =
                if (useFilteredList) mSortedList[position].quantity else dataSet[position].quantity
            myId = if (useFilteredList) mSortedList[position].id else dataSet[position].id
            isChecked = lastPosition == myId
        }
        holder.radioTextView.text =
            if (useFilteredList) mSortedList[position].correspondingUnit
            else dataSet[position].correspondingUnit
    }

    fun add(dataSet: MutableList<RecyclerDataClass>) = mSortedList.addAll(dataSet)

    fun replaceAll(dataSet: MutableList<RecyclerDataClass>) {
        mSortedList.apply {
            beginBatchedUpdates()
            //its in reverse so the indexing isn't affected
            //if it were 0 to size when we remove 0 1 now becomes original 2 and so on
            for (i in size() - 1 downTo 0)
                if (!dataSet.contains(this[i]))
                    this.remove(this[i])
            addAll(dataSet)
            endBatchedUpdates()
        }
    }

    interface OnRadioButtonsClickListener {
        fun radioButtonClicked(position: Int, text: CharSequence, unit: CharSequence)
    }

    fun setOnRadioButtonsClickListener(listener: OnRadioButtonsClickListener) {
        this.listener = listener
    }

    private val mSortedList =
        SortedList(RecyclerDataClass::class.java,
            object : SortedList.Callback<RecyclerDataClass>() {
                override fun areItemsTheSame(item1: RecyclerDataClass, item2: RecyclerDataClass) =
                    item1.quantity == item2.quantity &&
                            item1.correspondingUnit == item2.correspondingUnit

                override fun onMoved(fromPosition: Int, toPosition: Int) =
                    notifyItemMoved(fromPosition, toPosition)

                override fun onChanged(position: Int, count: Int) =
                    notifyItemRangeChanged(position, count)

                override fun onInserted(position: Int, count: Int) =
                    notifyItemRangeInserted(position, count)

                override fun onRemoved(position: Int, count: Int) =
                    notifyItemRangeRemoved(position, count)

                override fun compare(o1: RecyclerDataClass, o2: RecyclerDataClass) =
                    comparator.compare(o1, o2)

                override fun areContentsTheSame(
                    oldItem: RecyclerDataClass?,
                    newItem: RecyclerDataClass?
                ) = oldItem == newItem
            }, dataSet.size
        )
}
/*
fun add(data: RecyclerDataClass) = mSortedList.add(data)

fun remove(data: RecyclerDataClass) = mSortedList.remove(data)

fun remove(dataSet: MutableList<RecyclerDataClass>) {
    mSortedList.apply {
        beginBatchedUpdates()
        for (i in dataSet) remove(i)
        endBatchedUpdates()
    }
}
*/