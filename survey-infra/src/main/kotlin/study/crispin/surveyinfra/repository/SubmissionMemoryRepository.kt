package study.crispin.surveyinfra.repository

import java.util.UUID
import org.springframework.stereotype.Repository
import study.crispin.surveyinfra.repository.entity.SubmissionEntity

@Repository
internal class SubmissionMemoryRepository : SubmissionRepository {
    private val storage: MutableMap<UUID, SubmissionEntity> = mutableMapOf()

    override fun saveAll(entities: List<SubmissionEntity>): List<SubmissionEntity> =
        entities.map {
            when (it.id) {
                null ->
                    it.run {
                        val newId: UUID = UUID.randomUUID()
                        SubmissionEntity
                            .createWithId(
                                it.name,
                                it.surveyId,
                                it.surveyItemVersion,
                                it.answers,
                                newId
                            ).also { storage[newId] = it }
                    }

                else -> throw IllegalArgumentException()
            }
        }

    override fun findById(id: UUID): SubmissionEntity? = storage[id]

    override fun findAllBySurveyIdAndAnswerVersion(
        surveyId: UUID,
        answerVersion: Int
    ): List<SubmissionEntity> =
        storage.values.filter {
            it.surveyId == surveyId &&
                it.surveyItemVersion == answerVersion
        }
}
