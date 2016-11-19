package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Håvard on 04.11.2016.
 */
@Entity
public class Quiz {
    @Id @GeneratedValue
    private Long quizId;
    
    @Column(unique = true)
    @NotBlank
    private String question;
    
    @NotBlank
    private String[] answers;
    
    @Min(value = 0)
    @Max(value = 3)
    private int correctAnswer;
    
    @ManyToOne
    @ParentCategories(parents = 2)
    @NotNull
    private Category subSubCategory;
    
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
