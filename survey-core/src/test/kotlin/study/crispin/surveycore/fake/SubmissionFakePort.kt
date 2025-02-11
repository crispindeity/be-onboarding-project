package study.crispin.surveycore.fake

import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmissionPort
import study.crispin.surveyinfra.port.SubmissionPort

class SubmissionFakePort : SubmissionPort {
    override fun save(request: SaveSubmissionPort.Request) {
        TODO("Not yet implemented")
    }

    override fun find(request: FindAnswerPort.Request) {
        TODO("Not yet implemented")
    }
}
