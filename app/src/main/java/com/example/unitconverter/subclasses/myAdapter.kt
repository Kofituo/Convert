package com.example.unitconverter.subclasses

import android.content.res.ColorStateList
import android.text.TextUtils
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.unitconverter.R
import com.example.unitconverter.Utils.dpToInt
import com.google.android.material.radiobutton.MaterialRadioButton
import java.util.*

data class RecyclerDataClass(val quantity: String, val correspondingUnit: String) {
    override fun hashCode(): Int {
        var result = quantity.hashCode()
        result = 31 * result + correspondingUnit.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as RecyclerDataClass
        if (quantity != other.quantity) return false
        if (correspondingUnit != other.correspondingUnit) return false
        return true
    }
}

class MyAdapter(
    private val dataSet: MutableList<RecyclerDataClass>,
    private val colorInt: Int,
    private val comparator: Comparator<RecyclerDataClass>
) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    // view holder class
    var lastPosition: Int = -1
    var boolean = false

    private lateinit var listener: OnRadioButtonsClickListener

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var radioButton: MaterialRadioButton
        var radioTextView: TextView

        init {
            val isRTL =
                TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL

            view.apply {
                radioButton = findViewById(R.id.radioButtons)
                radioTextView = findViewById<TextView>(R.id.radioText).apply {
                    if (isRTL) {
                        val params = layoutParams as ViewGroup.MarginLayoutParams
                        params.marginEnd = 30.dpToInt(context)
                        layoutParams = params
                    }
                }
            }
            radioButton.setOnClickListener {
                lastPosition = adapterPosition
                notifyItemRangeChanged(0, itemCount)
                listener.radioButtonClicked(
                    adapterPosition,
                    radioButton.text.toString(),
                    radioTextView.text.toString()
                )
            }
        }
    }

    private val mSortedList: SortedList<RecyclerDataClass> =
        SortedList(RecyclerDataClass::class.java,
            object : SortedList.Callback<RecyclerDataClass>() {
                override fun areItemsTheSame(item1: RecyclerDataClass, item2: RecyclerDataClass)
                        : Boolean {
                    Log.e("1", "1  ${item1 == item2}  $item1  $item2")
                    return item1.quantity == item2.quantity && item1.correspondingUnit == item2.correspondingUnit
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    notifyItemMoved(fromPosition, toPosition)
                }

                override fun onChanged(position: Int, count: Int) {

                    notifyItemRangeChanged(position, count)
                }

                override fun onInserted(position: Int, count: Int) {
                    Log.e("9", "$position  $count")
                    notifyItemRangeInserted(position, count)
                }

                override fun onRemoved(position: Int, count: Int) {
                    notifyItemRangeRemoved(position, count)
                }

                override fun compare(o1: RecyclerDataClass?, o2: RecyclerDataClass?): Int =
                    comparator.compare(o1, o2)

                override fun areContentsTheSame(
                    oldItem: RecyclerDataClass?,
                    newItem: RecyclerDataClass?
                ): Boolean {
                    return oldItem == newItem
                }
            })

    // rest of the class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_view,
                parent,
                false
            )
        view.findViewById<MaterialRadioButton>(R.id.radioButtons).buttonTintList =
            ColorStateList.valueOf(colorInt)

        return MyViewHolder(view)
    }

    override fun getItemCount() = mSortedList.size()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.radioButton.apply {
            text = if (boolean) mSortedList[position].quantity else dataSet[position].quantity
            isChecked = lastPosition == position
        }
        holder.radioTextView.text =
            if (boolean) mSortedList[position].correspondingUnit
            else dataSet[position].correspondingUnit
    }


    fun sparseArray(): ArrayMap<String, Int> {
        return ArrayMap<String, Int>(31).apply {
            dataSet
        }
    }

    fun add(dataSet: MutableList<RecyclerDataClass>) = mSortedList.addAll(dataSet)
    fun replaceAll(dataSet: MutableList<RecyclerDataClass>) {
        mSortedList.apply {
            beginBatchedUpdates()
            //its in reverse so the indexing isn't affected
            //if it were 0 to size when we remove 0 1 now becomes original 2 and so on
            for (i in size() - 1 downTo 0)
                if (!dataSet.contains(get(i))) mSortedList.remove(get(i))
            addAll(dataSet)
            endBatchedUpdates()
        }
    }

    interface OnRadioButtonsClickListener {
        fun radioButtonClicked(position: Int, text: String, unit: String)
    }

    fun setOnRadioButtonsClickListener(listener: OnRadioButtonsClickListener) {
        this.listener = listener
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
}