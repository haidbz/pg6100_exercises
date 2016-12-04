package meistad.pg6100.rest_api.api.category.root_category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import meistad.pg6100.rest_api.api.category.CategoryRestAPIBase;
import meistad.pg6100.rest_api.dto.CategoryDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by haidbz on 04.12.16.
 */
@Api(value = "/categories", description = "Handling of creating, retrieving and answering categories")
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public interface CategoryRestAPI extends CategoryRestAPIBase {
    @ApiOperation("TODO documentation")
    @GET
    @Path(ID_REFERENCE + "/sub-categories")
    public List<CategoryDTO> getSubCategories(
            @ApiParam(NAME_PARAM)
            @PathParam(ID)
                    String name
    );
}
