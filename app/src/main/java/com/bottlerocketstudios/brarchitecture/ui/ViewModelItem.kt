package com.bottlerocketstudios.brarchitecture.ui

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import timber.log.Timber.e
import kotlin.reflect.full.cast
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf

class ViewModelItem <T : BaseBindableViewModel> (val viewModel: T, val _layout: Int) : BindableItem<ViewDataBinding>() {

    override fun bind(viewBinding: ViewDataBinding, position: Int) {
        val members = viewBinding::class.members
        val setViewModels = members.filter {
            it.name == "setViewModel" &&
                    it.parameters.size == 2 &&
                    it.parameters[0].type.isSubtypeOf(viewBinding::class.createType()) &&
                    it.parameters[1].type.isSubtypeOf(viewModel::class.createType())
        }
        if (setViewModels.size == 1) {
            setViewModels.first().call(viewBinding, viewModel)
        } else {
            e("Expected ${viewBinding::class.qualifiedName} to have exactly one member function setViewModel")
        }
    }

    override fun getLayout(): Int {
        return _layout
    }

    override fun isSameAs(other: Item<*>): Boolean {
        val retval: Boolean
        if (super.isSameAs(other)) {
            retval = true
        } else {
            if (other != null && other::class == this::class) {
                retval = (this::class.cast(other).viewModel.equals(this.viewModel))
            } else {
                retval = false
            }
        }
        return retval
    }

    override fun equals(other: Any?): Boolean {
        if (other is Item<*>) {
            return isSameAs(other)
        } else
            return super.equals(other)
    }

    override fun initializeViewBinding(view: View): ViewDataBinding {
        return DataBindingUtil.bind(view)!!
    }
}
