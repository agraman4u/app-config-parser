package io.github.agraman4u.appconfig

import io.github.agraman4u.appconfig.parser.AppConfigParser
import io.github.agraman4u.appconfig.parser.impl.AppConfigParserImpl
import io.github.agraman4u.appconfig.utils.AppConfigurationArgs
import io.github.agraman4u.appconfig.utils.ConfigKeys

class AppConfig private constructor(args: AppConfigurationArgs) {
    val parser: AppConfigParser = AppConfigParserImpl(args)

    companion object {
        lateinit var appConfig: AppConfig

        fun initAppConfig(
            serviceName: String,
            configDir: String,
            stage: String = ConfigKeys.getStage(),
            region: String = ConfigKeys.getRegion(),
        ) {
            appConfig = AppConfig(
                AppConfigurationArgs(
                    stage = stage,
                    region = region,
                    serviceName = serviceName,
                    readDir = configDir
                )
            )
        }

        inline fun <reified T> get(identifier: String, key: String): T {
            val value = appConfig.parser.getValue(identifier, key) as String
            return when (T::class) {
                Int::class -> value.toInt() as T
                Long::class -> value.toLong() as T
                Float::class -> value.toFloat() as T
                Double::class -> value.toDouble() as T
                String::class -> value as T
                else -> throw IllegalStateException("Type ${T::class.simpleName} not supported to be parse")
            }
        }

        fun getAllEntries(identifier: String): Map<String, String> {
            return appConfig.parser.getAllEntries(identifier) ?: emptyMap()
        }
    }
}