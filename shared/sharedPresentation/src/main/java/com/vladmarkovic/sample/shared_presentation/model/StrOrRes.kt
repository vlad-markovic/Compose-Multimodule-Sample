package com.vladmarkovic.sample.shared_presentation.model

import android.content.Context
import androidx.annotation.StringRes
import java.io.Serializable

@JvmInline
value class StrOrRes private constructor(
    private val value: Any?
) : Serializable {

    companion object {
        fun str(value: String): StrOrRes = StrOrRes(value)
        fun res(@StringRes value: Int): StrOrRes = StrOrRes(value)
    }

    fun get(context: Context): String = value as? String ?: context.getString(value as Int)

    override fun toString(): String = if (value is String) value else "Resource($value)"
}
