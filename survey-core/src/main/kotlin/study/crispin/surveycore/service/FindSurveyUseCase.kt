package study.crispin.surveycore.service

import java.util.UUID
import study.crispin.surveyinfra.adaptor.dto.SurveyDto

interface FindSurveyUseCase {

    data class Request(val surveyId: UUID)

    fun findSurvey(request: Request): SurveyDto
}
