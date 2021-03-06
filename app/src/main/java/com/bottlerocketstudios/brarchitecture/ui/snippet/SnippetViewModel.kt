package com.bottlerocketstudios.brarchitecture.ui.snippet

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.Snippet
import com.bottlerocketstudios.brarchitecture.ui.BaseBindableViewModel
import com.bottlerocketstudios.brarchitecture.ui.DbViewModel
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.util.formattedUpdateTime
import com.xwray.groupie.viewbinding.BindableItem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class SnippetViewModel(val snippet: Snippet) : DbViewModel, KoinComponent, BaseBindableViewModel() {
    private val context by inject<Context>()
    val formattedUpdateTime = snippet.updated.formattedUpdateTime(context)

    override fun getItemFactory(): (BaseBindableViewModel) -> BindableItem<ViewDataBinding> {
        return { vm -> ViewModelItem(vm as SnippetViewModel, R.layout.item_snippet) }
    }
}
