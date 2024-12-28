package internal

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jmailen.gradle.kotlinter.KotlinterPlugin
import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.InstallPreCommitHookTask
import org.jmailen.gradle.kotlinter.tasks.LintTask
import org.gradle.kotlin.dsl.extra

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
                target.tasks.withType<LintTask>().findByName(lintKotlinTaskName)?.disable()
                target.tasks.withType<FormatTask>().findByName(formatKotlinTaskName)?.disable()
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

@Suppress("UNCHECKED_CAST")
inline fun <T : Any?> ExtraPropertiesExtension.getOrPut(key: String, value: () -> T): T {
    return if (has(key)) {
        get(key) as T
    } else {
        value().also { set(key, it) }
    }
}

internal inline fun <T : Any?> Project.runOnce(name: String, block: () -> T) =
    this.extra.getOrPut("build.internal.cache.$name", block)

val Project.production
    get() = this.runOnce("production") { (properties["production"] as? String).toBoolean() }

val Project.isNative: Boolean
    get() = this.runOnce("isNative") { (properties[isNativeProperty] as? String).toBoolean() }

val Project.isNativeProperty
    get() = "image.native"

val Project.envs: Map<String, String>
    get() = this.runOnce("envs") {
        properties.filterKeys { it.startsWith("env.") }
            .mapNotNull { it.key.substring(4) to it.value.toString() }
            .toMap() + applicationEnvs
    }

val Project.applicationEnvs: Map<String, String>
    get() = this.runOnce("applicationEnvs") {
        mapOf(
            "APPLICATION_NAME" to this.name,
            "APPLICATION_VERSION" to this.version as String,
            "APPLICATION_PRODUCTION" to this.production.toString()
        )
    }

val Project.buildEnv: BuildEnv
    get() = this.runOnce("buildEnv") {
        try {
            BuildEnv.valueOf((properties["build-env"] as? String ?: "local").uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("build-env property must be one of ${BuildEnv.values().toList()}")
        }
    }

class Profiles(
    delegate: List<Profile>,
) : List<Profile> by delegate {
    fun contains(profiles: List<Profile>): Boolean {
        return this.any { profiles.contains(it) }
    }

    fun contains(vararg profiles: Profile): Boolean {
        return this.any { profiles.contains(it) }
    }
}

typealias Profile = String

val Project.dev: Profile
    get() = "dev"

val Project.beta: Profile
    get() = "beta"

val Project.prod: Profile
    get() = "prod"

val Project.profileProperty
    get() = "profile"

val Project.profileString: String?
    get() = this.runOnce<String?>("profileString") { (properties[profileProperty] as? String)?.ifBlank { null } }

val Project.profiles: Profiles
    get() = this.runOnce("profiles") {
        val list = profileString?.let {
            it.split(',').map { it.trim() }.filter { it.isNotBlank() }
        } ?: emptyList()
        Profiles(list)
    }


enum class BuildEnv {
    LOCAL,
    CI
}


inline fun Project.withBuildEnv(env: BuildEnv, block: () -> Unit) {
    if (this.buildEnv == env) {
        block()
    }
}
