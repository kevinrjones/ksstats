plugins {
    alias(libs.plugins.ksp)
    application
}


sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

dependencies {

    implementation(platform(libs.koin.bom))
    implementation(platform(libs.koin.annotations.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)

//    implementation(libs.logback)
    implementation(libs.log4j.core)
    implementation(libs.log4j.api)
    implementation(libs.log4j.sfl4j)

    implementation(libs.kotlin.coroutines)

    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.jUnitEngine)

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}


