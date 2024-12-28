package study.crispin.surveycore.fake

import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmitPort
import study.crispin.surveyinfra.port.SubmitPort

class SubmitFakePort : SubmitPort {

    override fun save(request: SaveSubmitPort.Request) {
        TODO("Not yet implemented")
    }

    override fun find(request: FindAnswerPort.Request) {
        TODO("Not yet implemented")
    }
}
