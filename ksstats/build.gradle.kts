import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeDesktop)

}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)

//            implementation(platform(rootProject.libs.koin.bom))
//            implementation(platform(rootProject.libs.koin.annotations.bom))
            implementation(rootProject.libs.koin.core)
            implementation(rootProject.libs.koin.compose)
            implementation(rootProject.libs.koin.compose.jvm)
            implementation(rootProject.libs.koin.annotations)
            implementation(rootProject.libs.jooq)
            implementation(rootProject.libs.jooq.meta)
            implementation(rootProject.libs.jooq.codeGen)
            implementation(rootProject.libs.jetbtains.compose.navigation)

            runtimeOnly(rootProject.libs.kotlin.coroutines.swing)
        }
    }
}


group = "com.knowledgespike"
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
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "distance"
            packageVersion = "1.0.0"
        }
    }
}
