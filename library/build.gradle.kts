import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.agraman4u"
version = "1.0.0"

kotlin {
    jvm()
    js {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        val commonMain by getting {
            resources.srcDir("configuration")
            dependencies {
                //put your multiplatform dependencies here
                implementation(libs.kotlinx.io.core)
                implementation(libs.okio)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)

            }
        }
        val jsMain by getting
        val jsTest by getting
    }
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("/Volumes/workplace/app-config-parser/key.gpg")
        }
    }
    namespace = "io.github.agraman4u.appconfig"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "app-config-parser", version.toString())

    pom {
        name = "App Config Parser"
        description = "A library to dynamically parse multi stage and multi region level configuration mappings"
        inceptionYear = "2025"
        url = "https://github.com/agraman4u/app-config-parser"
        licenses {
            license {
                name = "The MIT License"
                url = "https://opensource.org/licenses/MIT"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "agraman4u"
                name = "Aman Agrawal"
                url = "https://github.com/agraman4u"
            }
        }
        scm {
            url = "https://github.com/agraman4u/app-config-parser"
            connection = "scm:git:https://github.com/agraman4u/app-config-parser.git"
            developerConnection = "scm:git:git@github.com:agraman4u/app-config-parser.git"
        }
    }
}
