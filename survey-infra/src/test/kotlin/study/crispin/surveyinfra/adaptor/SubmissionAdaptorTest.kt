package study.crispin.surveyinfra.adaptor

import io.kotest.core.spec.style.DescribeSpec
import java.util.UUID
import study.crispin.surveyinfra.adaptor.dto.SubmissionDto
import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmissionPort
import study.crispin.surveyinfra.port.SubmissionPort
import study.crispin.surveyinfra.repository.SubmissionMemoryRepository
import study.crispin.surveyinfra.repository.SubmissionRepository
import study.crispin.surveyinfra.repository.SurveyItemRepository
import study.crispin.surveyinfra.repository.SurveyRepository
import study.crispin.surveyinfra.repository.entity.FormEmbeddable
import study.crispin.surveyinfra.repository.entity.FormType
import study.crispin.surveyinfra.repository.entity.SurveyEntity
import study.crispin.surveyinfra.repository.entity.SurveyItemEntity

class SubmissionAdaptorTest :
    DescribeSpec({

        lateinit var submissionAdaptor: SubmissionPort
        lateinit var surveyRepository: SurveyRepository
        lateinit var submissionRepository: SubmissionRepository
        lateinit var surveyItemRepository: SurveyItemRepository

        beforeTest {
            val surveyId = UUID(0L, 0L)
            val surveyItemId = UUID(-1L, -1L)
            submissionRepository = SubmissionMemoryRepository()
            submissionAdaptor =
                SubmissionAdaptor(
                    submissionRepository
                )

            surveyRepository.save(
                SurveyEntity.createWithId(
                    "관심사 조사",
                    "성별과 나이에 따른 관심사 조사",
                    surveyId
                )
            )

            surveyItemRepository.saveAll(
                listOf(
                    SurveyItemEntity.createWithId(
                        surveyId,
                        "이름",
                        "성함을 입력해주세요.",
                        FormEmbeddable(FormType.SHORT_INPUT),
                        true,
                        surveyItemId
                    ),
                    SurveyItemEntity.createWithId(
                        surveyId,
                        "나이",
                        "나이를 입력해주세요.",
                        FormEmbeddable(FormType.SHORT_INPUT),
                        true,
                        surveyItemId
                    ),
                    SurveyItemEntity.createWithId(
                        surveyId,
                        "관심사",
                        "관심 있는 분야를 모두 선택해주세요.",
                        FormEmbeddable(FormType.MULTI_SELECT, listOf("스포츠", "음악", "돈")),
                        true,
                        surveyItemId
                    ),
                    SurveyItemEntity.createWithId(
                        surveyId,
                        "성별",
                        "관심 있는 분야를 모두 선택해주세요.",
                        FormEmbeddable(FormType.SINGLE_SELECT, listOf("남", "여", "기타")),
                        true,
                        surveyItemId
                    )
                )
            )
        }

        describe("응답 제출 레파지토리 테스트") {

            describe("응답 제출 저장 테스트") {

                context("정상적인 저장 요청일 때") {

                    it("응답을 저장 할 수 있어야 한다.") {
                        // given
                        val surveyId = UUID(0L, 0L)
                        val surveyItemId = UUID(-1L, -1L)
                        val request =
                            SaveSubmissionPort.Request(
                                surveyItemId,
                                1,
                                listOf(
                                    SubmissionDto(
                                        "이름",
                                        listOf("응답봇1")
                                    ),
                                    SubmissionDto(
                                        "나이",
                                        listOf("10")
                                    ),
                                    SubmissionDto(
                                        "관심사",
                                        listOf("스포츠", "음악")
                                    ),
                                    SubmissionDto(
                                        "성별",
                                        listOf("남")
                                    )
                                )
                            )
                        // when
                        submissionAdaptor.save(request)

                        // then
                        submissionAdaptor.find(
                            FindAnswerPort.Request(
                                surveyId,
                                1
                            )
                        )
                    }
                }
            }
        }
    })
