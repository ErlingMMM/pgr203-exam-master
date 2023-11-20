package no.kristiania.survey;

public class QuestionType {
    private long id;
    private String type;


    public QuestionType() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }





    @Override
    public String toString() {
        return "Question type{" +
                "id=" + id +
                ", answer='" + type + '\'' +
                '}';
    }
}
