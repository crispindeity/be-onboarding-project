package study.crispin.surveycore.domain

data class SubmissionCollection(
    val submissions: List<Submission>
) {
    fun submissionValid(items: List<SurveyItem>) {
        validate(items)
    }

    private fun validate(items: List<SurveyItem>) {
        validateSubmissionCount(items)
        validateSubmissionNames(items)
        validateSubmissionItems(items)
    }

    private fun validateSubmissionCount(items: List<SurveyItem>) {
        require(submissions.size == items.size) {
            "Number of submissions (${submissions.size}) must match number of survey items (${items.size})"
        }
    }

    private fun validateSubmissionNames(items: List<SurveyItem>) {
        val surveyItemNames: Set<String> = items.map { it.name }.toSet()
        val submissionNames: Set<String> = submissions.map { it.name }.toSet()

        require(surveyItemNames == submissionNames) {
            "Submission names do not match survey item names. " +
                "Survey items: $surveyItemNames, Submissions: $submissionNames"
        }
    }

    private fun validateSubmissionItems(items: List<SurveyItem>) {
        val surveyItemMap: Map<String, SurveyItem> = items.associateBy { it.name }
        val submissionMap: Map<String, Submission> = submissions.associateBy { it.name }

        surveyItemMap.forEach { (itemName: String, surveyItem: SurveyItem) ->
            val submission: Submission =
                submissionMap[itemName]
                    ?: throw IllegalStateException("No submission found for item: $itemName")
            surveyItem.form.valid(submission.answer)
        }
    }
}
