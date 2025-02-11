package study.crispin.surveycore.port

import java.util.UUID

interface FindSubmissionUseCase {
    data class Request(
        val surveyId: UUID,
        val version: Int
    )

    fun findSubmission(request: Request)
}
