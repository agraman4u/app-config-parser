package org.agraman4u.appconfig.utils

import kotlinx.io.files.Path
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths


actual fun getProperty(): SystemProperties {
    return System.getProperties().map { Pair(it.key as String, it.value as String) }.toMap()
}

actual fun getProperty(key: String): String {
    return System.getProperty(key)
}


actual fun listFiles(path: Path): List<Path> {
    return File(path.toString()).walk().filter { it.isFile }.map {
        Path(it.absoluteFile.toString())
    }.toList()
}

actual fun readConfigFile(path: Path): List<String> {
    val configLines: MutableList<String> = ArrayList()

    try {
        val lines = Files.readAllLines(Paths.get(path.toString()), StandardCharsets.UTF_8)
        val configBuilder = StringBuilder()
        var insideConfig = false
        for (line in lines) {
            if (line.matches("""^\s*[a-zA-Z0-9*]+\.[a-zA-Z0-9*]+\.[a-zA-Z0-9*]+\s*\+\=\s*\{\s*""".toRegex())) {
                insideConfig = true
                configBuilder.append(line.trim { it <= ' ' })
            } else if (insideConfig) {
                configBuilder.append(line.trim { it <= ' ' })
                if (line.contains("}")) {
                    insideConfig = false
                    configLines.add(configBuilder.toString())
                    configBuilder.setLength(0)
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return configLines.toList()
}
