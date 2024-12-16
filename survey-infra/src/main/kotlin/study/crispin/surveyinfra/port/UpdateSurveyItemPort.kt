package study.crispin.surveyinfra.port

import java.util.UUID
import study.crispin.surveyinfra.adaptor.dto.SurveyItemDto

interface UpdateSurveyItemPort {
    fun update(
        surveyId: UUID,
        surveyItemVersion: Int,
        surveyItemDtos: List<SurveyItemDto>,
    )
}
