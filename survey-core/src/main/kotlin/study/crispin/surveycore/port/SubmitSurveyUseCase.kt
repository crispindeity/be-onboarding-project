package study.crispin.surveycore.port

import java.util.UUID
import study.crispin.surveycore.domain.Submit

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

    fun Request.toDomain(): Submit =
        Submit(
            name = this.name,
            answer = this.answer
        )

    fun submitSurvey(requests: Requests)
}
