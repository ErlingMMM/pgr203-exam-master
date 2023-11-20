package no.kristiania.survey;

public class UserAnswer {
    private long id;
    private long answerId;
    private long questionId;
    private long userId;
    private long userSurveyId;
    private String value;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserSurveyId() {
        return userSurveyId;
    }

    public void setUserSurveyId(long userSurveyId) {
        this.userSurveyId = userSurveyId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
