package study.crispin.surveyinfra.repository

import java.util.UUID
import org.springframework.stereotype.Repository
import study.crispin.surveyinfra.repository.entity.SubmissionEntity

@Repository
internal class SubmissionMemoryRepository : SubmissionRepository {
    private val storage: MutableMap<UUID, SubmissionEntity> = mutableMapOf()

    override fun save(entity: SubmissionEntity): SubmissionEntity {
        val savedEntity: SubmissionEntity =
            when (val id: UUID? = entity.id) {
                null -> {
                    entity.run {
                        val newId: UUID = UUID.randomUUID()
                        SubmissionEntity
                            .createWithId(
                                entity.name,
                                entity.surveyItemId,
                                entity.surveyItemVersion,
                                entity.answers
                            ).also { storage[newId] = it }
                    }
                }
                else -> entity.update(entity).also { storage[id] = it }
            }
        return savedEntity
    }

    override fun findById(id: UUID): SubmissionEntity? = storage[id]
}
