package com.bottlerocketstudios.brarchitecture.ui

import androidx.databinding.ViewDataBinding
import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item
import com.xwray.groupie.databinding.BindableItem

/**
 * I can't find a way to make this generic take the actual ViewDataBinding, so everything is generic
 * as ViewDataBinding up until inside the corresponding Item, which has to cast it to the correct
 * binding
 *
 * See CategoryItem for an example
 */
abstract class BaseBindableViewModel : Group {
    private val delegateItem: BindableItem<ViewDataBinding> by lazy {
        DelegateItemFactory.getItem(this)
    }

    protected abstract fun getItemFactory(): (BaseBindableViewModel) -> BindableItem<ViewDataBinding>

    private object DelegateItemFactory {
        fun getItem(me: BaseBindableViewModel): BindableItem<ViewDataBinding> {
            return me.getItemFactory()(me)
        }
    }

    override fun getItemCount(): Int {
        return delegateItem.itemCount
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        delegateItem.unregisterGroupDataObserver(groupDataObserver)
    }

    override fun getItem(position: Int): Item<*> {
        return delegateItem.getItem(position)
    }

    override fun getPosition(item: Item<*>): Int {
        return delegateItem.getPosition(item)
    }

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        return delegateItem.registerGroupDataObserver(groupDataObserver)
    }
}
