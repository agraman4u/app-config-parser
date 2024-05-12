package org.agraman4u.appconfig.utils

internal typealias SystemProperties = Map<String, String>

internal data class AppConfigurationArgs(val stage: String, val region: String, val serviceName: String)

internal data class ConfigEntry(val stage: String, val region: String, val identifier: String, val mappings: String)

internal typealias AppConfigMap = MutableMap<String, MutableMap<String, String>>