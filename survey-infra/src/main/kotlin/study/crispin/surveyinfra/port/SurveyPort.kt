package study.crispin.surveyinfra.port

interface SurveyPort :
    SaveSurveyPort,
    UpdateSurveyPort,
    FindSurveyPort,
    FindSurveyItemPort,
    UpdateSurveyItemPort
