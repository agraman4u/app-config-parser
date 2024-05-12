package org.agraman4u.appconfig.parser

import org.agraman4u.appconfig.utils.AppConfigMap
import org.agraman4u.appconfig.utils.AppConfigurationArgs
import org.agraman4u.appconfig.utils.ConfigEntry
import org.agraman4u.appconfig.utils.getProperty
import org.agraman4u.appconfig.utils.isWildCard
import org.agraman4u.appconfig.utils.isWildCardOrVal
import org.agraman4u.appconfig.utils.listFiles
import org.agraman4u.appconfig.utils.readConfigFile
import kotlinx.io.files.Path


internal class AppConfigParser(private val configurationArgs: AppConfigurationArgs) : Parser {
    companion object {
        private val ROOT_CONFIG_DIR = Path(getProperty("user.dir"), "configuration", "app-config")
        private val CONFIG_ENTRY_PROPERTY_REGEX = """\s*\"(\w+)\"\s*:\s*\"([^\"]+)\"\s*""".toRegex()
        private val CONFIG_REGEX = """^(\w+|\*)\.(\w+|\*)\.(\w+|\*)\s*\+=\s*\{(.+)}$""".toRegex()
    }

    private val values: AppConfigMap
    private val configFiles: List<Path>

    init {
        val matcher = Regex(".*\\.${configurationArgs.serviceName}\\.conf")
        val filteredFiles = listFiles(ROOT_CONFIG_DIR).filter { matcher.matches(it.name) }
        configFiles = filteredFiles
        values = parse()
    }

    override fun parse(): AppConfigMap {
        val allFileMappings = configFiles.map { parseFile(it) }.flatten().filter {
            it.stage.isWildCardOrVal(configurationArgs.stage)
                    && it.region.isWildCardOrVal(configurationArgs.region)

        }
        return buildAppConfig(allFileMappings)
    }

    override fun getValue(identifier: String, key: String): String? {
        return this.values[identifier]?.get(key)
    }

    override fun getAllEntries(identifier: String): Map<String, String>? {
        return this.values[identifier]
    }

    private fun parseFile(path: Path): List<ConfigEntry> {
        val configFile = readConfigFile(path)

        val entries = mutableListOf<ConfigEntry>()

        for (line in configFile) {
            val matchResult = CONFIG_REGEX.find(line)
            if (matchResult != null) {
                val (stage, region, identifier, properties) = matchResult.destructured
                entries.add(ConfigEntry(stage, region, identifier, properties))
            }
        }
        return entries
    }

    private fun buildAppConfig(entries: List<ConfigEntry>): AppConfigMap {
        val generics = entries.filter { it.stage.isWildCard() && it.region.isWildCard() }
        val appConfigMap: AppConfigMap = mutableMapOf()

        fun updateAppConfig(entry: ConfigEntry) {
            val (identifier, mappings) = entry.getIdentifierMap()
            appConfigMap.update(identifier, mappings)
        }

        generics.forEach { updateAppConfig(it) }

        val specificEntries =
            entries.filter {
                !(it.stage.isWildCard() && it.region.isWildCard())
                        && (it.stage.isWildCard() || it.region.isWildCard())
            }

        specificEntries.forEach { updateAppConfig(it) }

        entries.filter { !it.stage.isWildCard() && !it.region.isWildCard() }.forEach { updateAppConfig(it) }

        return appConfigMap
    }

    private fun ConfigEntry.getIdentifierMap(): Pair<String, Map<String, String>> {
        val propertiesMap = mutableMapOf<String, String>()

        CONFIG_ENTRY_PROPERTY_REGEX.findAll(mappings).forEach { matchResult ->
            val (key, value) = matchResult.destructured
            propertiesMap[key] = value
        }

        return Pair(identifier, propertiesMap)
    }

    private fun AppConfigMap.update(identifier: String, mappings: Map<String, String>) {
        if (!this.containsKey(identifier)) {
            this[identifier] = mutableMapOf()
        }
        mappings.forEach {
            this[identifier]!![it.key] = it.value
        }
    }
}