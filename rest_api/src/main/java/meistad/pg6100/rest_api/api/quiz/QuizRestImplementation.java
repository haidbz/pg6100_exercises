package meistad.pg6100.rest_api.api.quiz;

import com.google.common.base.Throwables;
import ejbs.QuizEJB;
import io.swagger.annotations.ApiParam;
import meistad.pg6100.rest_api.dto.QuizConverter;
import meistad.pg6100.rest_api.dto.QuizDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Created by Håvard on 26.11.2016.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class QuizRestImplementation implements QuizRestAPI {
    @EJB
    private QuizEJB ejb;
    
    @Override
    public List<QuizDTO> getAll() {
        return QuizConverter.transform(ejb.getAll(100));
    }
    
    @Override
    public long createQuiz(QuizDTO dto) {
        if (dto.id != null)
            throw new WebApplicationException("Cannot specify id when creating a new quiz", 400);
        
        long id;
        try {
            id = ejb.createQuiz(dto.question, dto.answers, dto.correctAnswer, dto.category);
        }
        catch (Exception e){
            e.printStackTrace();
            throw wrapException(e);
        }
        return id;
    }
    
    @Override
    public QuizDTO getById(@ApiParam(ID_PARAM) long id) {
        return QuizConverter.transform(ejb.getQuiz(id));
    }
    
    @Override
    public void replaceById(@ApiParam(ID_PARAM) long id, QuizDTO dto) {
        if (id != dto.id)
            throw new WebApplicationException("May not change the id.", 409);

        if (! ejb.isPresent(id))
            throw new WebApplicationException("Cannot create new quiz with put.", 404);

        try {
            ejb.updateQuiz(id, dto.question, dto.answers, dto.correctAnswer, dto.category);
        }
        catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void updateById(@ApiParam(ID_PARAM) long id, @ApiParam("Json patch for quiz. Id cannot be changed.") String jsonPatch) {

    }

    @Override
    public void deleteById(long id) {

    }

    //----------------------------------------------------------
    
    private WebApplicationException wrapException(Exception e) throws WebApplicationException{

        /*
            Errors:
            4xx: the user has done something wrong, eg asking for something that does not exist (404)
            5xx: internal server error (eg, could be a bug in the code)
         */
        
        Throwable cause = Throwables.getRootCause(e);
        if(cause instanceof ConstraintViolationException){
            return new WebApplicationException("Invalid constraints on input: "+cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
