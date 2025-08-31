package br.com.whatadice.util

import java.util.Locale

private val SPACE_REGEX = Regex("[\\s\\u00A0]+")

fun normalizeEmail(raw: String): String =
    raw.replace(SPACE_REGEX, "")
        .trim()
        .lowercase(Locale.ROOT)
