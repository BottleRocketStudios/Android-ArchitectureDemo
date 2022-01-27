package com.bottlerocketstudios.brarchitecture.ui.util

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.bottlerocketstudios.brarchitecture.data.model.DomainModel
import java.io.Serializable

/**
 * Union type that represents either the int ID, the format string, or the raw String.
 *
 * Calling getString resolves to a String from any subtype.
 */
sealed class StringIdHelper : DomainModel, Serializable {
    data class Id(@StringRes val idRes: Int) : StringIdHelper()

    data class Raw(val rawString: String) : StringIdHelper()

    data class Format(@StringRes val idRes: Int, val formatArgs: List<Any>) : StringIdHelper()

    data class Plural(@PluralsRes val idRes: Int, val quantity: Int, val formatArgs: List<Any>) : StringIdHelper()

    fun getString(context: Context): String {
        return when (this) {
            is Id -> {
                context.getString(idRes)
            }
            is Raw -> {
                rawString
            }
            is Format -> {
                val mappedArgs = formatArgs.map {
                    // Allow for the use of string helpers within format args by unwrapping them here
                    if (it is StringIdHelper) {
                        it.getString(context)
                    } else {
                        it
                    }
                }
                @Suppress("SpreadOperator")
                context.getString(idRes, *mappedArgs.toTypedArray())
            }
            is Plural -> {
                val mappedArgs = formatArgs.map {
                    // Allow for the use of string helpers within format args by unwrapping them here
                    if (it is StringIdHelper) {
                        it.getString(context)
                    } else {
                        it
                    }
                }
                @Suppress("SpreadOperator")
                context.resources.getQuantityString(idRes, quantity, *mappedArgs.toTypedArray())
            }
        }
    }

    companion object {
        const val serialVersionUID = 1L
    }
}

fun String.toStringIdHelper() = StringIdHelper.Raw(
    rawString = this
)
