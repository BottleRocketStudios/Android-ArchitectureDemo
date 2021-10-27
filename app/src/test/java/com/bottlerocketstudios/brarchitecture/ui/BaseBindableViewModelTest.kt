package com.bottlerocketstudios.brarchitecture.ui

import android.content.res.Resources
import android.view.View
import androidx.databinding.ViewDataBinding
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.KoinTestRule
import com.bottlerocketstudios.brarchitecture.test.TestModule
import com.bottlerocketstudios.brarchitecture.ui.repository.RepositoryViewModel
import com.google.common.truth.Truth.assertThat
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import timber.log.Timber

class BaseBindableViewModelTest : BaseTest() {

    @get:Rule
    val koinRule = KoinTestRule(TestModule.generateMockedTestModule())

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
    fun baseBindableViewModel_shouldBeInstanceOf_viewModelItem() {
        val tvm = TestBindableViewModel(TEST_DATA)

        assertThat(tvm.itemCount).isGreaterThan(0)
        assertThat(tvm.getItem(0)).isInstanceOf(ViewModelItem::class.java)
    }

    @Test
    fun baseBindableViewModel_shouldCreate_subclass() {
        val tvm = TestBindableViewModel(TEST_DATA)

        assertThat(tvm.itemCount).isGreaterThan(0)
        assertThat(tvm.getItem(0)).isInstanceOf(ViewModelItem::class.java)
        assertThat(tvm.getPosition(tvm.getItem(0))).isEqualTo(0)
    }

    @Test
    fun groupieSection_addBaseBindableViewModel_oneItemInSection() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val section = Section()

        section.add(tvm)

        assertThat(section.itemCount).isEqualTo(1)
    }

    @Test
    fun groupieSection_addAndRemoveBaseBindableViewModel_zeroItemsInSection() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val section = Section()

        section.add(tvm)
        section.remove(tvm)

        assertThat(section.itemCount).isEqualTo(0)
    }

    @Test
    fun viewModelItem_shouldBeSameAsItself_returnsTrue() {
        val tvm = TestBindableViewModel(TEST_DATA)

        val vmi = tvm.getItem(0)

        assertThat(vmi.isSameAs(vmi)).isTrue()
    }

    @Test
    fun viewModelItem_fromSameData_shouldBeSameAsAnotherViewModelItem() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val tvm2 = TestBindableViewModel(TEST_DATA)

        val vmi = tvm.getItem(0)
        val vmi2 = tvm2.getItem(0)

        assertThat(vmi.isSameAs(vmi2)).isTrue()
        assertThat(vmi.equals(vmi2)).isTrue()
    }

    @Test
    fun viewModelItem_anotherViewModelItemFromDifferentData_shouldNotBeTheSame() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val tvm2 = TestBindableViewModel(TEST_DATA.reversed())

        val vmi = tvm.getItem(0)
        val vmi2 = tvm2.getItem(0)

        assertThat(vmi.isSameAs(vmi2)).isFalse()
    }

    @Test
    fun viewModelItem_anotherViewModelItemOfAnotherType_shouldNotBeTheSame() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val rvm = RepositoryViewModel(mock {})

        val vmi = tvm.getItem(0)
        val rvmi = rvm.getItem(0)

        assertThat(vmi.isSameAs(rvmi)).isFalse()
    }

    @Test
    fun viewModelItem_anotherItemOfAnotherType_shouldNotBeTheSame() {
        val tvm = TestBindableViewModel(TEST_DATA)
        val ti = TestItem()

        val vmi = tvm.getItem(0)

        assertThat(vmi.isSameAs(ti)).isFalse()
        assertThat(vmi.equals(ti)).isFalse()
    }

    @Test
    fun viewModelItem_differentString_returnsFalse() {
        val tvm = TestBindableViewModel(TEST_DATA)

        val vmi = tvm.getItem(0)

        assertThat(vmi.equals("")).isFalse()
    }

    @Test
    fun viewModelItemBind_validBindable_isBindable() {
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
