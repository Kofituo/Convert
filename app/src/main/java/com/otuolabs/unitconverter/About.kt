package com.otuolabs.unitconverter

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.otuolabs.unitconverter.miscellaneous.layoutParams
import com.otuolabs.unitconverter.subclasses.AboutAdapter
import com.otuolabs.unitconverter.subclasses.SearchQuantityHolder
import kotlinx.android.synthetic.main.activity_about.*

class About : AppCompatActivity(), SearchQuantityHolder.Quantity {

    private inline val Int.gS get() = getString(this)

    private val recyclerViewList by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            RecyclerDataClass(R.string.share.gS, R.string.share_meta.gS),
            RecyclerDataClass(R.string.rate.gS, R.string.rate_meta.gS)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape)
            image.layoutParams = image.layoutParams<ConstraintLayout.LayoutParams> {
                guide.setGuidelinePercent(0.2f)
                matchConstraintPercentWidth = 0.11f
            }
        recycler_view.apply {
            adapter = AboutAdapter(recyclerViewList).apply { setOnItemClickListener(this@About) }
            layoutManager = LinearLayoutManager(this@About)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onQuantityClick(position: Int) {
    }

}