package no.kristiania.survey;

public class Answer {
    private long id;
    private String text;
    private long questionId;

    public Answer(String text, long questionId) {
        this.text = text;
        this.questionId = questionId;
    }

    public Answer() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", questionId=" + questionId +
                '}';
    }
}