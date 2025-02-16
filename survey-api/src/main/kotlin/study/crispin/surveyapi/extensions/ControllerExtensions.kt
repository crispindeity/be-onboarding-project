package study.crispin.surveyapi.extensions

import study.crispin.surveyapi.controller.dto.request.FormDto
import study.crispin.surveyapi.controller.dto.request.SurveyItemDto
import study.crispin.surveycore.domain.Form
import study.crispin.surveycore.domain.SurveyItem

fun List<SurveyItemDto>.toDomain(): List<SurveyItem> =
    map {
        SurveyItem(
            it.name,
            it.description,
            it.form.toDomain(),
            it.required
        )
    }

fun FormDto.toDomain(): Form =
    when (this) {
        FormDto.LongInputDto -> Form.LongInput
        is FormDto.MultiSelectDto -> Form.MultiSelect(this.options)
        FormDto.ShortInputDto -> Form.ShortInput
        is FormDto.SingleSelectDto -> Form.SingleSelect(this.options)
    }
