plugins {
    alias(libs.plugins.jvm.library)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.spring.test)
}

dependencies {
    implementation(project(":survey-infra"))
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
