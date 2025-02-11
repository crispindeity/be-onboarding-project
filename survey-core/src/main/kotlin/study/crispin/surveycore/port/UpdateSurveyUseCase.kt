package study.crispin.surveycore.port

import java.util.UUID
import study.crispin.surveycore.domain.SurveyItem

interface UpdateSurveyUseCase {
    data class Request(
        val id: UUID,
        val name: String,
        val description: String,
        val items: List<SurveyItem> = emptyList()
    )

    fun updateSurvey(request: Request)
}
