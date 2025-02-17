package study.crispin.surveyapi.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import study.crispin.surveyapi.controller.dto.request.CreateSurveyRequest
import study.crispin.surveyapi.controller.dto.response.Response
import study.crispin.surveyapi.extensions.toDomain
import study.crispin.surveycore.port.CreateSurveyUseCase

@RestController
@RequestMapping("/api/survey")
class SurveyApiV1Controller(
    private val createSurveyUseCase: CreateSurveyUseCase
) {
    @PostMapping(produces = ["application/json", "application/vnd.crispin.survey-v1+json"])
    fun createSurvey(
        @RequestBody request: CreateSurveyRequest
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
}
