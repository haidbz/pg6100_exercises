package meistad.pg6100.rest_api.api.category.root_category;

import ejbs.CategoryEJB;
import io.swagger.annotations.ApiParam;
import meistad.pg6100.rest_api.api.category.CategoryRestImplBase;
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
    @Override
    public List<CategoryDTO> getSubCategories(@ApiParam(NAME_PARAM) String name) {
        return null;
    }
}
