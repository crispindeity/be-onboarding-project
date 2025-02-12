package study.crispin.surveyinfra.repository.entity

import java.time.LocalDateTime
import java.util.UUID

data class SubmissionEntity(
    val name: String,
    val surveyItemId: UUID,
    val surveyItemVersion: Int,
    val answers: List<String> = emptyList()
) {
    var id: UUID? = null
        private set

    val createdAt: LocalDateTime = LocalDateTime.now()

    companion object {
        fun createWithId(
            name: String,
            surveyItemId: UUID,
            surveyItemVersion: Int,
            answers: List<String>
        ): SubmissionEntity =
            SubmissionEntity(
                name,
                surveyItemId,
                surveyItemVersion,
                answers
            ).apply {
                this.id = id
            }
    }

    fun update(entity: SubmissionEntity): SubmissionEntity {
        this.id = entity.id
        return this
    }
}
