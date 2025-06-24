package org.agraman4u.appconfig.utils

import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.FileSystem
import platform.posix.getenv
import kotlinx.cinterop.toKString


@OptIn(ExperimentalForeignApi::class)
actual fun getProperty(key: String): String {
    return getenv(key)?.toKString()!!
}


actual fun listFiles(path: Path): List<Path> {
    return FileSystem.SYSTEM.list(path)
}

actual fun readFile(path: Path): List<String> {
    return FileSystem.SYSTEM.read(path, { readUtf8().lines() })
}
