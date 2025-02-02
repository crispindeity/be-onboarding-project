package study.crispin.surveyinfra.port

import study.crispin.surveyinfra.adaptor.dto.SurveyDto

interface UpdateSurveyPort {
    fun update(surveyDto: SurveyDto)
}
