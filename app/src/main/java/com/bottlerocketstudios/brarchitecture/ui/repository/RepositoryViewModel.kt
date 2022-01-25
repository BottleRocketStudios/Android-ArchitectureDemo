package com.bottlerocketstudios.brarchitecture.ui.repository

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.ui.BaseBindableViewModel
import com.bottlerocketstudios.brarchitecture.ui.DbViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.util.formattedUpdateTime
import com.xwray.groupie.viewbinding.BindableItem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class RepositoryViewModel(val repository: Repository) : DbViewModel, KoinComponent, BaseBindableViewModel() {
    private val context by inject<Context>()
    val formattedUpdateTime = repository.updated.formattedUpdateTime(context)

    override fun getItemFactory(): (BaseBindableViewModel) -> BindableItem<ViewDataBinding> {
        return { vm -> ViewModelItem(vm as RepositoryViewModel, R.layout.item_repository) }
    }
}
