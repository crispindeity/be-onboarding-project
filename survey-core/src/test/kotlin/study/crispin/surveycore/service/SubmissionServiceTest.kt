package study.crispin.surveycore.service

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.util.UUID
import study.crispin.surveycore.domain.Submission
import study.crispin.surveycore.fake.SubmissionFakePort
import study.crispin.surveycore.fake.SurveyFakePort
import study.crispin.surveycore.port.FindSubmissionUseCase
import study.crispin.surveycore.port.SubmissionUseCase
import study.crispin.surveycore.port.SubmitSurveyUseCase
import study.crispin.surveyinfra.adaptor.dto.FormDto
import study.crispin.surveyinfra.adaptor.dto.SurveyDto
import study.crispin.surveyinfra.adaptor.dto.SurveyItemDto
import study.crispin.surveyinfra.port.SaveSurveyPort
import study.crispin.surveyinfra.port.SubmissionPort
import study.crispin.surveyinfra.port.SurveyPort

class SubmissionServiceTest :
    DescribeSpec({
        lateinit var submissionService: SubmissionUseCase
        lateinit var submissionPort: SubmissionPort
        lateinit var surveyPort: SurveyPort
        lateinit var surveyId: UUID

        beforeTest {
            submissionPort = SubmissionFakePort()
            surveyPort = SurveyFakePort()
            submissionService =
                SubmissionService(
                    surveyPort,
                    submissionPort
                )
            val surveyDto: SurveyDto =
                surveyPort.save(
                    SaveSurveyPort.Request(
                        "관심사 조사",
                        "성별에 따른 관심사 조사",
                        listOf(
                            SurveyItemDto(
                                "이름",
                                "성함을 입력해주세요.",
                                FormDto.ShortInput,
                                true
                            ),
                            SurveyItemDto(
                                "나이",
                                "나이을 입력해주세요.",
                                FormDto.ShortInput,
                                false
                            ),
                            SurveyItemDto(
                                "관심사",
                                "관심 있는 분야를 모두 선택해주세요.",
                                FormDto.MultiSelect(listOf("스포츠", "음악", "기술")),
                                true
                            ),
                            SurveyItemDto(
                                "성별",
                                "성별을 선택해 주세요.",
                                FormDto.SingleSelect(listOf("남", "여", "기타")),
                                true
                            )
                        )
                    )
                )
            surveyId = surveyDto.id
        }

        describe("응답 서비스 테스트") {

            describe("응답 제출 테스트") {

                context("정상적인 제출 요청일때") {

                    it("필수 응답 값 외의 응답 값을 빼고 요청했을 때 응답을 제출 할 수 있어야 한다.") {
                        // given
                        val requests =
                            SubmitSurveyUseCase.Requests(
                                surveyId = surveyId,
                                version = 0,
                                requests =
                                    listOf(
                                        SubmitSurveyUseCase.Request(
                                            name = "이름",
                                            answer = listOf("응답봇1")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "관심사",
                                            answer = listOf("음악", "스포츠", "기술")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "성별",
                                            answer = listOf("남")
                                        )
                                    )
                            )
                        // when & then
                        shouldNotThrowAny {
                            submissionService.submitSurvey(requests)
                        }
                    }

                    it("다중 선택 응답에 대해 부분 선택이 가능해야 한다") {
                        // given
                        val requests =
                            SubmitSurveyUseCase.Requests(
                                surveyId = surveyId,
                                version = 0,
                                requests =
                                    listOf(
                                        SubmitSurveyUseCase.Request(
                                            name = "이름",
                                            answer = listOf("응답봇1")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "나이",
                                            answer = listOf("10")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "관심사",
                                            answer = listOf("음악", "스포츠")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "성별",
                                            answer = listOf("남")
                                        )
                                    )
                            )
                        // when & then
                        shouldNotThrowAny {
                            submissionService.submitSurvey(requests)
                        }
                    }

                    it("제출한 응답을 제출 할 수 있어야 한다.") {
                        // given
                        val requests =
                            SubmitSurveyUseCase.Requests(
                                surveyId = surveyId,
                                version = 0,
                                requests =
                                    listOf(
                                        SubmitSurveyUseCase.Request(
                                            name = "이름",
                                            answer = listOf("응답봇1")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "나이",
                                            answer = listOf("10")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "관심사",
                                            answer = listOf("음악", "스포츠", "기술")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "성별",
                                            answer = listOf("남")
                                        )
                                    )
                            )
                        // when & then
                        shouldNotThrowAny {
                            submissionService.submitSurvey(requests)
                        }
                    }
                }

                context("비정상적인 제출 요청 테스트") {

                    it("단일 값에 대해 여러 응답을 하는 경우 예외가 발생해야 한다.") {
                        // given
                        val requests =
                            SubmitSurveyUseCase.Requests(
                                surveyId = surveyId,
                                version = 0,
                                requests =
                                    listOf(
                                        SubmitSurveyUseCase.Request(
                                            name = "이름",
                                            answer = listOf("응답봇1", "응답봇2")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "나이",
                                            answer = listOf("10")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "관심사",
                                            answer = listOf("음악", "스포츠")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "성별",
                                            answer = listOf("남")
                                        )
                                    )
                            )
                        // when & then
                        shouldThrow<IllegalArgumentException> {
                            submissionService.submitSurvey(requests)
                        }.message shouldBe
                            "Short input form allows at most one answer, but received 2 answers"
                    }

                    it("응답 내역 외의 값을 입력하여 응답하는 경우 예외가 발생헤야 한다.") {
                        // given
                        val requests =
                            SubmitSurveyUseCase.Requests(
                                surveyId = surveyId,
                                version = 0,
                                requests =
                                    listOf(
                                        SubmitSurveyUseCase.Request(
                                            name = "이름",
                                            answer = listOf("응답봇1")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "나이",
                                            answer = listOf("10")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "관심사",
                                            answer = listOf("음악", "스포츠")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "성별",
                                            answer = listOf("남")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "하하",
                                            answer = listOf("호호")
                                        )
                                    )
                            )
                        // when & then
                        shouldThrow<IllegalArgumentException> {
                            submissionService.submitSurvey(requests)
                        }.message shouldBe
                            "Submission names do not match survey item names. Survey items: [이름, 나이, 관심사, 성별], Submissions: [이름, 나이, 관심사, 성별, 하하]"
                    }

                    it("필수 입력값을 빼고 응답을 제출하는 경우 예외가 발생해야 한다.") {
                        // given
                        val requests =
                            SubmitSurveyUseCase.Requests(
                                surveyId = surveyId,
                                version = 0,
                                requests =
                                    listOf(
                                        SubmitSurveyUseCase.Request(
                                            name = "나이",
                                            answer = listOf("10")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "관심사",
                                            answer = listOf("음악", "스포츠")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "성별",
                                            answer = listOf("남")
                                        )
                                    )
                            )
                        // when & then
                        shouldThrow<IllegalArgumentException> {
                            submissionService.submitSurvey(requests)
                        }.message shouldBe
                            "A required survey item is missing from the submission. Missing required survey item: [이름]"
                    }

                    it("단일 선택값에 대해 여러개의 값을 선택하는 경우 예외가 발생해야 한다.") {
                        // given
                        val requests =
                            SubmitSurveyUseCase.Requests(
                                surveyId = surveyId,
                                version = 0,
                                requests =
                                    listOf(
                                        SubmitSurveyUseCase.Request(
                                            name = "이름",
                                            answer = listOf("응답봇1")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "나이",
                                            answer = listOf("10")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "관심사",
                                            answer = listOf("음악", "스포츠")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "성별",
                                            answer = listOf("남", "여")
                                        )
                                    )
                            )
                        // when & then
                        shouldThrow<IllegalArgumentException> {
                            submissionService.submitSurvey(requests)
                        }.message shouldBe
                            "Single select form requires exactly one answer, but received 2 answers"
                    }
                }
            }

            describe("응답 조회 테스트") {

                context("정상적인 조회 요청일때") {

                    it("제출한 응답을 조회 할 수 있어야 한다.") {
                        // given
                        val request =
                            FindSubmissionUseCase.Request(
                                surveyId,
                                0
                            )

                        submissionService.submitSurvey(
                            SubmitSurveyUseCase.Requests(
                                surveyId = surveyId,
                                version = 0,
                                requests =
                                    listOf(
                                        SubmitSurveyUseCase.Request(
                                            name = "이름",
                                            answer = listOf("응답봇1")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "나이",
                                            answer = listOf("10")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "관심사",
                                            answer = listOf("음악", "스포츠")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "성별",
                                            answer = listOf("남")
                                        )
                                    )
                            )
                        )
                        // when
                        val results: List<Submission> = submissionService.findSubmission(request)

                        // then
                        results shouldHaveSize 4
                    }
                }
            }
        }
    })
