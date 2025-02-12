package study.crispin.surveyinfra.adaptor

import org.springframework.stereotype.Component
import study.crispin.surveyinfra.adaptor.dto.SubmissionDto
import study.crispin.surveyinfra.extension.toDto
import study.crispin.surveyinfra.extension.toEntity
import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmissionPort
import study.crispin.surveyinfra.port.SubmissionPort
import study.crispin.surveyinfra.repository.SubmissionRepository

@Component
internal class SubmissionAdaptor(
    private val submissionRepository: SubmissionRepository
) : SubmissionPort {
    override fun save(request: SaveSubmissionPort.Request) {
        submissionRepository.saveAll(request.toEntity())
    }

    override fun find(request: FindAnswerPort.Request): List<SubmissionDto> =
        submissionRepository
            .findAllBySurveyIdAndAnswerVersion(
                request.surveyId,
                request.answerVersion
            ).map { it.toDto() }
}
