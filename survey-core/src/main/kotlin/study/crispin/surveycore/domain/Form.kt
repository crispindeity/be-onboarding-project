package study.crispin.surveycore.domain

sealed class Form {
    data class LongInput(val example: String) : Form()
    data class ShortInput(val example: String) : Form()
    data class MultiSelect(val options: List<String>) : Form()
    data class SingleSelect(val options: List<String>) : Form()
}
