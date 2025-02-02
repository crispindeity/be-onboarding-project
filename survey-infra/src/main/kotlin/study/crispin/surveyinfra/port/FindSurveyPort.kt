package study.crispin.surveyinfra.port

import java.util.UUID
import study.crispin.surveyinfra.adaptor.dto.SurveyDto

interface FindSurveyPort {
    fun findById(id: UUID): SurveyDto

    fun findByIdAndVersion(
        id: UUID,
        version: Int
    ): SurveyDto
}
