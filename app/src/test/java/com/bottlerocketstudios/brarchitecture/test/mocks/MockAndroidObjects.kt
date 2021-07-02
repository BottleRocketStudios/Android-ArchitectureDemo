package com.bottlerocketstudios.brarchitecture.test.mocks

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.bottlerocketstudios.brarchitecture.R
import org.mockito.ArgumentMatchers
import org.mockito.kotlin.any
import org.mockito.kotlin.anyVararg
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock

val testContext: Context = mock {
    // Order is important here, most generic match first. Last match is final value.
    on { getString(any()) } doReturn ""
    on { getDrawable(any()) } doReturn null
    on { getString(any(), anyVararg()) } doReturn ""
    on { resources } doReturn testResources

    // Add specific mocks for getString/getQuantity/etc string/plural/etc references and mocked values here
    on { getString(R.string.today) } doReturn "Today"
}

val testResources: Resources = mock {
    // Order is important here, most generic match first. Last match is final value.
    on { getQuantityString(any(), any(), any()) } doReturn ""
    on { getDrawable(any()) } doReturn null
    on { getDrawable(any(), any()) } doReturn null
    on { getString(any(), anyVararg()) } doReturn ""
    // Add specific mocks for getString/getQuantity/etc string/plural/etc references and mocked values here

    on { getQuantityString(eq(R.plurals.days_ago_plural), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()) } doAnswer {
        if (it.getArgument<Int>(1) == 1) {
            "${it.getArgument<Int>(1)} day ago"
        } else {
            "${it.getArgument<Int>(2)} days ago"
        }
    }
}

fun testApplicationFactory(
    mockContext: Context = testContext
): Application = mock {
    on { applicationContext } doReturn mockContext
    on { resources } doReturn testResources
    // Order is important here, most generic match first. Last match is final value.
    on { getDrawable(any()) } doReturn null
    on { getString(any()) } doReturn ""
    on { getString(any(), anyVararg()) } doReturn ""
    // Add specific mocks for getString/getQuantity/etc string/plural/etc references and mocked values here
}
