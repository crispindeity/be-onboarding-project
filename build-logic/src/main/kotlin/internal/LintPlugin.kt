package internal

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jmailen.gradle.kotlinter.KotlinterPlugin
import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.InstallPreCommitHookTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

class LintPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.apply<KotlinterPlugin>()

        fun ConfigurableKtLintTask.disable() {
            this.setDependsOn(emptyList<Task>())
            this.setSource(target.files())
            enabled = false
        }
        target.afterEvaluate {
            extensions.getByType(KotlinProjectExtension::class.java).sourceSets.forEach {
                if (it.name in arrayOf("test")) return@forEach
                val lintKotlinTaskName = "lintKotlin${it.name.replaceFirstChar(Char::titlecase)}"
                val formatKotlinTaskName =
                    "formatKotlin${it.name.replaceFirstChar(Char::titlecase)}"
                target.tasks
                    .withType<LintTask>()
                    .findByName(lintKotlinTaskName)
                    ?.disable()
                target.tasks
                    .withType<FormatTask>()
                    .findByName(formatKotlinTaskName)
                    ?.disable()
            }
        }
        target.withBuildEnv(BuildEnv.LOCAL) {
            if (target.rootProject == target) {
                target.tasks.register(
                    "installKotlinterPreCommitHook",
                    InstallPreCommitHookTask::class.java
                ) {
                    this.group = "build setup"
                    this.description = "Installs Kotlinter Git pre-commit hook"
                }
                target.tasks.getByName("prepareKotlinBuildScriptModel") {
                    dependsOn(target.tasks.getByName("installKotlinterPrePushHook"))
                    dependsOn(target.tasks.getByName("installKotlinterPreCommitHook"))
                }
            }
        }
    }
}

internal enum class BuildEnv {
    LOCAL,
    CI
}

@Suppress("UNCHECKED_CAST")
internal inline fun <T : Any?> ExtraPropertiesExtension.getOrPut(
    key: String,
    value: () -> T
): T =
    if (has(key)) {
        get(key) as T
    } else {
        value().also { set(key, it) }
    }

internal inline fun <T : Any?> Project.runOnce(
    name: String,
    block: () -> T
) = this.extra.getOrPut("build.internal.cache.$name", block)

internal val Project.buildEnv: BuildEnv
    get() =
        this.runOnce("buildEnv") {
            try {
                BuildEnv.valueOf((properties["build-env"] as? String ?: "local").uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "build-env property must be one of ${
                        BuildEnv.values().toList()
                    }"
                )
            }
        }

internal inline fun Project.withBuildEnv(
    env: BuildEnv,
    block: () -> Unit
) {
    if (this.buildEnv == env) {
        block()
    }
}
