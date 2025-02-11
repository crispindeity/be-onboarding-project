package study.crispin.surveyinfra.port

import java.util.UUID
import study.crispin.surveyinfra.adaptor.dto.SubmissionDto

interface SaveSubmissionPort {
    data class Request(
        val surveyId: UUID,
        val version: Int,
        val submissionDtos: List<SubmissionDto>
    )

    fun save(request: Request)
}
