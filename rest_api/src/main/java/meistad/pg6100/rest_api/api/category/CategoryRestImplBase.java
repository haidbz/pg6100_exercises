package meistad.pg6100.rest_api.api.category;

import ejbs.CategoryEJB;
import meistad.pg6100.rest_api.dto.CategoryConverter;
import meistad.pg6100.rest_api.dto.CategoryDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
        ejb.create(dto.name, dto.parent);
    }

    @Override
    public CategoryDTO getById(String name) {
        return CategoryConverter.transform(ejb.get(name));
    }

    @Override
    public void replaceById(String name, CategoryDTO dto) {
        if (!ejb.isPresent(name))
            throw new WebApplicationException("That category doesn't exist.", 404);
/*
        if (!name.equals(dto.name))
            throw new WebApplicationException("The names given do not match.", 400);
*/
        try {
            ejb.replace(dto.name, dto.parent, dto.children, dto.quizzes, dto.level);
        }
        catch (IllegalArgumentException e){
            if (e.getMessage().contains("level"))
                throw new WebApplicationException(e.getMessage(), 400);
            throw new WebApplicationException(e.getMessage(), 404);
        }
    }

    @Override
    public void updateById(String name, String json) {

    }

    @Override
    public void deleteById(String name) {
        ejb.deleteRecursively(name);
    }

    @Override
    public List<CategoryDTO> getCategoriesWithQuizzes() {
        return null;
    }
}
