import org.jetbrains.compose.desktop.application.dsl.TargetFormat


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeDesktop)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jooq)
    alias(libs.plugins.kotlinSerialization)
}


kotlin {
    jvm("desktop")

    sourceSets {

        all {
            languageSettings {
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
                optIn("androidx.compose.foundation.ExperimentalFoundationApi")
            }
        }


        val genDir = layout.buildDirectory.dir("./generated/jooq/kotlin")
//        val generatedDir = layout.projectDirectory.dir("generated/kotlin")
//
//
        val desktopMain by getting
        commonMain.configure {
            sourceSets {
                kotlin.srcDir(genDir)
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(compose.material3)
            implementation(compose.materialIconsExtended)

//            implementation(platform(libs.koin.bom))
//            implementation(platform(libs.koin.annotations.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.jvm)
            implementation(libs.koin.annotations)
            implementation(libs.jetbtains.compose.navigation)
            implementation(libs.sqlite)
            implementation(libs.jooq)
            implementation(libs.jooq.meta)
            implementation(libs.jooq.codeGen)
//            implementation(libs.logback)
            implementation(libs.log4j.core)
            implementation(libs.log4j.api)
            implementation(libs.log4j.sfl4j)

            implementation(libs.kotlinx.datetime)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.sqlite)

            runtimeOnly(libs.kotlin.coroutines.swing)
        }
    }

}


group = "com.ksstats"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

compose.desktop {
    application {

        mainClass = "com.ksstats.app.MainKt"

        nativeDistributions {
//            modules("java.sql", "java.management")
            includeAllModules = true
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Pkg)
            packageName = "ksstats"
            packageVersion = "1.0.0"
        }

        buildTypes.release.proguard {
            obfuscate.set(false)
            isEnabled.set(true)
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
    }
}



jooq {
    val output: Provider<Directory> = layout.buildDirectory.dir(".")
    val dir = "${layout.projectDirectory}/../"
    val sqliteUrl = "jdbc:sqlite:${dir}/database/cricket.sqlite"
    configuration {
        basedir = "${output.get()}"
        jdbc {
            driver = "org.sqlite.JDBC"
            url = sqliteUrl
            user = ""
            password = ""
        }
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                name = "org.jooq.meta.sqlite.SQLiteDatabase"
            }

            target {
                packageName = "com.ksstats.db"
                directory = "generated/jooq/kotlin"
                isClean = true
            }
        }
    }
}

