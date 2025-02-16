package study.crispin.surveyapi.controller.dto.response

data class Response<T>(
    val code: String,
    val result: T?
) {
    companion object {
        fun error(code: String) = Response(code, null)

        fun success() = Response("SUCCESS", null)

        fun <T> success(result: T) = Response("SUCCESS", result)
    }
}
