package com.otuolabs.unitconverter.miscellaneous

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.transition.Transition
import android.transition.TransitionValues
import android.transition.Visibility
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.otuolabs.unitconverter.AdditionItems
import com.otuolabs.unitconverter.R


class ScaleTransition(context: Context, attrs: AttributeSet) : Visibility(context, attrs) {

    private var mDisappearedScale = 0f

    override fun captureStartValues(transitionValues: TransitionValues) {
        super.captureStartValues(transitionValues)
        transitionValues.values[PROPERTY_NAME_SCALE_X] = transitionValues.view.scaleX
        transitionValues.values[PROPERTY_NAME_SCALE_Y] = transitionValues.view.scaleY
    }

    /**
     * @param disappearedScale Value of scale on start of appearing or in finish of disappearing.
     * Default value is 0. Can be useful for mixing some Visibility
     * transitions, for otuolabs Scale and Fade
     * @return This Scale object.
     */
    private fun setDisappearedScale(disappearedScale: Float): ScaleTransition {
        require(disappearedScale >= 0f) { "disappearedScale cannot be negative!" }
        mDisappearedScale = disappearedScale
        return this
    }

    init {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleTransition)
        setDisappearedScale(
                a.getFloat(R.styleable.ScaleTransition_Scale_disappearedScale, mDisappearedScale)
        )
        a.recycle()
    }

    private fun createAnimation(
        view: View,
        startScale: Float,
        endScale: Float,
        values: TransitionValues?
    ): Animator {
        val initialScaleX = view.scaleX
        val initialScaleY = view.scaleY
        var startScaleX = initialScaleX * startScale
        val endScaleX = initialScaleX * endScale
        var startScaleY = initialScaleY * startScale
        val endScaleY = initialScaleY * endScale
        if (values != null) {
            val savedScaleX = values.values[PROPERTY_NAME_SCALE_X] as Float
            val savedScaleY = values.values[PROPERTY_NAME_SCALE_Y] as Float
            // if saved value is not equal initial value it means that previous
            // transition was interrupted and in the onTransitionEnd
            // we've applied endScale. we should apply proper value to
            // continue animation from the interrupted state
            if (savedScaleX != initialScaleX) {
                startScaleX = savedScaleX
            }
            if (savedScaleY != initialScaleY) {
                startScaleY = savedScaleY
            }
        }
        view.scaleX = startScaleX
        view.scaleY = startScaleY
        val animator: Animator = mergeAnimators(
            ObjectAnimator.ofFloat(view, View.SCALE_X, startScaleX, endScaleX),
            ObjectAnimator.ofFloat(view, View.SCALE_Y, startScaleY, endScaleY)
        )
        addListener(object : TransitionListener {
            override fun onTransitionEnd(transition: Transition) {
                view.scaleX = initialScaleX
                view.scaleY = initialScaleY
                transition.removeListener(this)
            }

            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {
            }
        })
        return animator
    }

    override fun onAppear(
            sceneRoot: ViewGroup,
            view: View,
            startValues: TransitionValues?,
            endValues: TransitionValues?
    ): Animator = createAnimation(view, mDisappearedScale, 1f, startValues)

    override fun onDisappear(
        sceneRoot: ViewGroup,
        view: View,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator = createAnimation(view, 1f, mDisappearedScale, startValues)

    companion object {
        const val PROPERTY_NAME_SCALE_X = "${AdditionItems.pkgName}scale:scaleX"
        const val PROPERTY_NAME_SCALE_Y = "${AdditionItems.pkgName}scale:scaleY"

        fun mergeAnimators(animator2: Animator, animator1: Animator): Animator = run {
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(animator1, animator2)
            animatorSet
        }
    }
}
