package study.crispin.surveyapi.controller.dto.request

import java.util.UUID

data class UpdateSurveyRequestDto(
    val id: UUID,
    val name: String,
    val description: String,
    val items: List<SurveyItemRequestDto> = emptyList()
)
