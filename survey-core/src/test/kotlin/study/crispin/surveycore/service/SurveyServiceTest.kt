package study.crispin.surveycore.service

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import study.crispin.surveycore.domain.Form
import study.crispin.surveycore.domain.Survey
import study.crispin.surveycore.domain.SurveyItem
import study.crispin.surveycore.extension.toDomain
import study.crispin.surveycore.fake.SubmitFakePort
import study.crispin.surveycore.fake.SurveyFakePort
import study.crispin.surveycore.port.CreateSurveyUseCase
import study.crispin.surveycore.port.FindSurveyUseCase
import study.crispin.surveycore.port.SurveyUseCase
import study.crispin.surveycore.port.UpdateSurveyUseCase

class SurveyServiceTest :
    DescribeSpec({

        lateinit var surveyService: SurveyUseCase

        beforeTest {
            surveyService =
                SurveyService(
                    surveyPort = SurveyFakePort(),
                    submitPort = SubmitFakePort()
                )
        }

        describe("설문조사 서비스 테스트") {

            describe("설문조사 생성 테스트") {

                context("정상적인 생성 요청일 때") {

                    it("설문조사를 생성 할 수 있어야 한다.") {
                        // given
                        val request =
                            CreateSurveyUseCase.Request(
                                "관심사 조사",
                                "성별에 따른 관심사 조사",
                                listOf(
                                    SurveyItem(
                                        "이름",
                                        "성함을 입력해주세요.",
                                        Form.ShortInput,
                                        true
                                    ),
                                    SurveyItem(
                                        "나이",
                                        "나이을 입력해주세요.",
                                        Form.ShortInput,
                                        false
                                    ),
                                    SurveyItem(
                                        "관심사",
                                        "관심 있는 분야를 모두 선택해주세요.",
                                        Form.MultiSelect(listOf("스포츠", "음약", "기술")),
                                        true
                                    )
                                )
                            )

                        // when
                        val actual: Survey = surveyService.createSurvey(request)

                        // then
                        assertSoftly {
                            actual.name shouldBe "관심사 조사"
                            actual.items shouldHaveSize 3
                        }
                    }
                }
            }

            describe("설문 조사 업데이트 테스트") {

                context("정상적인 업데이트 요청일 때") {

                    it("설문조사를 업데이트 할 수 있어야 한다.") {
                        // given
                        val items: List<SurveyItem> =
                            listOf(
                                SurveyItem(
                                    "이름",
                                    "성함을 입력해주세요.",
                                    Form.ShortInput,
                                    true
                                ),
                                SurveyItem(
                                    "나이",
                                    "나이을 입력해주세요.",
                                    Form.ShortInput,
                                    false
                                ),
                                SurveyItem(
                                    "관심사",
                                    "관심 있는 분야를 모두 선택해주세요.",
                                    Form.MultiSelect(listOf("스포츠", "음약", "기술")),
                                    true
                                )
                            )
                        val createRequest =
                            CreateSurveyUseCase.Request(
                                "관심사 조사",
                                "나이에 따른 관심사 조사",
                                items
                            )
                        val createdSurvey: Survey = surveyService.createSurvey(createRequest)

                        val request =
                            UpdateSurveyUseCase.Request(
                                createdSurvey.id!!,
                                "연령별 관심사 조사",
                                "연령에 따른 관심사 조사"
                            )

                        // when
                        surveyService.updateSurvey(request)

                        // then
                        val actual: Survey =
                            surveyService
                                .findSurvey(
                                    FindSurveyUseCase.Request(createdSurvey.id!!)
                                ).toDomain()

                        assertSoftly {
                            actual.name shouldBe "연령별 관심사 조사"
                            actual.description shouldBe "연령에 따른 관심사 조사"
                        }
                    }
                }
            }

            describe("설문 조사 항목 업데이트 테스트") {

                context("정상적인 업데이트 요청일 때") {

                    it("설문조사 항목을 업데이트 할 수 있어야 한다.") {
                        // given
                        val items: List<SurveyItem> =
                            listOf(
                                SurveyItem(
                                    "이름",
                                    "성함을 입력해주세요.",
                                    Form.ShortInput,
                                    true
                                ),
                                SurveyItem(
                                    "나이",
                                    "나이을 입력해주세요.",
                                    Form.ShortInput,
                                    false
                                ),
                                SurveyItem(
                                    "관심사",
                                    "관심 있는 분야를 모두 선택해주세요.",
                                    Form.MultiSelect(listOf("스포츠", "음약", "기술")),
                                    true
                                )
                            )
                        val createRequest =
                            CreateSurveyUseCase.Request(
                                "관심사 조사",
                                "나이에 따른 관심사 조사",
                                items
                            )
                        val createdSurvey: Survey = surveyService.createSurvey(createRequest)

                        val updateItems: List<SurveyItem> =
                            listOf(
                                SurveyItem(
                                    "이름",
                                    "성함을 입력해주세요.",
                                    Form.ShortInput,
                                    true
                                ),
                                SurveyItem(
                                    "나이",
                                    "나이을 입력해주세요.",
                                    Form.ShortInput,
                                    false
                                ),
                                SurveyItem(
                                    "관심사",
                                    "관심 있는 분야를 모두 선택해주세요.",
                                    Form.MultiSelect(listOf("스포츠", "음약", "기술")),
                                    true
                                ),
                                SurveyItem(
                                    "성별",
                                    "성별을 선택해 주세요.",
                                    Form.SingleSelect(listOf("남", "여", "기타")),
                                    true
                                )
                            )

                        val request =
                            UpdateSurveyUseCase.Request(
                                createdSurvey.id!!,
                                createdSurvey.name,
                                createdSurvey.description,
                                updateItems
                            )

                        // when
                        surveyService.updateSurvey(request)

                        // then
                        val actual: Survey =
                            surveyService
                                .findSurvey(
                                    FindSurveyUseCase.Request(createdSurvey.id!!)
                                ).toDomain()

                        actual.items shouldHaveSize 4
                    }
                }
            }
        }
    })
