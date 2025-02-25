package study.crispin.surveyapi.controller

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import study.crispin.surveyapi.config.ControllerTestConfig
import study.crispin.surveyapi.controller.dto.request.CreateSurveyRequestDto
import study.crispin.surveyapi.controller.dto.request.FormRequestDto
import study.crispin.surveyapi.controller.dto.request.SurveyItemRequestDto
import study.crispin.surveycore.port.CreateSurveyUseCase
import study.crispin.surveycore.port.UpdateSurveyUseCase

@Import(ControllerTestConfig::class)
@WebMvcTest(SurveyApiV1Controller::class)
class SurveyApiV1ControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var json: Json

    @MockitoBean
    private lateinit var createSurveyUseCase: CreateSurveyUseCase

    @MockitoBean
    private lateinit var updateSurveyUseCase: UpdateSurveyUseCase

    @Nested
    inner class SurveyControllerTest {
        @Nested
        inner class SurveyControllerSuccessTest {
            @Test
            fun createSurveyRequestTest() {
                // given
                val request =
                    CreateSurveyRequestDto(
                        "관심사 조사",
                        "성별에 따른 관심사 조사",
                        listOf(
                            SurveyItemRequestDto(
                                "이름",
                                "성함을 입력해주세요.",
                                FormRequestDto.ShortInputDto,
                                true
                            ),
                            SurveyItemRequestDto(
                                "나이",
                                "나이를 입력해주세요.",
                                FormRequestDto.ShortInputDto,
                                true
                            ),
                            SurveyItemRequestDto(
                                "관심사",
                                "관심 있는 분야를 모두 선택해주세요.",
                                FormRequestDto.MultiSelectDto(listOf("음악", "스포츠", "드라마")),
                                true
                            ),
                            SurveyItemRequestDto(
                                "성별",
                                "성별을 선택해주세요.",
                                FormRequestDto.SingleSelectDto(listOf("남", "여")),
                                true
                            )
                        )
                    )
                // when & then
                mockMvc
                    .perform(
                        MockMvcRequestBuilders
                            .post("/api/survey")
                            .content(
                                json.encodeToString(
                                    CreateSurveyRequestDto.serializer(),
                                    request
                                )
                            ).accept("application/vnd.crispin.survey-v1+json")
                            .contentType(MediaType.APPLICATION_JSON)
                    ).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk)
            }
        }
    }
}
