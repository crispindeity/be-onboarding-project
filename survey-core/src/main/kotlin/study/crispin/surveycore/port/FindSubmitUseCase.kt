package study.crispin.surveycore.port

import java.util.UUID

interface FindSubmitUseCase {
    data class Request(
        val surveyId: UUID,
        val version: Int
    )

    fun findSubmit(request: Request)
}
