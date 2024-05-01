plugins {
    alias(libs.plugins.ksp)
    application
}


sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

dependencies {

    implementation(platform(rootProject.libs.koin.bom))
    implementation(platform(rootProject.libs.koin.annotations.bom))
    implementation(rootProject.libs.koin.core)
    implementation(rootProject.libs.koin.annotations)

    implementation(rootProject.libs.logback)
    implementation(rootProject.libs.kotlin.coroutines)

    ksp(rootProject.libs.koin.ksp.compiler)

    testImplementation(rootProject.libs.junit)
    testImplementation(rootProject.libs.jUnitEngine)

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}


