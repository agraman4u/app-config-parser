package io.github.agraman4u.appconfig.utils

import okio.FileSystem
import okio.Path

actual fun listFiles(path: Path): List<Path> {
    println("Config dir: $path")
    return FileSystem.SYSTEM.list(path)
}

actual fun readFile(path: Path): List<String> {
    return FileSystem.SYSTEM.read(path, { readUtf8().lines() })
}