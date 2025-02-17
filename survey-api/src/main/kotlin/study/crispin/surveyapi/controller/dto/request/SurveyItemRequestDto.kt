package study.crispin.surveyapi.controller.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SurveyItemRequestDto(
    val name: String,
    val description: String,
    val form: FormRequestDto,
    val required: Boolean
)
