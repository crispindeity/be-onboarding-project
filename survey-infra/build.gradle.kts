plugins {
    id("gradle.kotlin")
    id("gradle.spring")
    id("gradle.project")
    id("gradle.test")
    id("kotlinter")
}

dependencies {
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}

