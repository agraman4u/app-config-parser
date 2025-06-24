package org.agraman4u.appconfig.utils

import okio.Path
import okio.FileSystem
import java.io.File


actual fun getProperty(key: String): String {
    return System.getProperty(key)
}


actual fun listFiles(path: Path): List<Path> {
    return FileSystem.SYSTEM.list(path)
}

actual fun readFile(path: Path): List<String> {
    return FileSystem.SYSTEM.read(path, { readUtf8().lines() })
}
