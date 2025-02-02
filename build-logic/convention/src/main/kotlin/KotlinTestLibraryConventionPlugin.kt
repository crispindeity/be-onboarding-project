import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import study.crispin.convention.configureKotlinJvm
import study.crispin.convention.configureTestTask
import study.crispin.convention.libs


class KotlinTestLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureTestTask()
            configureKotlinJvm()

            dependencies {
                add("testRuntimeOnly", libs.findLibrary("junit.platform.launcher").get())
                add("testImplementation", libs.findLibrary("kotlin.test.junit5").get())
                add("testImplementation", libs.findLibrary("kotest.runner.junit5").get())
            }
        }
    }
}
