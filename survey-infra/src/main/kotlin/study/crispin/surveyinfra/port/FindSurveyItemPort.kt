package study.crispin.surveyinfra.port

import java.util.UUID

interface FindSurveyItemPort {
    fun findVersionBySurveyId(id: UUID): Int
}
