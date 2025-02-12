package study.crispin.surveyinfra.port

import java.util.UUID
import study.crispin.surveyinfra.adaptor.dto.SubmissionDto

interface FindAnswerPort {
    data class Request(
        val surveyId: UUID,
        val answerVersion: Int
    )

    fun find(request: Request): List<SubmissionDto>
}
