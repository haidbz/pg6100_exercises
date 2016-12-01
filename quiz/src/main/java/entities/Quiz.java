package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Håvard on 04.11.2016.
 */
@NamedQueries({
        @NamedQuery(name = Quiz.GET_ALL, query = "SELECT q FROM Quiz q")
})
@Entity
public class Quiz {
    public static final String GET_ALL = "GET_ALL_QUIZZES";
    
    @Id @GeneratedValue(strategy = GenerationType.TABLE)
    private Long quizId;
    
    @Column(unique = true)
    @Size(max = 255)
    @NotBlank
    private String question;
    
    @Size(min = 4, max = 4)
    private String[] answers;
    
    @Min(value = 0)
    @Max(value = 3)
    private int correctAnswer;
    
    @ManyToOne
    @NotNull
    @ParentCategories(parents = 2)
    private Category subSubCategory;
    
    public Long getId() {
        return quizId;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String[] getAnswers() {
        return answers;
    }
    
    public void setAnswers(String[] answers) {
        this.answers = answers;
    }
    
    public int getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    public Category getSubSubCategory() {
        return subSubCategory;
    }
    
    public void setSubSubCategory(Category subSubCategory) {
        this.subSubCategory = subSubCategory;
    }
}
