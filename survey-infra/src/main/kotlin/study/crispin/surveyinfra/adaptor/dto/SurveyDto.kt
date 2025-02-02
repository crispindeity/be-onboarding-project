package study.crispin.surveyinfra.adaptor.dto

import java.util.UUID

data class SurveyDto(
    val id: UUID,
    val name: String,
    val description: String,
    val items: List<SurveyItemDto> = emptyList()
) {
    fun update(items: List<SurveyItemDto>): SurveyDto =
        SurveyDto(
            id = this.id,
            name = this.name,
            description = this.description,
            items = items
        )

    fun update(
        name: String,
        description: String
    ): SurveyDto =
        SurveyDto(
            id = this.id,
            name = name,
            description = description
        )
}
