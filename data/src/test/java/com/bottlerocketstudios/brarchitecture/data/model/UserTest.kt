package com.bottlerocketstudios.brarchitecture.data.model

import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UserTest : BaseTest() {
    private val linksDto: LinksDto
        get() = LinksDto(avatar = LinkDto(href = "link_dto"))

    @Test
    fun user_shouldHaveFields_whenConstructed() {
        val user = UserDto(
            username = "username",
            nickname = "nickname",
            accountStatus = "account_status",
            displayName = "display_name",
            createdOn = "created_on",
            uuid = "uuid",
            linksDto = linksDto
        )
        assertThat(user.username).isEqualTo("username")
        assertThat(user.nickname).isEqualTo("nickname")
        assertThat(user.accountStatus).isEqualTo("account_status")
        assertThat(user.displayName).isEqualTo("display_name")
        assertThat(user.createdOn).isEqualTo("created_on")
        assertThat(user.uuid).isEqualTo("uuid")
        assertThat(user.linksDto?.avatar?.href).isEqualTo("link_dto")
    }

    @Test
    fun user_defaultFields_whenDefaultConstruction() {
        val user = UserDto()
        assertThat(user.username).isEqualTo("")
        assertThat(user.nickname).isEqualTo("")
        assertThat(user.accountStatus).isEqualTo("")
        assertThat(user.displayName).isEqualTo("")
        assertThat(user.createdOn).isEqualTo(null)
        assertThat(user.uuid).isEqualTo("")
        assertThat(user.linksDto).isEqualTo(null)
        assertThat(user.linksDto?.avatar).isEqualTo(null)
        assertThat(user.linksDto?.avatar?.href).isEqualTo(null)
    }

    @Test
    fun linksDTO_defaultFields_whenDefaultConstruction() {
        val linksDto = LinksDto()
        assertThat(linksDto.avatar).isEqualTo(null)
    }

    @Test
    fun linkDTO_defaultFields_whenDefaultConstruction() {
        val linkDto = LinkDto()
        assertThat(linkDto.href).isEqualTo(null)
    }
}
