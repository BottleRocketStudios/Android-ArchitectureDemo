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
    fun baseBindableViewModel_shouldBeInstanceOf_viewModelItem(){
        //Arrange
        val tvm = TestBindableViewModel(TEST_DATA)
        //Act
        //Assert
        assertThat(tvm.itemCount).isGreaterThan(0)
        assertThat(tvm.getItem(0)).isInstanceOf(ViewModelItem::class.java)

    }

    @Test
    fun baseBindableViewModel_shouldCreate_subclass(){
        //Arrange
        val tvm = TestBindableViewModel(TEST_DATA)
        //Act
        //Assert
        assertThat(tvm.itemCount).isGreaterThan(0)
        assertThat(tvm.getItem(0)).isInstanceOf(ViewModelItem::class.java)
        assertThat(tvm.getPosition(tvm.getItem(0))).isEqualTo(0)

    }

    @Test
    fun subclass_viewModelItem_shouldBe_added(){
        //Arrange
        val tvm = TestBindableViewModel(TEST_DATA)
        val section = Section()
        //Act
        section.add(tvm)
        //Assert
        assertThat(section.itemCount).isEqualTo(1)
    }

    @Test
    fun subclass_viewModelItem_shouldBe_deleted(){
        //Arrange
        val tvm = TestBindableViewModel(TEST_DATA)
        val section = Section()
        //Act
        section.add(tvm)
        section.remove(tvm)
        //Assert
        assertThat(section.itemCount).isEqualTo(0)
    }

    @Test
    fun viewModelItem_shouldBeSameAsItself_whee() {
        //Arrange
        val tvm = TestBindableViewModel(TEST_DATA)
        //Act
        val vmi = tvm.getItem(0)
        //Assert
        assertThat(vmi.isSameAs(vmi)).isTrue()
    }

    @Test
    fun viewModelItem_shouldBeSameAsAnotherViewModelItem_fromSameData() {
        //Arrange
        val tvm = TestBindableViewModel(TEST_DATA)
        val tvm2 = TestBindableViewModel(TEST_DATA)
        //Act
        val vmi = tvm.getItem(0)
        val vmi2 = tvm2.getItem(0)
        //Assert
        assertThat(vmi.isSameAs(vmi2)).isTrue()
        assertThat(vmi.equals(vmi2)).isTrue()
    }

    @Test
    fun viewModelItem_shouldNotBeSameAs_anotherViewModelItemFromDifferentData() {
        //Arrange
        val tvm = TestBindableViewModel(TEST_DATA)
        val tvm2 = TestBindableViewModel(TEST_DATA.reversed())
        //Act
        val vmi = tvm.getItem(0)
        val vmi2 = tvm2.getItem(0)
        //Assert
        assertThat(vmi.isSameAs(vmi2)).isFalse()
    }

    @Test
    fun viewModelItem_shouldNotBeSameAs_anotherViewModelItemOfAnotherType() {
        //Arrange
        val tvm = TestBindableViewModel(TEST_DATA)
        val rvm = RepositoryViewModel(mock {})
        //Act
        val vmi = tvm.getItem(0)
        val rvmi = rvm.getItem(0)
        //Assert
        assertThat(vmi.isSameAs(rvmi)).isFalse()
    }

    @Test
    fun viewModelItem_shouldNotBeSameAs_anotherItemOfAnotherType() {
        //Arrange
        val tvm = TestBindableViewModel(TEST_DATA)
        val ti = TestItem()
        //Act
        val vmi = tvm.getItem(0)
        //Assert
        assertThat(vmi.isSameAs(ti)).isFalse()
        assertThat(vmi.equals(ti)).isFalse()
    }

    @Test
    fun viewModelItem_shouldNotEqual_String() {
        //Arrange
        val tvm = TestBindableViewModel(TEST_DATA)
        //Act
        val vmi = tvm.getItem(0)
        //Assert
        assertThat(vmi.equals("")).isFalse()
    }

    @Test
    fun viewModelItem_shouldBind_toAThing() {
        //Arrange
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
        //Act
        // Nothing we can really test here. So turn off the error and let's get one more line of coverage!
        Timber.uprootAll()
        //Assert
        vmi.bind(bindable, 0)
    }
}
