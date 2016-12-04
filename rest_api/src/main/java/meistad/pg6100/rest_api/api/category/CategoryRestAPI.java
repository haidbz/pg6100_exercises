package meistad.pg6100.rest_api.api.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import meistad.pg6100.rest_api.dto.CategoryDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by haidbz on 04.12.16.
 */
@Api(value = "/categories", description = "Handling of creating, retrieving and answering categories")
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public interface CategoryRestAPI {
    @ApiOperation("Get every category")
    @GET
    List<CategoryDTO> getAll();
    
    void createCategory();
    
    CategoryDTO getById();
    
    void replace();

    void update();
    
    void delete();
    
    List<CategoryDTO> getCategoriesWithQuizzes();
}