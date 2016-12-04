package meistad.pg6100.rest_api.api.category;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.jaxrs.PATCH;
import meistad.pg6100.rest_api.dto.CategoryDTO;
import meistad.pg6100.rest_api.dto.QuizDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by haidbz on 04.12.16.
 */
public interface CategoryRestAPIBase {
    String NAME_PARAM = "The id and name of the category.";

    String ID = "id";
    String ID_REFERENCE = "/{" + ID +"}";
    String ID_PATH = "/" + ID + ID_REFERENCE;

    @ApiOperation("Get every quiz")
    @GET
    List<CategoryDTO> getAll();

    @ApiOperation("TODO documentation")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "Id of the new category")
    void createCategory(QuizDTO dto);

    @ApiOperation("TODO documentation")
    @GET
    @Path(ID_PATH)
    CategoryDTO getById(
            @ApiParam(NAME_PARAM)
            @PathParam(ID)
                    String name
    );

    @ApiOperation("TODO documentation")
    @PUT
    @Path(ID_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    void replaceById(
            @ApiParam(NAME_PARAM)
            @PathParam(ID)
                    String name,
            @ApiParam("Now, this here will be the new category, y'know laddie?")
                    CategoryDTO dto
    );

    @ApiOperation("TODO documentation")
    @PATCH
    @Path(ID_PATH)
    @Consumes("application/merge-patch+json")
    void updateById(
            @ApiParam(NAME_PARAM)
            @PathParam(ID)
                    String name,
            @ApiParam("The changes expressed here will be applied to the category, yanno.")
                    String json
    );

    @ApiOperation("TODO documentation")
    @DELETE
    @Path(ID_PATH)
    void deleteById(
            @ApiParam(NAME_PARAM)
            @PathParam(ID)
                    String name
    );

    @ApiOperation("TODO documentation")
    @GET
    @Path("/withQuizzes")
    List<CategoryDTO> getCategoriesWithQuizzes();
}
