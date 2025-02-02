package study.crispin.surveyinfra.port

import java.util.UUID
import study.crispin.surveyinfra.adaptor.dto.SubmitDto

interface SaveSubmitPort {
    data class Request(
        val surveyId: UUID,
        val version: Int,
        val submitDtos: List<SubmitDto>
    )

    fun save(request: Request)
}
