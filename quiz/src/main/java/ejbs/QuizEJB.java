package ejbs;

import entities.Quiz;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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
    
    public long createQuiz(@NotBlank @Size(max = 255) String question, @Size(min = 4, max = 4) String[] answers,
                           @Min(value = 0) @Max(value = 3) int correctAnswer, String category){
        Quiz quiz = new Quiz();
        quiz.setQuestion(question);
        quiz.setAnswers(answers);
        quiz.setCorrectAnswer(correctAnswer);
        quiz.setSubSubCategory(categoryEJB.get(category));
        
        entityManager.persist(quiz);
        
        return quiz.getId();
    }

    public Quiz getQuiz(long id) {
        return entityManager.find(Quiz.class, id);
    }
    
    public List<Quiz> getAll(){
        return getAll(50);
    }
    
    public List<Quiz> getAll(int maxResults){
        Query query = entityManager.createNamedQuery(Quiz.GET_ALL);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    public boolean isPresent (long id) {
        return getQuiz(id) != null;
    }

    public boolean updateQuiz (@NotNull long id, String question, String[] answers, int correctAnswer, String category) {
        Quiz quiz = getQuiz(id);
        if (quiz == null)
            return false;
        quiz.setQuestion(question);
        quiz.setAnswers(answers);
        quiz.setCorrectAnswer(correctAnswer);
        quiz.setSubSubCategory(categoryEJB.get(category));
        return true;
    }
    
    public void deleteQuiz (long id){
        Quiz quiz = getQuiz(id);
        entityManager.remove(quiz);
    }
}
