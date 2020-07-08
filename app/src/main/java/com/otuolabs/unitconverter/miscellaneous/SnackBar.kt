package com.otuolabs.unitconverter.miscellaneous

import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

data class SnackBar(
    var view: View? = null,
    var resId: Int? = null,
    var duration: Int? = null,
    var callback: BaseTransientBottomBar.BaseCallback<Snackbar>? = null,
    var action: Action? = null,
    var animationMode: Int? = null
)

data class Action(var resId: Int? = null, var listener: View.OnClickListener? = null)

inline fun showSnack(block: SnackBar.() -> Unit) =
    SnackBar().apply(block).run {
        Snackbar.make(view!!, resId!!, duration!!)
            .also { snack: Snackbar ->
                callback?.let {
                    snack.addCallback(it)
                }
                action?.let {
                    snack.setAction(it.resId!!, it.listener)
                }
                animationMode?.let {
                    snack.animationMode = it
                }
                snack.show()
            }
    }

inline fun SnackBar.setAction(action: Action.() -> Unit) {
    this.action = Action().apply(action)
}
