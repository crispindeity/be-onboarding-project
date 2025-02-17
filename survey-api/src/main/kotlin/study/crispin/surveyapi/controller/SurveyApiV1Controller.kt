package study.crispin.surveyapi.controller

import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import study.crispin.surveyapi.controller.dto.request.CreateSurveyRequestDto
import study.crispin.surveyapi.controller.dto.request.UpdateSurveyRequestDto
import study.crispin.surveyapi.controller.dto.response.Response
import study.crispin.surveyapi.extensions.toDomain
import study.crispin.surveycore.port.CreateSurveyUseCase
import study.crispin.surveycore.port.UpdateSurveyUseCase

@RestController
@RequestMapping("/api/survey")
class SurveyApiV1Controller(
    private val createSurveyUseCase: CreateSurveyUseCase,
    private val updateSurveyUseCase: UpdateSurveyUseCase
) {
    @PostMapping(produces = ["application/json", "application/vnd.crispin.survey-v1+json"])
    fun createSurvey(
        @RequestBody request: CreateSurveyRequestDto
    ): Response<Nothing> {
        createSurveyUseCase.createSurvey(
            CreateSurveyUseCase.Request(
                request.name,
                request.description,
                request.items.toDomain()
            )
        )
        return Response.success()
    }

    @PatchMapping(produces = ["application/json", "application/vnd.crispin.survey-v1+json"])
    fun updateSurvey(
        @RequestBody request: UpdateSurveyRequestDto
    ): Response<Nothing> {
        updateSurveyUseCase.updateSurvey(
            UpdateSurveyUseCase.Request(
                request.id,
                request.name,
                request.description,
                request.items.toDomain()
            )
        )
        return Response.success()
    }
}
