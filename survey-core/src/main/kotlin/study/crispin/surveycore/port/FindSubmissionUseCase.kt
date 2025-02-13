package study.crispin.surveycore.port

import java.util.UUID
import study.crispin.surveycore.domain.Submission

interface FindSubmissionUseCase {
    data class Request(
        val surveyId: UUID,
        val version: Int
    )

    fun findSubmission(request: Request): List<Submission>
}
