package study.crispin.surveycore.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
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
                        submissionService.submitSurvey(requests)
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
                                            answer = listOf("음악", "스포츠")
                                        ),
                                        SubmitSurveyUseCase.Request(
                                            name = "성별",
                                            answer = listOf("남")
                                        )
                                    )
                            )
                        // when & then
                        submissionService.submitSurvey(requests)
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
