plugins {
    id("gradle.spring")
    id("gradle.kotlin")
    id("gradle.project")
    id("gradle.test")
}

dependencies {
    runtimeOnly("com.h2database:h2")
}
