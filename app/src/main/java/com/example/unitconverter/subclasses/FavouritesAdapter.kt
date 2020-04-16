package com.example.unitconverter.subclasses

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

    lateinit var comparatr: Comparator<FavouritesData>

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
                imageView.setImageDrawable(drawable?.let {
                    imageView.resources.getDrawable(it, null)
                })
                mainTextView.text = topText
                metadataTextView.text = metadata
            }
        }
    }
}