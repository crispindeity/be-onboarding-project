package study.crispin.surveyapi.config

import java.nio.charset.StandardCharsets
import kotlinx.serialization.json.Json
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@TestConfiguration
class ControllerTestConfig {
    @Bean
    fun kotlinSerializationJson(): Json =
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

    @Bean
    fun mockMvc(
        webApplicationContext: WebApplicationContext,
        kotlinSerializationMessageConverter: HttpMessageConverter<Any>,
        stringHttpMessageConverter: StringHttpMessageConverter
    ): MockMvc =
        MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(
                CharacterEncodingFilter(
                    StandardCharsets.UTF_8.name(),
                    true
                )
            ).defaultRequest<DefaultMockMvcBuilder>(
                MockMvcRequestBuilders
                    .get("/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
            ).build()
}
