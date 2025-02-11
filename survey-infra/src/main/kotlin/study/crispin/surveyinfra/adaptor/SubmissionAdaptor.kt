package study.crispin.surveyinfra.adaptor

import org.springframework.stereotype.Component
import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmissionPort
import study.crispin.surveyinfra.port.SubmissionPort
import study.crispin.surveyinfra.repository.SubmissionRepository

@Component
internal class SubmissionAdaptor(
    private val submissionRepository: SubmissionRepository
) : SubmissionPort {
    override fun save(request: SaveSubmissionPort.Request) {
        TODO("Not yet implemented")
    }

    override fun find(request: FindAnswerPort.Request) {
        TODO("Not yet implemented")
    }
}
