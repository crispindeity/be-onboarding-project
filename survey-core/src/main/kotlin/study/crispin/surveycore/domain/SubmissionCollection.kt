package study.crispin.surveycore.domain

data class SubmissionCollection(
    val submissions: List<Submission>
) {
    fun submissionValid(items: List<SurveyItem>) {
        validate(items)
    }

    private fun validate(items: List<SurveyItem>) {
        checkMissingRequiredItems(items)
        verifySubmissionCompleteness(items)
        validateSubmissionAnswers(items)
    }

    private fun checkMissingRequiredItems(items: List<SurveyItem>) {
        val requiredSurveyItemName: List<String> =
            items
                .filter { it.required }
                .map { it.name }
        val submissionName: List<String> =
            submissions
                .map { it.name }

        require(
            submissionName.containsAll(requiredSurveyItemName)
        ) {
            "A required survey item is missing from the submission. " +
                "Missing required survey item: ${
                    requiredSurveyItemName.filter {
                        it !in submissionName
                    }
                }"
        }
    }

    private fun verifySubmissionCompleteness(items: List<SurveyItem>) {
        val surveyItemNames: Set<String> = items.map { it.name }.toSet()
        val submissionNames: Set<String> = submissions.map { it.name }.toSet()

        if (surveyItemNames.size != submissionNames.size) {
            validateOptionalItemsCount(items, surveyItemNames, submissionNames)
        }
    }

    private fun validateOptionalItemsCount(
        items: List<SurveyItem>,
        surveyItemNames: Set<String>,
        submissionNames: Set<String>
    ) {
        require(
            items.filterNot(SurveyItem::required).count { it.name in surveyItemNames } ==
                surveyItemNames.size - submissionNames.size
        ) {
            "Submission names do not match survey item names. " +
                "Survey items: $surveyItemNames, Submissions: $submissionNames"
        }
    }

    private fun validateSubmissionAnswers(items: List<SurveyItem>) {
        val surveyItemMap: Map<String, SurveyItem> = items.associateBy { it.name }
        val submissionMap: Map<String, Submission> = submissions.associateBy { it.name }

        surveyItemMap.forEach { (itemName: String, surveyItem: SurveyItem) ->
            val submission: Submission =
                submissionMap[itemName]
                    ?: return
            surveyItem.form.valid(submission.answer)
        }
    }
}
