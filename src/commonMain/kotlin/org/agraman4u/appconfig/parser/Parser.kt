package org.agraman4u.appconfig.parser

import org.agraman4u.appconfig.utils.AppConfigMap


interface Parser {
    fun parse(): AppConfigMap
    fun getValue(identifier: String, key: String): String?
    fun getAllEntries(identifier: String): Map<String, String>?
}

