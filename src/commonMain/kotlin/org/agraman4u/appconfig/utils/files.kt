package org.agraman4u.appconfig.utils

import kotlinx.io.IOException
import okio.Path

internal expect fun listFiles(path: Path): List<Path>
internal expect fun readFile(path: Path): List<String>

fun readConfigFile(path: Path): List<String> {
    val configLines: MutableList<String> = ArrayList()
    try {
        val lines = readFile(path)
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
