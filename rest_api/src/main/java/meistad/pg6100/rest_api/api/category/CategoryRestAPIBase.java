package meistad.pg6100.rest_api.api.category;

import io.swagger.annotations.ApiOperation;
import meistad.pg6100.rest_api.dto.CategoryDTO;

import javax.ws.rs.GET;
import java.util.List;

/**
 * Created by haidbz on 04.12.16.
 */
public interface CategoryRestAPIBase {
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
