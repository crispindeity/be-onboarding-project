package study.crispin.surveycore.fake

import java.util.UUID
import study.crispin.surveyinfra.adaptor.dto.SurveyDto
import study.crispin.surveyinfra.adaptor.dto.SurveyItemDto
import study.crispin.surveyinfra.port.SaveSurveyPort
import study.crispin.surveyinfra.port.SurveyPort

class SurveyFakePort : SurveyPort {
    private val surveys = mutableMapOf<UUID, SurveyDto>()
    private val surveyItems = mutableMapOf<Pair<UUID, Int>, List<SurveyItemDto>>()

    override fun save(request: SaveSurveyPort.Request): SurveyDto {
        val surveyDto =
            SurveyDto(
                id = UUID.randomUUID(),
                name = request.name,
                description = request.description,
                items = request.items
            )
        surveys[surveyDto.id] = surveyDto
        surveyItems[Pair(surveyDto.id, 0)] = surveyDto.items

        return surveyDto
    }

    override fun update(surveyDto: SurveyDto) {
        if (surveys.containsKey(surveyDto.id)) {
            surveys[surveyDto.id] =
                SurveyDto(
                    id = surveyDto.id,
                    name = surveyDto.name,
                    description = surveyDto.description
                )
        }
    }

    override fun update(
        surveyId: UUID,
        surveyItemVersion: Int,
        surveyItemDtos: List<SurveyItemDto>
    ) {
        surveyItems[Pair(surveyId, surveyItemVersion)] = surveyItemDtos
    }

    override fun findById(id: UUID): SurveyDto =
        surveys[id]?.let { surveyDto ->
            val items: List<SurveyItemDto> =
                surveyItems.entries
                    .filter { it.key.first == id }
                    .maxByOrNull { it.key.second }
                    ?.value
                    ?: emptyList()
            surveyDto.copy(items = items)
        } ?: throw IllegalArgumentException()

    override fun findByIdAndVersion(
        id: UUID,
        version: Int
    ): SurveyDto =
        surveys[id]?.let { surveyDto ->
            val items: List<SurveyItemDto> = surveyItems[Pair(id, version)] ?: emptyList()
            surveyDto.copy(items = items)
        } ?: throw IllegalArgumentException()

    override fun findVersionBySurveyId(id: UUID): Int =
        surveyItems.keys
            .filter { it.first == id }
            .maxOfOrNull { it.second }
            ?: throw IllegalArgumentException()
}
