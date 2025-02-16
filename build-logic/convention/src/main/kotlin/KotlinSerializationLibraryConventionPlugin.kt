import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import study.crispin.convention.libs
import study.crispin.convention.plugins

class KotlinSerializationLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins(
                "kotlin.plugin.serialization",
                "kotlinx-serialization"
            )

            dependencies {
                add("implementation", libs.findLibrary("kotlin-serialization-json").get())
                add("implementation", libs.findLibrary("kotlin-reflect").get())
            }
        }
    }
}
