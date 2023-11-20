package no.kristiania.survey;


import no.kristiania.dao.QuestionTypeDao;
import no.kristiania.dao.SaveQuestionType;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;



import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTypeDaoTest {

    private final QuestionTypeDao dao = new QuestionTypeDao(TestDatabase.testDataSource());

    @Test
    void shouldListCorrectQuestionTypesInCorrectOrder() throws SQLException {
        SaveQuestionType saveQuestionType = new SaveQuestionType(dao);

        saveQuestionType.saveQuestionsTypes();

        assertThat(dao.listAll())
                .containsExactly("single", "multi", "scale", "text");
    }
}
