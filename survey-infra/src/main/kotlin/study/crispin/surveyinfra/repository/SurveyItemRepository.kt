package study.crispin.surveyinfra.repository

import java.util.UUID
import study.crispin.surveyinfra.repository.entity.SurveyItemEntity

internal interface SurveyItemRepository {
    fun saveAll(entities: List<SurveyItemEntity>): List<SurveyItemEntity>
    fun findBySurveyIdAndMaxVersion(id: UUID): List<SurveyItemEntity>
}
