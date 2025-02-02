import org.gradle.api.Plugin
import org.gradle.api.Project
import study.crispin.convention.configureKotlinJvm
import study.crispin.convention.gitHookInstall
import study.crispin.convention.plugins

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins(
                "java-library",
                "org.jetbrains.kotlin.jvm",
                "org.jmailen.kotlinter"
            )
            configureKotlinJvm()
            gitHookInstall()
        }
    }
}
