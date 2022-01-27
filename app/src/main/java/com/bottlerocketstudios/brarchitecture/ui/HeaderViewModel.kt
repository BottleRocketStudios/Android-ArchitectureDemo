package com.bottlerocketstudios.brarchitecture.ui

import androidx.databinding.ViewDataBinding
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.util.StringIdHelper
import com.xwray.groupie.viewbinding.BindableItem

data class HeaderViewModel(val text: StringIdHelper) : DbViewModel, BaseBindableViewModel() {
    override fun getItemFactory(): (BaseBindableViewModel) -> BindableItem<ViewDataBinding> {
        return { vm -> ViewModelItem(vm as HeaderViewModel, R.layout.item_header) }
    }
}
