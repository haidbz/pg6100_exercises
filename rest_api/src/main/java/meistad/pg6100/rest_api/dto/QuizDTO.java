package meistad.pg6100.rest_api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Håvard on 25.11.2016.
 */
@ApiModel("A quiz")
public class QuizDTO {
    @ApiModelProperty("The id of the quiz")
    public Long id;
    
    @ApiModelProperty("The question of the quiz")
    public String question;
    
    @ApiModelProperty("The answer options of the quiz")
    public String[] answers;
    
    @ApiModelProperty("The index of the correct answer among the answers")
    public int correctAnswer;
    
    @ApiModelProperty("The category of the quiz")
    public String category;
    
    public QuizDTO() {}
    
    public QuizDTO(long id, String question, String[] answers, int correctAnswer, String category) {
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.category = category;
    }
}
