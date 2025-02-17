package study.crispin.surveyapi.controller.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateSurveyRequestDto(
    val name: String,
    val description: String,
    val items: List<SurveyItemRequestDto>
)
