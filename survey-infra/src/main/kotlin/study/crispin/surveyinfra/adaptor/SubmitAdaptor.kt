package study.crispin.surveyinfra.adaptor

import org.springframework.stereotype.Component
import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmitPort
import study.crispin.surveyinfra.port.SubmitPort
import study.crispin.surveyinfra.repository.SubmitRepository

@Component
internal class SubmitAdaptor(
    private val submitRepository: SubmitRepository
) : SubmitPort {
    override fun save(request: SaveSubmitPort.Request) {
        TODO("Not yet implemented")
    }

    override fun find(request: FindAnswerPort.Request) {
        TODO("Not yet implemented")
    }
}
