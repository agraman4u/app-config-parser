package org.agraman4u.appconfig.parser

import org.agraman4u.appconfig.AppConfig
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AppConfigParserTest {
    @Test
    fun loadConfigFiles() {
        AppConfig.initAppConfig("AppConfigTest", "prod", "EastUs1")
        assertEquals("hello world prod.EastUs1", AppConfig.get<String>("databaseDriverConfig", "name"))


        AppConfig.initAppConfig("AppConfigTest", "prod", "WestUs2")
        assertEquals("hello world prod west us", AppConfig.get<String>("databaseDriverConfig", "name"))

        AppConfig.initAppConfig("AppConfigTest", "beta", "WestUs2")
        assertEquals("hello world unk westus2", AppConfig.get<String>("databaseDriverConfig", "name"))

        AppConfig.initAppConfig("AppConfigTest", "beta", "unk")
        assertEquals("hello world", AppConfig.get<String>("databaseDriverConfig", "name"))
        assertEquals(40, AppConfig.get<Int>("databaseDriverConfig", "id"))
    }
}