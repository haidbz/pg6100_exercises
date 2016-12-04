package meistad.pg6100.rest_api.api.category.sub_category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meistad.pg6100.rest_api.api.category.sub_sub_category.SubSubCategoryRestAPI;
import meistad.pg6100.rest_api.dto.CategoryDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by haidbz on 04.12.16.
 */
@Api(value = "/sub-categories", description = "Handling of creating, retrieving and answering sub-categories")
@Path("/sub-categories")
@Produces(MediaType.APPLICATION_JSON)
public interface SubCategoryRestAPI extends SubSubCategoryRestAPI {
    @ApiOperation("TODO documentation")
    @GET
    @Path(ID_REFERENCE + "/sub-sub-categories")
    public List<CategoryDTO> getSubSubCategories();
}
