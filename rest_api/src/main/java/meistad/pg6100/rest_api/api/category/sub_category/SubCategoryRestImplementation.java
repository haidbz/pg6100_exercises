package meistad.pg6100.rest_api.api.category.sub_category;

import io.swagger.annotations.ApiParam;
import meistad.pg6100.rest_api.api.category.CategoryRestImplBase;
import meistad.pg6100.rest_api.dto.CategoryDTO;
import meistad.pg6100.rest_api.dto.QuizDTO;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Created by haidbz on 04.12.16.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SubCategoryRestImplementation extends CategoryRestImplBase implements SubCategoryRestAPI {
    @Override
    public List<CategoryDTO> getAll() {
        return null;
    }

    @Override
    public List<CategoryDTO> getSubSubCategories() {
        return null;
    }

    @Override
    public CategoryDTO getParent(@ApiParam(NAME_PARAM) String name) {
        return null;
    }
}
