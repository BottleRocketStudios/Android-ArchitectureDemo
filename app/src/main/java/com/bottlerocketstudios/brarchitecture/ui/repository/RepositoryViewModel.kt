package com.bottlerocketstudios.brarchitecture.ui.repository

import androidx.databinding.ViewDataBinding
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.domain.models.Repository
import com.bottlerocketstudios.brarchitecture.ui.BaseBindableViewModel
import com.bottlerocketstudios.brarchitecture.ui.DbViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.util.StringIdHelper
import com.bottlerocketstudios.brarchitecture.ui.util.formattedUpdateTime
import com.xwray.groupie.viewbinding.BindableItem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Clock

data class RepositoryViewModel(val repository: Repository) : DbViewModel, KoinComponent, BaseBindableViewModel() {
    private val clock by inject<Clock>()
    val formattedUpdateTime: StringIdHelper = repository.updated.formattedUpdateTime(clock)

    override fun getItemFactory(): (BaseBindableViewModel) -> BindableItem<ViewDataBinding> {
        return { vm -> ViewModelItem(vm as RepositoryViewModel, R.layout.item_repository) }
    }
}
