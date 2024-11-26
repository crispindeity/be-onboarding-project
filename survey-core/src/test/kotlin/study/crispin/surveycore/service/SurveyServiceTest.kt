package study.crispin.surveycore.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import study.crispin.surveycore.domain.Form
import study.crispin.surveycore.domain.Survey
import study.crispin.surveycore.domain.SurveyItem

class SurveyServiceTest : DescribeSpec({

    lateinit var surveyService: CreateSurveyUseCase

    beforeTest {
        surveyService = SurveyService()
    }

    describe("설문조사 서비스 테스트") {

        describe("설문 조사 생성 테스트 ") {

            context("정상적인 생성 요청일 때") {

                it("설문조사를 생성 할 수 있어야 한다.") {
                    // given
                    val request = CreateSurveyUseCase.Request(
                        "관심사 조사",
                        "성별에 따른 관심사 조사",
                        listOf(
                            SurveyItem(
                                "이름",
                                "성함을 입력해주세요.",
                                Form.ShortInput("홍길동"),
                                true
                            ),
                            SurveyItem(
                                "나이",
                                "나이을 입력해주세요.",
                                Form.ShortInput("10"),
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
                    actual shouldNotBe null
                }
            }
        }
    }
})
