package io.github.agraman4u.appconfig.utils

internal data class AppConfigurationArgs(
    val readDir: String,
    val serviceName: String,
    val stage: String,
    val region: String
)

internal data class ConfigEntry(
    val stage: String,
    val region: String,
    val identifier: String,
    val mappings: String
)

internal typealias AppConfigMap = MutableMap<String, MutableMap<String, String>>
