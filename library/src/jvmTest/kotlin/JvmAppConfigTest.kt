package io.github.agraman4u.appconfig

import kotlin.test.Test
import kotlin.test.assertEquals

class AppConfigParserTest {
    @Test
    fun loadConfigFiles() {
        AppConfig.initAppConfig("AppConfigTest", "configuration", "prod", "EastUs1")
        assertEquals(
            "hello world prod.EastUs1",
            AppConfig.get<String>("databaseDriverConfig", "name")
        )

        AppConfig.initAppConfig("AppConfigTest", "configuration", "prod", "WestUs2")
        assertEquals(
            "hello world prod west us",
            AppConfig.get<String>("databaseDriverConfig", "name")
        )

        AppConfig.initAppConfig("AppConfigTest", "configuration", "beta", "WestUs2")
        assertEquals(
            "hello world unk westus2",
            AppConfig.get<String>("databaseDriverConfig", "name")
        )

        AppConfig.initAppConfig("AppConfigTest", "configuration", "beta", "unk")
        assertEquals("hello world", AppConfig.get<String>("databaseDriverConfig", "name"))
        assertEquals(40, AppConfig.get<Int>("databaseDriverConfig", "id"))
    }
}