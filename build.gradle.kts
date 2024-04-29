import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask


plugins {

    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.composeDesktop) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.versionUpdate)
    alias(libs.plugins.catalogUpdate)
    alias(libs.plugins.ksp) apply false
//    alias(libs.plugins.jooq) apply false
}

subprojects {
    group = "com.knowledgespike"
    version = "0.1.0"

    apply(plugin = "kotlinx-serialization")


}

project(":shared") {
    dependencies {


    }
}

project(":ksstats") {


    dependencies {
    }
}



fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}