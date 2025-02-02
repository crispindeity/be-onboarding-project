package study.crispin.surveyinfra.repository

import java.util.UUID
import org.springframework.stereotype.Repository
import study.crispin.surveyinfra.repository.entity.SurveyEntity

@Repository
internal class SurveyMemoryRepository : SurveyRepository {
    private val storage: MutableMap<UUID, SurveyEntity> = mutableMapOf()

    override fun save(entity: SurveyEntity): SurveyEntity {
        val savedEntity: SurveyEntity =
            when (val id: UUID? = entity.id) {
                null ->
                    entity.run {
                        val newId: UUID = UUID.randomUUID()
                        SurveyEntity
                            .createWithId(
                                name,
                                description,
                                newId
                            ).also { storage[newId] = it }
                    }
                else -> entity.update(entity).also { storage[id] = it }
            }
        return savedEntity
    }

    override fun findById(id: UUID): SurveyEntity? = storage[id]
}
