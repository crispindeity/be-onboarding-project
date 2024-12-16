package study.crispin.surveyinfra.adaptor

import java.util.UUID
import org.springframework.stereotype.Component
import study.crispin.surveyinfra.adaptor.dto.SurveyDto
import study.crispin.surveyinfra.adaptor.dto.SurveyItemDto
import study.crispin.surveyinfra.extension.toDto
import study.crispin.surveyinfra.extension.toEntity
import study.crispin.surveyinfra.port.SaveSurveyPort
import study.crispin.surveyinfra.port.SurveyPort
import study.crispin.surveyinfra.repository.SurveyItemRepository
import study.crispin.surveyinfra.repository.SurveyRepository
import study.crispin.surveyinfra.repository.entity.SurveyEntity

@Component
internal class SurveyAdaptor(
    private val surveyRepository: SurveyRepository,
    private val surveyItemRepository: SurveyItemRepository,
) : SurveyPort {
    override fun save(request: SaveSurveyPort.Request): SurveyDto {
        val surveyDto: SurveyDto = surveyRepository.save(request.toEntity()).toDto()
        val surveyItemDtos: List<SurveyItemDto> = surveyItemRepository.saveAll(
            request.items.map {
                it.toEntity(surveyDto.id)
            }
        ).map {
            it.toDto()
        }
        return surveyDto.update(surveyItemDtos)
    }

    override fun update(surveyDto: SurveyDto) {
        val updatedEntity: SurveyEntity = SurveyEntity.createWithId(
            id = surveyDto.id,
            name = surveyDto.name,
            description = surveyDto.description,
        )
        surveyRepository.save(updatedEntity)
    }

    override fun update(
        surveyId: UUID,
        surveyItemVersion: Int,
        surveyItemDtos: List<SurveyItemDto>,
    ) {
        surveyItemRepository.saveAll(
            surveyItemDtos.map {
                it.toEntity(
                    surveyId,
                    surveyItemVersion,
                )
            }
        )
    }

    override fun findById(id: UUID): SurveyDto {
        return surveyRepository.findById(id)?.let { entity ->
            val surveyDto: SurveyDto = entity.toDto()

            val surveyItemDtos: List<SurveyItemDto> =
                surveyItemRepository.findBySurveyIdAndMaxVersion(surveyDto.id)
                    .map { it.toDto() }
            surveyDto.update(surveyItemDtos)

        } ?: throw IllegalArgumentException()
    }

    override fun findVersionBySurveyId(id: UUID): Int {
        return surveyItemRepository.findBySurveyIdAndMaxVersion(id)
            .first()
            .version
    }
}
