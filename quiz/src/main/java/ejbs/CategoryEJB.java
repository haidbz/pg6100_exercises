package ejbs;

import entities.Category;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Håvard on 19.11.2016.
 */
@Stateless
public class CategoryEJB {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public CategoryEJB() {}
    
    public boolean createCategory(@NotBlank String name){
        return createCategory(name, null);
    }
    
    public boolean createCategory(@NotBlank String name, String parent){
        if (!categoryIsNull(name) || (parent != null && categoryIsNull(parent)))
            return false;
    
        Category category = new Category();
        category.setName(name);
        category.getChildCategories();
        if (parent != null) {
            Category parentCategory = getCategory(parent);
            category.setParentCategory(parentCategory);
            parentCategory.getChildCategories().add(category);
        }
        
        entityManager.persist(category);
        return true;
    }
    
    private boolean categoryIsNull(String name) {
        Category category = getCategory(name);
        return category == null;
    }
    
    public Category getCategory(String category) {
        return entityManager.find(Category.class, category);
    }
}
