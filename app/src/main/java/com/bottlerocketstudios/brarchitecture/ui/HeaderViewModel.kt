package com.bottlerocketstudios.brarchitecture.ui

import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import com.bottlerocketstudios.brarchitecture.R
import com.xwray.groupie.viewbinding.BindableItem

data class HeaderViewModel(@StringRes val text: Int) : DbViewModel, BaseBindableViewModel() {
    override fun getItemFactory(): (BaseBindableViewModel) -> BindableItem<ViewDataBinding> {
        return { vm -> ViewModelItem(vm as HeaderViewModel, R.layout.item_header) }
    }
}
