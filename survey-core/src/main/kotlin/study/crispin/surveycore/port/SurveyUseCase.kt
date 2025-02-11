package study.crispin.surveycore.port

interface SurveyUseCase :
    CreateSurveyUseCase,
    UpdateSurveyUseCase,
    SubmitSurveyUseCase,
    FindSurveyUseCase,
    FindSubmitUseCase
