package study.crispin.surveyapi.controller.dto.request

import java.util.UUID
import kotlinx.serialization.Serializable
import study.crispin.surveyapi.config.UUIDSerializer

@Serializable
data class UpdateSurveyRequestDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val description: String,
    val items: List<SurveyItemRequestDto> = emptyList()
)
