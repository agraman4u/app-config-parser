package org.agraman4u.appconfig.utils

private val DEPLOYMENT_STAGE_KEY = "DEPLOYMENT_STAGE"
private val DEPLOYMENT_REGION_KEY = "DEPLOYMENT_REGION"
internal expect fun getProperty(): SystemProperties
internal expect fun getProperty(key: String): String

internal fun SystemProperties.getStage(): String {
    return this[DEPLOYMENT_STAGE_KEY]!!
}

internal fun SystemProperties.getRegion(): String {
    return this[DEPLOYMENT_REGION_KEY]!!
}