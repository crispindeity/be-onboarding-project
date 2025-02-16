plugins {
    alias(libs.plugins.jvm.library)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.spring.test)
    alias(libs.plugins.spring.web)
    alias(libs.plugins.kotlin.serializer)
}

dependencies {
    implementation(project(":survey-core"))
}
