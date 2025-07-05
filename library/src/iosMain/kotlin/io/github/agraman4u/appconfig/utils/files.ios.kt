package io.github.agraman4u.appconfig.utils

import okio.Path
import okio.FileSystem

internal actual fun listFiles(path: Path): List<Path> {
    return FileSystem.SYSTEM.list(path)
}

actual fun readFile(path: Path): List<String> {
    return FileSystem.SYSTEM.read(path, { readUtf8().lines() })
}