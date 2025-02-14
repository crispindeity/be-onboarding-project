package study.crispin.surveycore.domain

sealed class Form {
    data object LongInput : Form()

    data object ShortInput : Form()

    data class MultiSelect(
        val options: List<String>
    ) : Form()

    data class SingleSelect(
        val options: List<String>
    ) : Form()

    fun valid(answer: List<String>) {
        when (this) {
            LongInput -> validateLongInput(answer)
            ShortInput -> validateShortInput(answer)
            is MultiSelect -> validateMultiSelect(answer, this.options)
            is SingleSelect -> validateSingleSelect(answer, this.options)
        }
    }

    private fun validateLongInput(answer: List<String>) {
        require(answer.size <= 1) {
            "Long input form allows at most one answer, but received ${answer.size} answers"
        }
    }

    private fun validateShortInput(answer: List<String>) {
        require(answer.size <= 1) {
            "Short input form allows at most one answer, but received ${answer.size} answers"
        }
    }

    private fun validateMultiSelect(
        answer: List<String>,
        options: List<String>
    ) {
        require(options.containsAll(answer) || answer.count { it !in options } == 0) {
            "Invalid multi-select options. Answers $answer contain " +
                "options not in the allowed list: $options"
        }
    }

    private fun validateSingleSelect(
        answer: List<String>,
        options: List<String>
    ) {
        require(answer.size == 1) {
            "Single select form requires exactly one answer, but received ${answer.size} answers"
        }
        require(options.contains(answer.first())) {
            "Invalid single select option. Answer '${answer.first()}'" +
                " is not in the allowed options: $options"
        }
    }
}
