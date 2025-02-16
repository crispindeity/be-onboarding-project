package study.crispin.surveyapi.controller.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSurveyRequest(
    val name: String,
    val description: String,
    val items: List<SurveyItemDto>
)

@Serializable
data class SurveyItemDto(
    val name: String,
    val description: String,
    val form: FormDto,
    val required: Boolean
)

@Serializable
sealed class FormDto {
    @Serializable
    @SerialName("longInput")
    data object LongInputDto : FormDto()

    @Serializable
    @SerialName("shortInput")
    data object ShortInputDto : FormDto()

    @Serializable
    @SerialName("multiSelect")
    data class MultiSelectDto(
        val options: List<String>
    ) : FormDto()

    @Serializable
    @SerialName("singleSelect")
    data class SingleSelectDto(
        val options: List<String>
    ) : FormDto()
}
