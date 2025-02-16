package study.crispin.surveyinfra.adaptor

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import java.util.UUID
import study.crispin.surveyinfra.adaptor.dto.SubmissionDto
import study.crispin.surveyinfra.port.FindAnswerPort
import study.crispin.surveyinfra.port.SaveSubmissionPort
import study.crispin.surveyinfra.port.SubmissionPort
import study.crispin.surveyinfra.repository.SubmissionMemoryRepository
import study.crispin.surveyinfra.repository.SubmissionRepository
import study.crispin.surveyinfra.repository.SurveyItemMemoryRepository
import study.crispin.surveyinfra.repository.SurveyItemRepository
import study.crispin.surveyinfra.repository.SurveyMemoryRepository
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
            submissionRepository = SubmissionMemoryRepository()
            surveyRepository = SurveyMemoryRepository()
            surveyItemRepository = SurveyItemMemoryRepository()
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
                    SurveyItemEntity(
                        surveyId,
                        "이름",
                        "성함을 입력해주세요.",
                        FormEmbeddable(FormType.SHORT_INPUT),
                        true
                    ),
                    SurveyItemEntity(
                        surveyId,
                        "나이",
                        "나이를 입력해주세요.",
                        FormEmbeddable(FormType.SHORT_INPUT),
                        true
                    ),
                    SurveyItemEntity(
                        surveyId,
                        "관심사",
                        "관심 있는 분야를 모두 선택해주세요.",
                        FormEmbeddable(FormType.MULTI_SELECT, listOf("스포츠", "음악", "돈")),
                        true
                    ),
                    SurveyItemEntity(
                        surveyId,
                        "성별",
                        "관심 있는 분야를 모두 선택해주세요.",
                        FormEmbeddable(FormType.SINGLE_SELECT, listOf("남", "여", "기타")),
                        true
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
                        val request =
                            SaveSubmissionPort.Request(
                                surveyId,
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
                        val submissionDtos: List<SubmissionDto> =
                            submissionAdaptor.find(
                                FindAnswerPort.Request(
                                    surveyId,
                                    1
                                )
                            )

                        submissionDtos shouldHaveSize 4
                    }
                }
            }

            describe("응답 조회 테스트") {

                context("정상적인 설문조사 아이디와 응답 버전을 통한 조회 요청일 때") {

                    it("응답을 조회 할 수 있어야 한다.") {
                        // given
                        val surveyId = UUID(0L, 0L)
                        val request =
                            SaveSubmissionPort.Request(
                                surveyId,
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
                        submissionAdaptor.save(request)

                        // when
                        val result: List<SubmissionDto> =
                            submissionAdaptor.find(
                                FindAnswerPort.Request(
                                    surveyId,
                                    1
                                )
                            )

                        // then
                        result shouldHaveSize 4
                    }
                }
            }
        }
    })
