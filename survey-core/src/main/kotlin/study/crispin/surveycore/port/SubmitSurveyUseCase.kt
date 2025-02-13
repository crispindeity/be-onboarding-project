package study.crispin.surveycore.port

import java.util.UUID

interface SubmitSurveyUseCase {
    data class Requests(
        val surveyId: UUID,
        val version: Int,
        val requests: List<Request>
    )

    data class Request(
        val name: String,
        val answer: List<String>
    )

    fun submitSurvey(requests: Requests)
}
