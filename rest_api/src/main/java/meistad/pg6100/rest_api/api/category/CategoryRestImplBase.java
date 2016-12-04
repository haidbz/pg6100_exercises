package meistad.pg6100.rest_api.api.category;

import ejbs.CategoryEJB;
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
public class CategoryRestImplBase implements CategoryRestAPIBase {
    @EJB
    CategoryEJB ejb;
    
    @Override
    public List<CategoryDTO> getAll() {
        return null;
    }

    @Override
    public void createCategory(QuizDTO dto) {

    }

    @Override
    public CategoryDTO getById(String name) {
        return null;
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
