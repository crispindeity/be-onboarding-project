package internal

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jmailen.gradle.kotlinter.KotlinterPlugin
import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.InstallPreCommitHookTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

class LintPlugin : Plugin<Project> {
    // 후크 설치 상태를 추적하기 위한 전역 플래그
    companion object {
        private const val HOOK_INSTALLATION_MARKER = "kotlinter.hooks.installed"
    }

    // 플러그인이 프로젝트에 적용될 때 실행되는 메인 메서드
    override fun apply(target: Project) {
        // KotlinterPlugin을 현재 프로젝트에 적용
        target.apply<KotlinterPlugin>()

        // KtLint 태스크를 비활성화하는 확장 함수 정의
        fun ConfigurableKtLintTask.disable() {
            // 태스크의 모든 의존성 제거
            this.setDependsOn(emptyList<Task>())
            // 소스를 빈 파일 세트로 설정
            this.setSource(target.files())
            // 태스크 비활성화
            enabled = false
        }

        // 프로젝트 평가 후 실행되는 블록
        target.afterEvaluate {
            // Kotlin 소스 셋을 순회
            extensions.getByType(KotlinProjectExtension::class.java).sourceSets.forEach {
                // test 소스셋은 건너뛰기
                if (it.name in arrayOf("test")) return@forEach

                // lint와 format 태스크 이름 생성
                val lintKotlinTaskName = "lintKotlin${it.name.replaceFirstChar(Char::titlecase)}"
                val formatKotlinTaskName =
                    "formatKotlin${it.name.replaceFirstChar(Char::titlecase)}"

                // lint 태스크 찾아서 비활성화
                target.tasks
                    .withType<LintTask>()
                    .findByName(lintKotlinTaskName)
                    ?.disable()

                // format 태스크 찾아서 비활성화
                target.tasks
                    .withType<FormatTask>()
                    .findByName(formatKotlinTaskName)
                    ?.disable()
            }
        }

        // 모든 프로젝트가 평가된 후 실행되는 블록
        target.gradle.projectsEvaluated {
            // LOCAL 환경에서만 실행
            target.withBuildEnv(BuildEnv.LOCAL) {
                // 후크가 아직 설치되지 않았는지 확인
                if (!target.rootProject.extensions.extraProperties
                        .has(HOOK_INSTALLATION_MARKER)
                ) {
                    // 후크를 설치할 적절한 프로젝트 찾기
                    val projectForHooks: Project = findSuitableProjectForHooks(target.rootProject)

                    // 현재 프로젝트가 후크 설치 대상 프로젝트인 경우
                    if (target == projectForHooks) {
                        // 후크 태스크 등록
                        registerHookTasks(target)
                        // 후크 설치 완료 표시
                        target.rootProject.extensions.extraProperties[HOOK_INSTALLATION_MARKER] =
                            true
                    }
                }
            }
        }
    }

    // 후크 설치에 적합한 프로젝트를 찾는 메서드
    private fun findSuitableProjectForHooks(rootProject: Project): Project =
        rootProject.findProject("build-logic") // build-logic 프로젝트 찾기
            ?: if (rootProject.buildFile.exists() ||
                rootProject
                    .file(
                        "build.gradle.kts"
                    ).exists()
            ) {
                rootProject // 루트 프로젝트에 빌드 파일이 있으면 사용
            } else {
                findFirstSuitableSubproject(rootProject) // 적절한 서브프로젝트 찾기
                    ?: rootProject // 모두 실패하면 루트 프로젝트 사용
            }

    // 빌드 파일이 있는 첫 번째 서브프로젝트를 찾는 메서드
    private fun findFirstSuitableSubproject(rootProject: Project): Project? =
        rootProject.subprojects.firstOrNull { project ->
            // Gradle 또는 Kotlin DSL 빌드 파일이 있는지 확인
            project.buildFile.exists() || project.file("build.gradle.kts").exists()
        }

    // Git 후크 관련 태스크를 등록하는 메서드
    private fun registerHookTasks(project: Project) {
        // pre-commit 후크 설치 태스크 등록
        project.tasks.register(
            "installKotlinterPreCommitHook",
            InstallPreCommitHookTask::class.java
        ) {
            this.group = "build setup"
            this.description = "Installs Kotlinter Git pre-commit hook"
        }

        // Kotlin 빌드 스크립트 모델 준비 태스크에 후크 설치 의존성 추가
        project.tasks.findByName("prepareKotlinBuildScriptModel")?.let { task ->
            task.dependsOn(project.tasks.getByName("installKotlinterPrePushHook"))
            task.dependsOn(project.tasks.getByName("installKotlinterPreCommitHook"))
        }
    }
}

internal enum class BuildEnv {
    LOCAL
}

// 특정 빌드 환경에서만 코드 블록을 실행하는 Project 확장 함수
internal inline fun Project.withBuildEnv(
    env: BuildEnv,
    block: () -> Unit
) {
    // buildEnv 프로퍼티가 지정된 환경과 일치하는 경우에만 실행
    if (properties["buildEnv"]?.toString() == env.name) {
        block()
    }
}
