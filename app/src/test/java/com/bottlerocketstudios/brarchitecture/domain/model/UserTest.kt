package com.bottlerocketstudios.brarchitecture.domain.model

import com.bottlerocketstudios.brarchitecture.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UserTest : BaseTest() {
    @Test
    fun user_shouldHaveFields_whenConstructed() {
        val user = User("username", "nickname", "account_status", "display_name", "created_on", "uuid")
        assertThat(user.username).isEqualTo("username")
        assertThat(user.nickname).isEqualTo("nickname")
        assertThat(user.accountStatus).isEqualTo("account_status")
        assertThat(user.displayName).isEqualTo("display_name")
        assertThat(user.createdOn).isEqualTo("created_on")
        assertThat(user.uuid).isEqualTo("uuid")
    }
}
