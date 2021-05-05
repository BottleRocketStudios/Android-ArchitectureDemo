package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.databinding.ViewDataBinding
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.ui.BaseBindableViewModel
import com.bottlerocketstudios.brarchitecture.ui.DbViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.xwray.groupie.viewbinding.BindableItem

data class RepositoryViewModel(val repository: Repository) : DbViewModel, BaseBindableViewModel() {
    override fun getItemFactory(): (BaseBindableViewModel) -> BindableItem<ViewDataBinding> {
        return { vm -> ViewModelItem(vm as RepositoryViewModel, R.layout.item_repository) }
    }
}
