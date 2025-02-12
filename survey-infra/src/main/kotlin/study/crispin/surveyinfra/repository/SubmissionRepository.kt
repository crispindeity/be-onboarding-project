package study.crispin.surveyinfra.repository

import java.util.UUID
import study.crispin.surveyinfra.repository.entity.SubmissionEntity

interface SubmissionRepository {
    fun save(entity: SubmissionEntity): SubmissionEntity

    fun findById(id: UUID): SubmissionEntity?
}
