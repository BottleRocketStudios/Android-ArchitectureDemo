package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.databinding.ViewDataBinding
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.model.RepoFile
import com.bottlerocketstudios.brarchitecture.ui.BaseBindableViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.xwray.groupie.viewbinding.BindableItem

data class RepoFileViewModel(val file: RepoFile) : BaseBindableViewModel() {
    override fun getItemFactory(): (BaseBindableViewModel) -> BindableItem<ViewDataBinding> {
        return { vm -> ViewModelItem(vm as RepoFileViewModel, R.layout.item_file) }
    }
}
