package study.crispin.surveyinfra.port

import java.util.UUID

interface FindAnswerPort {
    data class Request(
        val surveyId: UUID,
        val answerVersion: Int
    )

    fun find(request: Request)
}
