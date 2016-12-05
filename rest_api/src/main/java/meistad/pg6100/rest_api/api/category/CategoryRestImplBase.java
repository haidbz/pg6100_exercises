package meistad.pg6100.rest_api.api.category;

import com.google.common.base.Throwables;
import ejbs.CategoryEJB;
import meistad.pg6100.rest_api.dto.CategoryConverter;
import meistad.pg6100.rest_api.dto.CategoryDTO;
import meistad.pg6100.rest_api.dto.QuizDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Created by haidbz on 04.12.16.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public abstract class CategoryRestImplBase implements CategoryRestAPIBase {
    @EJB
    protected CategoryEJB ejb;
    
    @Override
    public void createCategory(CategoryDTO dto) {
        if (ejb.isPresent(dto.name))
            throw new WebApplicationException("That category already exists", 400);
        if (dto.parent != null && !ejb.isPresent(dto.parent))
            throw new WebApplicationException("The given parent category does not exist", 400);
        ejb.createCategory(dto.name, dto.parent);
    }

    @Override
    public CategoryDTO getById(String name) {
        return CategoryConverter.transform(ejb.getCategory(name));
    }

    @Override
    public void replaceById(String name, CategoryDTO dto) {

    }

    @Override
    public void updateById(String name, String json) {

    }

    @Override
    public void deleteById(String name) {

    }

    @Override
    public List<CategoryDTO> getCategoriesWithQuizzes() {
        return null;
    }
}
