import org.gradle.api.Plugin
import org.gradle.api.Project
import study.crispin.convention.plugins
import study.crispin.convention.libs
import org.gradle.kotlin.dsl.dependencies

class KotlinSpringLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins(
                "org.springframework.boot",
                "io.spring.dependency-management",
                "org.jetbrains.kotlin.plugin.spring",
            )

            dependencies {
                add("implementation", libs.findLibrary("spring.context").get())
            }
        }
    }
}
