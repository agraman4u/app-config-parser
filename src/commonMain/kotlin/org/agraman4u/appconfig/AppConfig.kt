package org.agraman4u.appconfig

import org.agraman4u.appconfig.parser.AppConfigParser
import org.agraman4u.appconfig.parser.Parser
import org.agraman4u.appconfig.utils.AppConfigurationArgs
import org.agraman4u.appconfig.utils.getRegion
import org.agraman4u.appconfig.utils.getStage

class AppConfig private constructor(args: AppConfigurationArgs) {
    val parser: Parser = AppConfigParser(args)

    companion object {
        lateinit var appConfig: AppConfig

        fun initAppConfig(
            serviceName: String,
            stage: String = getStage(),
            region: String = getRegion(),
            configDir: String? = null
        ) {
            appConfig = AppConfig(AppConfigurationArgs(stage, region, serviceName, configDir))
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

