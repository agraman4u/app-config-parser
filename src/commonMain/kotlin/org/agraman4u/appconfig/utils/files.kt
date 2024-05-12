package org.agraman4u.appconfig.utils

import kotlinx.io.files.Path

internal expect fun listFiles(path: Path): List<Path>
internal expect fun readConfigFile(path: Path): List<String>