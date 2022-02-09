package com.bottlerocketstudios.brarchitecture.ui.repository

import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.data.model.Repository
import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.bottlerocketstudios.brarchitecture.test.KoinTestRule
import com.bottlerocketstudios.brarchitecture.test.TestModule
import com.bottlerocketstudios.brarchitecture.ui.ViewModelItem
import com.bottlerocketstudios.brarchitecture.ui.util.StringIdHelper
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class RepositoryViewModelTest : BaseTest() {

    @get:Rule
    val koinRule = KoinTestRule(TestModule.generateMockedTestModule())

    @Test
    fun repositoryViewModel_initialized_configuredAsExpected() {
        inlineKoinSingle { Clock.fixed(Instant.parse("2022-01-26T13:00:00.00Z"), ZoneId.of("UTC")) } // example of using inlineKoinSingle to override Clock in koin module graph
        val rvm = RepositoryViewModel(Repository(updated = ZonedDateTime.now(Clock.fixed(Instant.parse("2022-01-26T08:00:00.00Z"), ZoneId.of("UTC")))))

        assertThat(rvm.repository).isNotNull()
        assertThat(rvm.getItem(0)).isInstanceOf(ViewModelItem::class.java)
        assertThat(rvm.formattedUpdateTime).isEqualTo(StringIdHelper.Id(R.string.today))
    }
}
