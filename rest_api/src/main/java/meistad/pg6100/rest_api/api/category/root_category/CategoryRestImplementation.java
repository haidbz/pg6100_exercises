package meistad.pg6100.rest_api.api.category.root_category;

import ejbs.CategoryEJB;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import meistad.pg6100.rest_api.api.category.CategoryRestImplBase;
import meistad.pg6100.rest_api.dto.CategoryConverter;
import meistad.pg6100.rest_api.dto.CategoryDTO;
import meistad.pg6100.rest_api.dto.QuizDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Created by haidbz on 04.12.16.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CategoryRestImplementation extends CategoryRestImplBase implements CategoryRestAPI {
    @ApiOperation("Get all categories")
    @Override
    public List<CategoryDTO> getAll() {
        return CategoryConverter.transform(ejb.getAllCategories(100));
    }

    @ApiOperation("Create a new root category. Will ignore all but the name field.")
    @Override
    public void createCategory(CategoryDTO dto) {
        dto.parent = null;
        super.createCategory(dto);
    }

    @Override
    public List<CategoryDTO> getSubCategories(@ApiParam(NAME_PARAM) String name) {
        return null;
    }
}
