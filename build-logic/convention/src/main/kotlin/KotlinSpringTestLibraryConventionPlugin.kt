import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import study.crispin.convention.libs
import study.crispin.convention.plugins

class KotlinSpringTestLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins(
                "kotlin.test",
                "kotlin.spring"
            )

            dependencies {
                add("testImplementation", libs.findLibrary("spring.boot.starter.test").get())
            }
        }
    }
}
