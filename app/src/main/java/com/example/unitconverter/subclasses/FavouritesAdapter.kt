package com.example.unitconverter.subclasses

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unitconverter.R
import com.example.unitconverter.miscellaneous.inflate

class FavouritesAdapter : RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    lateinit var dataSet: List<FavouritesData>

    lateinit var comparator: Comparator<FavouritesData>

    lateinit var activity: Activity

    private lateinit var favouritesItem: FavouritesItem

    private val cacheDrawable = LinkedHashMap<Int, Drawable>(30)

    companion object {
        inline fun favouritesAdapter(block: FavouritesAdapter.() -> Unit) =
            FavouritesAdapter().apply(block)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView
        val mainTextView: TextView
        val metadataTextView: TextView

        init {
            view.apply {
                imageView = findViewById(R.id.image)
                mainTextView = findViewById(R.id.top_text)
                metadataTextView = findViewById(R.id.metadata)
                setOnClickListener {
                    val pos = this@ViewHolder.adapterPosition
                    favouritesItem.startActivity(dataSet[pos])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate {
                resourceId = R.layout.favorites_recycler_view
                root = parent
            }
        )

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            dataSet[position].apply {
                Log.e("ths", "$this")
                imageView.setImageDrawable(
                    cacheDrawable[drawableId]
                        ?: activity.resources.getDrawable(drawableId!!, null)
                            .apply {
                                cacheDrawable[drawableId!!] = this
                                Log.e("caled", "callled ds ${dataSet[position]}")
                            }
                )
                imageView.contentDescription = topText
                mainTextView.text = topText
                metadataTextView.text = metadata
            }
        }
    }

    fun setFavouritesItemListener(favouritesItem: FavouritesItem) {
        this.favouritesItem = favouritesItem
    }

    interface FavouritesItem {
        fun startActivity(data: FavouritesData)
    }
}