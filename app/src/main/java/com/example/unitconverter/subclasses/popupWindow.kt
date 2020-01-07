package com.example.unitconverter.subclasses

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintSet
import com.example.unitconverter.*
import com.google.android.material.card.MaterialCardView


/* popup which takes context for inflation
    anchor or view its attached to

 */


class MyPopupWindow(private val context: Context, private val anchor: View, resInt: Int) :
    PopupWindow(context) {

    private var xPosition = -1

    private var yPosition = -1

    private var anchorX = 0

    private var anchorY = 0

    private var anchorWidth = anchor.width

    private lateinit var quickAction: View

    private var quickActionWidth = -1

    private val mWindow = PopupWindow(context)

    private val quickActionHeight =
        // used to convert dp to pixels
        context.resources.getDimensionPixelSize(R.dimen.quick_action_height)

    private var xOffset: Int = 0

    private var whichSide = 0

    init {
        setQuickActonView(resInt)

        setOnDismissListener {
            if (!endAnimation()) {
                animateFinal?.start()
                longPress = false
            }
        }
    }

    override fun isShowing(): Boolean = mWindow.isShowing

    private fun settingConstraint(constraintLayout: View) {
        constraintLayout as ConstraintLayout
        val constraintSet = ConstraintSet()

        val arrowImageView = constraintLayout.findViewById<ImageView>(R.id.arrow)

        val firstConstraintLayout = constraintLayout.findViewById<MaterialCardView>(R.id.firstCont)

        val secondConstraintLayout =
            constraintLayout.findViewById<ConstraintLayout>(R.id.secondCont)

        val arrowViewParameters = arrowImageView.layoutParams as ViewGroup.MarginLayoutParams

        val firstConstraintLayoutParameters =
            firstConstraintLayout.layoutParams as ViewGroup.MarginLayoutParams
        val secondConstraintLayoutParameters =
            secondConstraintLayout.layoutParams as ViewGroup.MarginLayoutParams

        arrowImageView.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp)

        constraintSet.apply {
            clone(constraintLayout)
            // clear top constraint of arrow icon
            clear(R.id.arrow, ConstraintSet.TOP)
            connect(
                R.id.arrow,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
            )

            // reset constraints of firstCont
            clear(R.id.firstCont, ConstraintSet.BOTTOM)
            connect(R.id.firstCont, ConstraintSet.BOTTOM, R.id.secondCont, ConstraintSet.TOP, 0)
            connect(
                R.id.firstCont,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
            )
            connect(R.id.firstCont, ConstraintSet.TOP, R.id.arrow, ConstraintSet.BOTTOM, 0)
            connect(
                R.id.firstCont,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
            )

            //resetting secondCont
            clear(R.id.secondCont, ConstraintSet.BOTTOM)
            clear(R.id.secondCont, ConstraintSet.TOP)
            connect(
                R.id.secondCont,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
            )
            connect(R.id.secondCont, ConstraintSet.TOP, R.id.firstCont, ConstraintSet.BOTTOM, 0)

            applyTo(constraintLayout)
        }
        firstConstraintLayoutParameters.topMargin = 13.dpToInt()
        //apply starts here
        secondConstraintLayoutParameters.apply {
            bottomMargin = 0
            topMargin = 3.dpToInt()
        }

        arrowViewParameters.apply {
            bottomMargin = 96.dpToInt()
            topMargin = 0
        }
        //updating margin changes
        firstConstraintLayout.requestLayout()
        secondConstraintLayout.requestLayout()
        arrowImageView.requestLayout()
    }

    private fun setQuickActonView(resInt: Int) {
        //setting quick action view
        //getting layout inflater
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        quickAction = inflater.inflate(resInt, null)

        quickAction.layoutParams =
            ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        //setting the content view of the popup
        contentView = quickAction
    }

    fun determinePosition() {
        val displayMetrics = DisplayMetrics()

        val windowManager: WindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        windowManager.defaultDisplay.getMetrics(displayMetrics)

        //getting screen width
        val screenWidth = displayMetrics.widthPixels
        //preShow

        preShow()

        val locationOfAnchorArray = IntArray(2)

        anchor.getLocationOnScreen(locationOfAnchorArray)

        // measure the popup window
        quickAction.measure(0, 0)

        quickActionWidth = quickAction.measuredWidth

        //getting x and y coordinates of anchor
        anchorX = locationOfAnchorArray[0]
        anchorY = locationOfAnchorArray[1]


        /**
         * TO determine the x position of the view
         * anchor X + quick action width determines whether it will reach the end of the
         * screen or not
         * if that is less than the screen width it should try and let the view be in
         * between it
         *
         * **Implementation**
         *
         * x end of the view to start of quick action = xOffset
         */

        // first it was if (anchorX + quickActionWidth <= screenWidth)
        xOffset = (quickActionWidth - anchorWidth) / 2

        val testPosition = anchorX + quickActionWidth - xOffset

        xPosition = if (testPosition <= screenWidth) {
            // if the difference is less than 0 then it means there's no space
            val xDifference = anchorX - xOffset

            if (xDifference < 0) {
                //its at the left
                whichSide = 0
                anchorX

            } else {
                //its in the middle
                whichSide = 1
                xDifference
            }
        } else {
            //its at the right side
            whichSide = 2
            val anchorRightX = anchorX + anchorWidth
            anchorRightX - quickActionWidth
        }
        // make sure the popup doesn't cross the app bar

        val appBarOffset = app_bar_bottom + statusBarHeight + 2.dpToInt()
        yPosition = if (anchorY - quickActionHeight <= appBarOffset) {
            settingConstraint(quickAction)
            // its up
            anchorY + anchorWidth
        } else {
            //its down
            anchorY - quickActionHeight
        }
        determineArrowPosition()
    }

    private fun determineArrowPosition() {
        val anchorMidWidth = anchorWidth / 2
        val arrow = quickAction.findViewById<ImageView>(R.id.arrow)
        arrow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        val arrowViewParameters = arrow.layoutParams as ViewGroup.MarginLayoutParams

        //calculating margin start
        val arrowWidth = arrow.measuredWidth

        when (whichSide) {
            0 -> {
                arrowViewParameters.marginStart = anchorMidWidth - (arrowWidth / 2)
                animationStyle =
                    if (yPosition == anchorY + anchorWidth) R.style.DownLeft else R.style.UpLeft
            }
            1 -> {
                val diff = quickActionWidth - arrowWidth
                arrowViewParameters.marginStart = diff / 2
                animationStyle =
                    if (yPosition == anchorY + anchorWidth) R.style.DownCenter else R.style.UpCenter
            }
            2 -> {
                val diff = quickActionWidth + arrowWidth / 2
                arrowViewParameters.marginStart = diff / 2
                animationStyle =
                    if (yPosition == anchorY + anchorWidth) R.style.DownRight else R.style.UpRight
            }
        }
    }

    fun setDrawable(drawable: Drawable) {
        val textView = quickAction.findViewById<TextView>(R.id.quick_action_textView)
        // creates new drawable from old one
        val drawableCopy = drawable.mutate().constantState?.newDrawable()?.mutate()
        drawableCopy?.setBounds(0, 0, 30.dpToInt(), 30.dpToInt())
        textView.setCompoundDrawables(drawableCopy, null, null, null)
    }

    fun show() = mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPosition, yPosition)

    override fun setContentView(contentView: View) {
        //quickAction = contentView
        mWindow.contentView = contentView
    }

    private fun preShow() {
        mWindow.apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = quickActionHeight
            isTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isOutsideTouchable = true
            isFocusable = true
            mWindow.contentView = quickAction
        }
    }

    override fun dismiss() = mWindow.dismiss()

    override fun setTouchInterceptor(l: View.OnTouchListener?) =
        mWindow.setTouchInterceptor(l)

    override fun setOutsideTouchable(touchable: Boolean) {
        mWindow.isOutsideTouchable = touchable
    }

    override fun setOnDismissListener(onDismissListener: OnDismissListener?) =
        mWindow.setOnDismissListener(onDismissListener)

    private fun Int.dpToInt(): Int = dpToInt(context)

    override fun setAnimationStyle(animationStyle: Int) {
        mWindow.animationStyle = animationStyle
    }

    override fun getAnimationStyle(): Int = mWindow.animationStyle
}