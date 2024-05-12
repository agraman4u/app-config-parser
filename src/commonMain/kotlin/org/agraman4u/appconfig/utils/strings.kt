package org.agraman4u.appconfig.utils

internal fun String.isWildCardOrVal(value: String): Boolean {
    return listOf("*", value).contains(this)
}

internal fun String.isWildCard(): Boolean {
    return this == "*"
}