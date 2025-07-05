package io.github.agraman4u.appconfig.utils

internal actual fun getProperty(key: String): String {
    return System.getProperty(key)
}