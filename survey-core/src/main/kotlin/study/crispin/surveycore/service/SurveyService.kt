package study.crispin.surveycore.service

import org.springframework.stereotype.Service
import study.crispin.surveycore.domain.Survey
import study.crispin.surveycore.extension.toDomain
import study.crispin.surveycore.extension.toDto
import study.crispin.surveyinfra.adaptor.dto.SurveyDto
import study.crispin.surveyinfra.port.SaveSurveyPort
import study.crispin.surveyinfra.port.SurveyPort

@Service
internal class SurveyService(
    private val surveyPort: SurveyPort
) : SurveyUseCase {

    override fun createSurvey(request: CreateSurveyUseCase.Request): Survey {
        val saveRequest = SaveSurveyPort.Request(
            request.name,
            request.description,
            request.items.map { it.toDto() },
        )
        return surveyPort.save(saveRequest).toDomain()
    }

    override fun updateSurvey(request: UpdateSurveyUseCase.Request) {
        surveyPort.findById(request.id).let { surveyDto ->
            val updatedSurveyDto: SurveyDto = surveyDto.update(
                request.name,
                request.description,
            )
            surveyPort.update(updatedSurveyDto)
        }

        if (request.items.isNotEmpty()) {
            val version: Int = surveyPort.findVersionBySurveyId(request.id)
            surveyPort.update(
                request.id,
                version + 1,
                request.items.map { it.toDto() },
            )
        }
    }
}
