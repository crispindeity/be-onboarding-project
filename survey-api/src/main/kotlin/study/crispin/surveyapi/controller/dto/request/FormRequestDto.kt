package study.crispin.surveyapi.controller.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class FormRequestDto {
    @Serializable
    @SerialName("longInput")
    data object LongInputDto : FormRequestDto()

    @Serializable
    @SerialName("shortInput")
    data object ShortInputDto : FormRequestDto()

    @Serializable
    @SerialName("multiSelect")
    data class MultiSelectDto(
        val options: List<String>
    ) : FormRequestDto()

    @Serializable
    @SerialName("singleSelect")
    data class SingleSelectDto(
        val options: List<String>
    ) : FormRequestDto()
}
