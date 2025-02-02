plugins {
    alias(libs.plugins.jvm.library)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.spring.test)
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
