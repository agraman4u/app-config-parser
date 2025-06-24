plugins {
    kotlin("multiplatform") version "1.9.0"
    `maven-publish`
}

val PROJECT_GROUP = "org.agraman4u.appconfig"
val PROJECT_VERSION = "1.0"

group = PROJECT_GROUP
version = PROJECT_VERSION

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(8)
        withJava()
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }
    js {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
        hostOs == "Linux" && isArm64 -> linuxArm64("native")
        hostOs == "Linux" && !isArm64 -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


    sourceSets {
        val okioVersion = "3.12.0"

        val commonMain by getting {
            java {

            }
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.2")
                implementation("com.squareup.okio:okio:$okioVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("com.squareup.okio:okio-fakefilesystem:$okioVersion")
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("com.squareup.okio:okio-nodefilesystem:$okioVersion")
            }
        }
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}

tasks.register<Copy>("copy-configuration") {
    println("Copying config files")
    from(layout.projectDirectory.dir("configuration"))
    into(layout.buildDirectory.dir("configuration"))
    mustRunAfter("compileJava")
}