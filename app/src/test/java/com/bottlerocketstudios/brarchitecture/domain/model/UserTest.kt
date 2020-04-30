package com.bottlerocketstudios.brarchitecture.domain.model

import com.bottlerocketstudios.brarchitecture.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UserTest : BaseTest() {
    @Test
    fun user_shouldHaveFields_whenConstructed() {
        val user = User("username", "nickname", "account_status", "display_name", "created_on", "uuid")
        assertThat(user.username).isEqualTo("username")
        assertThat(user.nickname).isEqualTo("nickname")
        assertThat(user.account_status).isEqualTo("account_status")
        assertThat(user.display_name).isEqualTo("display_name")
        assertThat(user.created_on).isEqualTo("created_on")
        assertThat(user.uuid).isEqualTo("uuid")
    }
}
