package study.crispin.surveycore.service

import org.springframework.stereotype.Service
import study.crispin.surveycore.domain.Submission
import study.crispin.surveycore.domain.SubmissionCollection
import study.crispin.surveycore.domain.Survey
import study.crispin.surveycore.extension.toDomain
import study.crispin.surveycore.extension.toDto
import study.crispin.surveycore.port.FindSubmissionUseCase
import study.crispin.surveycore.port.SubmissionUseCase
import study.crispin.surveycore.port.SubmitSurveyUseCase
import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmissionPort
import study.crispin.surveyinfra.port.SubmissionPort
import study.crispin.surveyinfra.port.SurveyPort

@Service
class SubmissionService(
    private val surveyPort: SurveyPort,
    private val submissionPort: SubmissionPort
) : SubmissionUseCase {
    override fun findSubmission(request: FindSubmissionUseCase.Request): List<Submission> =
        submissionPort
            .find(
                FindAnswerPort.Request(
                    request.surveyId,
                    request.version
                )
            ).toDomain()

    override fun submitSurvey(requests: SubmitSurveyUseCase.Requests) {
        val findedSurvey: Survey =
            surveyPort
                .findByIdAndVersion(
                    requests.surveyId,
                    requests.version
                ).toDomain()

        val submissions: List<Submission> =
            requests.requests.map {
                it.toDomain()
            }

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
}
