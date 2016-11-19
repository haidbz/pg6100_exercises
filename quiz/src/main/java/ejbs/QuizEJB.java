package ejbs;

import entities.Category;
import entities.Quiz;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Håvard on 19.11.2016.
 */
@Stateless
public class QuizEJB {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @EJB
    private CategoryEJB categoryEJB;
    
    public QuizEJB() {}
    
    public void createQuiz(@NotBlank @Size(max = 255) String question, @Size(min = 4, max = 4) String[] answers,
                           @Min(value = 0) @Max(value = 3) int correctAnswer, String category){
        Quiz quiz = new Quiz();
        quiz.setQuestion(question);
        quiz.setAnswers(answers);
        quiz.setCorrectAnswer(correctAnswer);
        quiz.setSubSubCategory(categoryEJB.getCategory(category));
        
        entityManager.persist(quiz);
    }
}
