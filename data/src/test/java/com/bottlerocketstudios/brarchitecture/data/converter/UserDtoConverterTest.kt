package com.bottlerocketstudios.brarchitecture.data.converter

import com.bottlerocketstudios.brarchitecture.data.model.LinkDto
import com.bottlerocketstudios.brarchitecture.data.model.LinksDto
import com.bottlerocketstudios.brarchitecture.data.model.UserDto
import com.bottlerocketstudios.brarchitecture.data.test.BaseTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UserDtoConverterTest : BaseTest() {
    private val linksDto = LinksDto(LinkDto("link_dto"))
    @Test
    fun user_shouldCreateUser_whenConvertToUser() {
        val userDto = UserDto("username", "nickname", "account_status", "display_name", "created_on", "uuid", linksDto)
        val user = userDto.convertToUser()
        assertThat(user.username).isEqualTo(userDto.username)
        assertThat(user.nickname).isEqualTo(userDto.nickname)
        assertThat(user.accountStatus).isEqualTo(userDto.accountStatus)
        assertThat(user.displayName).isEqualTo(userDto.displayName)
        assertThat(user.createdOn).isEqualTo(userDto.createdOn)
        assertThat(user.uuid).isEqualTo(userDto.uuid)
        assertThat(user.links).isEqualTo(userDto.linksDto?.convertToLinks())
        assertThat(user.avatarUrl).isEqualTo(userDto.linksDto?.convertToLinks()?.avatar?.href)
    }

    @Test
    fun user_defaultFields_onDefaultConstruction() {
        val userDto = UserDto()
        val user = userDto.convertToUser()
        assertThat(user.username).isEqualTo(userDto.username)
        assertThat(user.nickname).isEqualTo(userDto.nickname)
        assertThat(user.accountStatus).isEqualTo(userDto.accountStatus)
        assertThat(user.displayName).isEqualTo(userDto.displayName)
        assertThat(user.createdOn).isEqualTo(userDto.createdOn)
        assertThat(user.uuid).isEqualTo(userDto.uuid)
        assertThat(user.links).isEqualTo(userDto.linksDto?.convertToLinks())
        assertThat(user.avatarUrl).isEqualTo(userDto.linksDto?.convertToLinks()?.avatar?.href)
    }

    @Test
    fun user_linkDefault_returnLinkDefault() {
        val userDto = UserDto(linksDto = LinksDto(LinkDto()))
        val user = userDto.convertToUser()
        assertThat(user.avatarUrl).isEqualTo(userDto.linksDto?.convertToLinks()?.avatar?.href)
    }
}
