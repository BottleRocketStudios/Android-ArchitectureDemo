package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.databinding.ViewDataBinding
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.BaseBindableViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.xwray.groupie.viewbinding.BindableItem

data class FolderHeaderViewModel(val path: String, val count: Int) : BaseBindableViewModel() {
    override fun getItemFactory(): (BaseBindableViewModel) -> BindableItem<ViewDataBinding> {
        return { vm -> ViewModelItem(vm as FolderHeaderViewModel, R.layout.item_repo_header) }
    }
}
