package study.crispin.surveycore.service

import org.springframework.stereotype.Service
import study.crispin.surveycore.domain.Submit
import study.crispin.surveycore.domain.SubmitCollection
import study.crispin.surveycore.domain.Survey
import study.crispin.surveycore.extension.toDomain
import study.crispin.surveycore.extension.toDto
import study.crispin.surveyinfra.adaptor.dto.SurveyDto
import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmitPort
import study.crispin.surveyinfra.port.SaveSurveyPort
import study.crispin.surveyinfra.port.SubmitPort
import study.crispin.surveyinfra.port.SurveyPort

@Service
internal class SurveyService(
    private val surveyPort: SurveyPort,
    private val submitPort: SubmitPort
) : SurveyUseCase {
    override fun createSurvey(request: CreateSurveyUseCase.Request): Survey {
        val saveRequest =
            SaveSurveyPort.Request(
                request.name,
                request.description,
                request.items.map { it.toDto() }
            )
        return surveyPort.save(saveRequest).toDomain()
    }

    override fun updateSurvey(request: UpdateSurveyUseCase.Request) {
        surveyPort.findById(request.id).let { surveyDto ->
            val updatedSurveyDto: SurveyDto =
                surveyDto.update(
                    request.name,
                    request.description
                )
            surveyPort.update(updatedSurveyDto)
        }

        if (request.items.isNotEmpty()) {
            val version: Int = surveyPort.findVersionBySurveyId(request.id)
            surveyPort.update(
                request.id,
                version + 1,
                request.items.map { it.toDto() }
            )
        }
    }

    override fun submitSurvey(requests: SubmitSurveyUseCase.Requests) {
        val findedSurvey: Survey =
            surveyPort
                .findByIdAndVersion(
                    requests.surveyId,
                    requests.version
                ).toDomain()

        val submits: List<Submit> = requests.requests.map { it.toDomain() }

        SubmitCollection(submits)
            .submitValid(findedSurvey.items)

        submitPort.save(
            SaveSubmitPort.Request(
                requests.surveyId,
                requests.version,
                submits.toDto()
            )
        )
    }

    override fun findSurvey(request: FindSurveyUseCase.Request): SurveyDto =
        surveyPort.findById(request.surveyId)

    override fun findSubmit(request: FindSubmitUseCase.Request) {
        submitPort.find(
            FindAnswerPort.Request(
                request.surveyId,
                request.version
            )
        )
    }
}
