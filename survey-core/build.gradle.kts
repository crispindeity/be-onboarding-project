plugins {
    id("gradle.kotlin")
    id("gradle.spring")
    id("gradle.project")
    id("gradle.test")
    id("kotlinter")
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
