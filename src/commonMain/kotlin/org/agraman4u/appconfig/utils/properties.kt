package org.agraman4u.appconfig.utils

private val DEPLOYMENT_STAGE_KEY = "DEPLOYMENT_STAGE"
private val DEPLOYMENT_REGION_KEY = "DEPLOYMENT_REGION"

internal expect fun getProperty(key: String): String

internal fun getStage(): String {
    return getProperty(DEPLOYMENT_STAGE_KEY)
}

internal fun getRegion(): String {
    return getProperty(DEPLOYMENT_REGION_KEY)
}