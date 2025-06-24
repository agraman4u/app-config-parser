package org.agraman4u.appconfig.utils

import okio.Path
import okio.NodeJsFileSystem

import okio.buffer
import okio.use

internal actual fun listFiles(path: Path): List<Path> {
    return NodeJsFileSystem.list(path)
}

internal actual fun readFile(path: Path): List<String> {
    return NodeJsFileSystem.source(path).buffer().use { it.readUtf8().lines() }
}