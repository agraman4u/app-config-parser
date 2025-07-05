package io.github.agraman4u.appconfig.parser

import io.github.agraman4u.appconfig.utils.AppConfigMap

interface AppConfigParser {
    fun parse(): AppConfigMap
    fun getValue(identifier: String, key: String): String?
    fun getAllEntries(identifier: String): Map<String, String>?
}