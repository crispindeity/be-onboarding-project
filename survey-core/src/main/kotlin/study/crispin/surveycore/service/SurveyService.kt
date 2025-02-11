package study.crispin.surveycore.service

import org.springframework.stereotype.Service
import study.crispin.surveycore.domain.Submission
import study.crispin.surveycore.domain.SubmissionCollection
import study.crispin.surveycore.domain.Survey
import study.crispin.surveycore.extension.toDomain
import study.crispin.surveycore.extension.toDto
import study.crispin.surveycore.port.CreateSurveyUseCase
import study.crispin.surveycore.port.FindSubmissionUseCase
import study.crispin.surveycore.port.FindSurveyUseCase
import study.crispin.surveycore.port.SubmitSurveyUseCase
import study.crispin.surveycore.port.SurveyUseCase
import study.crispin.surveycore.port.UpdateSurveyUseCase
import study.crispin.surveyinfra.adaptor.dto.SurveyDto
import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmissionPort
import study.crispin.surveyinfra.port.SaveSurveyPort
import study.crispin.surveyinfra.port.SubmissionPort
import study.crispin.surveyinfra.port.SurveyPort

@Service
internal class SurveyService(
    private val surveyPort: SurveyPort,
    private val submissionPort: SubmissionPort
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

        val submissions: List<Submission> = requests.requests.map { it.toDomain() }

        SubmissionCollection(submissions)
            .submissionValid(findedSurvey.items)

        submissionPort.save(
            SaveSubmissionPort.Request(
                requests.surveyId,
                requests.version,
                submissions.toDto()
            )
        )
    }

    override fun findSurvey(request: FindSurveyUseCase.Request): SurveyDto =
        surveyPort.findById(request.surveyId)

    override fun findSubmission(request: FindSubmissionUseCase.Request) {
        submissionPort.find(
            FindAnswerPort.Request(
                request.surveyId,
                request.version
            )
        )
    }
}
