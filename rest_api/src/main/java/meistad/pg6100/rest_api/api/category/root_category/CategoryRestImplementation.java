package meistad.pg6100.rest_api.api.category.root_category;

import ejbs.CategoryEJB;
import meistad.pg6100.rest_api.dto.CategoryDTO;

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
public class CategoryRestImplementation implements CategoryRestAPI {
    @EJB
    CategoryEJB ejb;

    @Override
    public List<CategoryDTO> getAll() {
        return null;
    }

    @Override
    public void createCategory() {

    }

    @Override
    public CategoryDTO getById() {
        return null;
    }

    @Override
    public void replace() {

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }

    @Override
    public List<CategoryDTO> getCategoriesWithQuizzes() {
        return null;
    }
}
