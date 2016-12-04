package meistad.pg6100.rest_api.api.quiz;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.jaxrs.PATCH;
import meistad.pg6100.rest_api.dto.QuizDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Håvard on 26.11.2016.
 */
@Api(value = "/quiz", description = "Handling of creating, retrieving and answering quizzes")
@Path("/quiz")
@Produces(MediaType.APPLICATION_JSON)
public interface QuizRestAPI {
    String ID_PARAM = "The id of the quiz.";

    String ID = "id";
    String PATH_ID = "/" + ID + "/{" + ID + "}";

    @ApiOperation("Get every quiz")
    @GET
    List<QuizDTO> getAll();

    @ApiOperation("Create new quiz")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Id of the new quiz")
    long createQuiz(
            @ApiParam("Question, answer options as well as correct answer and category of the quiz. Should not specify id.")
                    QuizDTO dto
    );

    @ApiOperation("Get a single quiz corresponding to the given id")
    @GET
    @Path(PATH_ID)
    QuizDTO getById(
            @ApiParam(ID_PARAM)
            @PathParam(ID)
                    long id
    );

    @ApiOperation("Replace a quiz with another with the same id")
    @PUT
    @Path(PATH_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    void replaceById(
            @ApiParam(ID_PARAM)
            @PathParam(ID)
                    long id,
            @ApiParam("The quiz that will replace the old one, must have the same id.")
                    QuizDTO dto
    );

    @ApiOperation("Update an existing quiz")
    @PATCH
    @Path(PATH_ID)
    @Consumes("application/merge-patch+json")
    void updateById (
            @ApiParam(ID_PARAM)
            @PathParam(ID)
                    long id,
            @ApiParam("Json patch for quiz. Id cannot be changed.")
                    String jsonPatch
    );

    @ApiOperation("Delete a quiz")
    @DELETE
    @Path(PATH_ID)
    void deleteById (
            long id
    );
}
