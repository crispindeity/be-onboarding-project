package study.crispin.surveycore.fake

import java.util.UUID
import study.crispin.surveyinfra.adaptor.dto.SubmissionDto
import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmissionPort
import study.crispin.surveyinfra.port.SubmissionPort

class SubmissionFakePort : SubmissionPort {
    private val submissions: MutableMap<Pair<UUID, Int>, List<SubmissionDto>> = mutableMapOf()

    override fun save(request: SaveSubmissionPort.Request) {
        submissions[Pair(request.surveyId, request.version)] = request.submissionDtos
    }

    override fun find(request: FindAnswerPort.Request): List<SubmissionDto> =
        submissions[Pair(request.surveyId, request.answerVersion)] ?: emptyList()
}
