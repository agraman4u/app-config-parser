package io.github.agraman4u.appconfig.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
internal actual fun getProperty(key: String): String {
    return getenv(key)?.toKString()!!
}