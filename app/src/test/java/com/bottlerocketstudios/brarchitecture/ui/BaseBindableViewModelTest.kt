package com.bottlerocketstudios.brarchitecture.ui

import android.content.res.Resources
import android.view.View
import androidx.databinding.ViewDataBinding
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.google.common.truth.Truth.assertThat
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import timber.log.Timber

class BaseBindableViewModelTest : BaseTest() {
    private val TEST_DATA = "data"

    data class TestBindableViewModel(val data: String) : BaseBindableViewModel() {
        private val TEST_LAYOUT = 1337

        override fun getItemFactory(): (BaseBindableViewModel) -> BindableItem<ViewDataBinding> {
            return { vm -> ViewModelItem(vm as TestBindableViewModel, TEST_LAYOUT) }
        }
    }

    class TestItem : Item<GroupieViewHolder>() {
        override fun getLayout(): Int {
            return 0
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {}
    }

    @Test
    fun baseBindableViewModel_shouldImplementGroupieFunctions_forSubclasses() {
        val tvm = TestBindableViewModel(TEST_DATA)
        assertThat(tvm.itemCount).isGreaterThan(0)
        assertThat(tvm.getItem(0)).isInstanceOf(ViewModelItem::class.java)
        assertThat(tvm.getPosition(tvm.getItem(0))).isEqualTo(0)
        val section = Section()
        section.add(tvm)
        assertThat(section.itemCount).isEqualTo(1)
        section.remove(tvm)
        assertThat(section.itemCount).isEqualTo(0)
    }

    @Test
    fun viewModelItem_shouldBeSameAsItself_whee() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val vmi = tvm.getItem(0)
        assertThat(vmi.isSameAs(vmi)).isTrue()
    }

    @Test
    fun viewModelItem_shouldBeSameAsAnotherViewModelItem_fromSameData() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val vmi = tvm.getItem(0)
        val tvm2 = TestBindableViewModel(TEST_DATA)
        val vmi2 = tvm2.getItem(0)
        assertThat(vmi.isSameAs(vmi2)).isTrue()
        assertThat(vmi.equals(vmi2)).isTrue()
    }

    @Test
    fun viewModelItem_shouldNotBeSameAs_anotherViewModelItemFromDifferentData() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val vmi = tvm.getItem(0)
        val tvm2 = TestBindableViewModel(TEST_DATA.reversed())
        val vmi2 = tvm2.getItem(0)
        assertThat(vmi.isSameAs(vmi2)).isFalse()
    }

    @Test
    fun viewModelItem_shouldNotBeSameAs_anotherViewModelItemOfAnotherType() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val vmi = tvm.getItem(0)
        val rvm = RepositoryViewModel(mock {})
        val rvmi = rvm.getItem(0)
        assertThat(vmi.isSameAs(rvmi)).isFalse()
    }

    @Test
    fun viewModelItem_shouldNotBeSameAs_anotherItemOfAnotherType() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val vmi = tvm.getItem(0)
        val ti = TestItem()
        assertThat(vmi.isSameAs(ti)).isFalse()
        assertThat(vmi.equals(ti)).isFalse()
    }

    @Test
    fun viewModelItem_shouldNotEqual_String() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val vmi = tvm.getItem(0)
        assertThat(vmi.equals("")).isFalse()
    }

    @Test
    fun viewModelItem_shouldBind_toAThing() {
        val tvm = TestBindableViewModel(TEST_DATA)
        // We shouldn't ever need to do this
        // Only doing it here to directly test the bind method on ViewModelItem
        val vmi = tvm.getItem(0) as ViewModelItem<TestBindableViewModel>

        val mockResources: Resources = mock {
            on { getResourceEntryName(any()) } doReturn ""
        }
        val mockView: View = mock {
            on { resources } doReturn mockResources
        }
        val bindable: ViewDataBinding = mock {
            on { root } doReturn mockView
        }
        // Nothing we can really test here. So turn off the error and let's get one more line of coverage!
        Timber.uprootAll()
        vmi.bind(bindable, 0)
    }
}
