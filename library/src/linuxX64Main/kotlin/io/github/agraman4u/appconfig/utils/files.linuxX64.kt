package io.github.agraman4u.appconfig.utils

import okio.FileSystem
import okio.Path

internal actual fun listFiles(path: Path): List<Path> {
    return FileSystem.SYSTEM.list(path)
}

internal actual fun readFile(path: Path): List<String> {
    return FileSystem.SYSTEM.read(path, { readUtf8().lines() })
}