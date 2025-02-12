package study.crispin.surveyinfra.repository

import java.util.UUID
import study.crispin.surveyinfra.repository.entity.SubmissionEntity

interface SubmissionRepository {
    fun saveAll(entities: List<SubmissionEntity>): List<SubmissionEntity>

    fun findById(id: UUID): SubmissionEntity?

    fun findAllBySurveyIdAndAnswerVersion(
        surveyId: UUID,
        answerVersion: Int
    ): List<SubmissionEntity>
}
