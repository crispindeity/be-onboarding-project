package study.crispin.surveyapi.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer
            .defaultContentType(MediaType.APPLICATION_JSON)
            .parameterName("api-version")
            .favorParameter(true)
            .mediaTypes(
                mapOf("v1" to MediaType.valueOf("application/vnd.crispin.survey-v1+json"))
            )
    }
}
