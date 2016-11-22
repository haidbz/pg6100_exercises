package ejbs;

import entities.Category;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;

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
    
    public List<Category> getAllCategories(){
        return getAllCategories(50);
    }
    
    public List<Category> getAllCategories(int maxResults){
        Query query = entityManager.createNamedQuery(Category.GET_ALL);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }
    
    public void deleteCategory(String name){
        Category category = getCategory(name);
        
        entityManager.remove(category);
    }
    
    // TODO remove and have deletion of category always delete all sub-categories. Or maybe figure out why it doesnot delete.
    public void deleteCategoryMoveChildren(String name, String newParent){
        Category deleteCategory = getCategory(name);
        Category parentCategory = getCategory(newParent);
        
        parentCategory.getChildCategories().addAll(deleteCategory.getChildCategories());
        deleteCategory.setChildCategories(null);
        
//        deleteCategory(name);
        entityManager.remove(deleteCategory);
    }
}
