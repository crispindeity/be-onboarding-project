package study.crispin.convention

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.testing.Test
import org.gradle.internal.extensions.core.extra
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.plugins(vararg pluginId: String) {
    with(this.pluginManager) {
        pluginId.forEach(::apply)
    }
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<KotlinJvmProjectExtension> {
        jvmToolchain(17)
    }
}

internal fun Project.configureTestTask() {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

internal fun Project.gitHookInstall() {
    if (!rootProject.extra.has("install-git-hooks")) {
        rootProject.extra["install-git-hooks"] = true

        val preCommit: InstallPreCommitHookTask =
            rootProject.tasks.create<InstallPreCommitHookTask>("installPreCommitHook") {
                group = "build setup"
                description = "Installs Kotlinter Git pre-commit hook"
            }

        val prePush: InstallPrePushHookTask =
            rootProject.tasks.create<InstallPrePushHookTask>("installPrePushHook") {
                group = "build setup"
                description = "Installs Kotlinter Git pre-push hook"
            }

        rootProject.tasks.named("prepareKotlinBuildScriptModel").configure {
            dependsOn(preCommit, prePush)
        }
    }
}

internal abstract class InstallPreCommitHookTask : DefaultTask() {
    @TaskAction
    fun installHook() {
        val hookFile: File = project.rootProject.file(".git/hooks/pre-commit")
        hookFile.parentFile.mkdirs()
        hookFile.writeText(
            """
            #!/bin/sh
            ./gradlew lintKotlin
            """.trimIndent()
        )
        hookFile.setExecutable(true)
    }
}

internal abstract class InstallPrePushHookTask : DefaultTask() {
    @TaskAction
    fun installHook() {
        val hookFile = project.rootProject.file(".git/hooks/pre-push")
        hookFile.parentFile.mkdirs()
        hookFile.writeText(
            """
            #!/bin/sh
            ./gradlew formatKotlin
            """.trimIndent()
        )
        hookFile.setExecutable(true)
    }
}
