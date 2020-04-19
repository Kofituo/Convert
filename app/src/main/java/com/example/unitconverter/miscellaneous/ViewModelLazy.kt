package com.example.unitconverter.miscellaneous

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KClass

class ViewModelLazy<VM : ViewModel>(
    private val activity: ViewModelStoreOwner,
    private val viewModelClass: KClass<VM>
) : Lazy<VM> {

    private var cached: VM? = null

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel.isNull()) {
                ViewModelProvider(activity).get(viewModelClass.java).also {
                    cached = it
                }
            } else {
                viewModel
            }
        }

    override fun isInitialized(): Boolean = cached.isNotNull()
}

inline fun <reified VM : ViewModel> FragmentActivity.viewModels(): Lazy<VM> =
    ViewModelLazy(this, VM::class)