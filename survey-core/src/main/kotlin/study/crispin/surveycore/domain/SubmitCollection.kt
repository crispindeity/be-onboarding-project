package study.crispin.surveycore.domain

data class SubmitCollection(
    val submits: List<Submit>
) {
    fun submitValid(items: List<SurveyItem>) {
        validate(items)
    }

    private fun validate(items: List<SurveyItem>) {
        validateSubmitCount(items)
        validateSubmitNames(items)
        validateSubmitItems(items)
    }

    private fun validateSubmitCount(items: List<SurveyItem>) {
        require(submits.size == items.size) {
            "Number of submissions (${submits.size}) must match number of survey items (${items.size})"
        }
    }

    private fun validateSubmitNames(items: List<SurveyItem>) {
        val surveyItemNames: Set<String> = items.map { it.name }.toSet()
        val submitNames: Set<String> = submits.map { it.name }.toSet()

        require(surveyItemNames == submitNames) {
            "Submission names do not match survey item names. " +
                    "Survey items: $surveyItemNames, Submissions: $submitNames"
        }
    }

    private fun validateSubmitItems(items: List<SurveyItem>) {
        val surveyItemMap: Map<String, SurveyItem> = items.associateBy { it.name }
        val submitMap: Map<String, Submit> = submits.associateBy { it.name }

        surveyItemMap.forEach { (itemName: String, surveyItem: SurveyItem) ->
            val submit: Submit = submitMap[itemName]
                ?: throw IllegalStateException("No submission found for item: $itemName")
            surveyItem.form.valid(submit.answer)
        }
    }
}
