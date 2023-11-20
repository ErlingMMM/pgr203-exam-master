package no.kristiania.dao;

import no.kristiania.survey.QuestionType;

import java.sql.SQLException;

public class SaveQuestionType {
    private QuestionTypeDao questionTypeDao;

    public SaveQuestionType(QuestionTypeDao questionTypeDao) {
        this.questionTypeDao = questionTypeDao;
    }


    public void saveQuestionsTypes() throws SQLException {
        StringBuilder responseText = new StringBuilder();

        int value = 1;
        for (String question : questionTypeDao.listAll()) responseText.append(value++);
        if (value >= 5) return;  //We will never have more than 4 types

        QuestionType questionType = new QuestionType();
        questionType.setType("single");
        questionType.setId(1);
        questionTypeDao.save(questionType);
        questionType.setType("multi");
        questionType.setId(2);
        questionTypeDao.save(questionType);
        questionType.setType("scale");
        questionType.setId(3);
        questionTypeDao.save(questionType);
        questionType.setType("text");
        questionType.setId(4);
        questionTypeDao.save(questionType);
    }
}


