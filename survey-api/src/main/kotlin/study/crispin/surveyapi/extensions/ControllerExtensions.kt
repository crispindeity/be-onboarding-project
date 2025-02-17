package study.crispin.surveyapi.extensions

import study.crispin.surveyapi.controller.dto.request.FormRequestDto
import study.crispin.surveyapi.controller.dto.request.SurveyItemRequestDto
import study.crispin.surveycore.domain.Form
import study.crispin.surveycore.domain.SurveyItem

fun List<SurveyItemRequestDto>.toDomain(): List<SurveyItem> =
    map {
        SurveyItem(
            it.name,
            it.description,
            it.form.toDomain(),
            it.required
        )
    }

fun FormRequestDto.toDomain(): Form =
    when (this) {
        FormRequestDto.LongInputDto -> Form.LongInput
        is FormRequestDto.MultiSelectDto -> Form.MultiSelect(this.options)
        FormRequestDto.ShortInputDto -> Form.ShortInput
        is FormRequestDto.SingleSelectDto -> Form.SingleSelect(this.options)
    }
