rootProject.name = "KSStats"

include("shared")
include("ksstats")


pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

//pluginManagement {
//    repositories {
//        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
//        google()
//        gradlePluginPortal()
//        mavenCentral()
//    }
//
//    plugins {
//        kotlin("jvm").version(extra["kotlin.version"] as String)
//        id("org.jetbrains.compose").version(extra["compose.version"] as String)
//    }
//}

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.gradle.toolchains:foojay-resolver:0.7.0")
    }
}

apply(plugin = "org.gradle.toolchains.foojay-resolver-convention")


//toolchainManagement {
//    jvm {
//        javaRepositories {
//            repository("foojay") {
//                resolverClass.set(org.gradle.toolchains.foojay.FoojayToolchainResolver::class.java)
//            }
//        }
//    }
//}

