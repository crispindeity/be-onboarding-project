pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "be-onboarding-project"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":survey-core",
    ":survey-infra"
)
