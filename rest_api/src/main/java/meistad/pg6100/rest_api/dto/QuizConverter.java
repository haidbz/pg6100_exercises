package meistad.pg6100.rest_api.dto;

import entities.Quiz;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Håvard on 26.11.2016.
 */
public class QuizConverter {
    public static QuizDTO transform (Quiz quiz){
        Objects.requireNonNull(quiz);
        
        QuizDTO quizDTO = new QuizDTO(
                quiz.getQuizId(),
                quiz.getQuestion(),
                quiz.getAnswers(),
                quiz.getCorrectAnswer(),
                quiz.getSubSubCategory().getName());
        return quizDTO;
    }
    
    public static List<QuizDTO> transform (List<Quiz> quizzes){
        Objects.requireNonNull(quizzes);
        
        return quizzes.stream()
                .map(QuizConverter::transform)
                .collect(Collectors.toList());
    }
}
