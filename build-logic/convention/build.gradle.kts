plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
}

kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    plugins {
        register("jvmLibrary") {
            id = "jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("kotlinTest") {
            id = "kotlin.test"
            implementationClass = "KotlinTestLibraryConventionPlugin"
        }
        register("kotlinSpring") {
            id = "kotlin.spring"
            implementationClass = "KotlinSpringLibraryConventionPlugin"
        }
        register("kotlinSpringTest") {
            id = "kotlin.spring.test"
            implementationClass = "KotlinSpringTestLibraryConventionPlugin"
        }
    }
}
