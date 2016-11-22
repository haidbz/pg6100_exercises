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
    
    public void deleteRecursivelyCategory(String name) {
        deleteCategory(name, true);
    }
    
    public void deleteSingleCategory(String name){
        deleteCategory(name, false);
    }
    
    private void deleteCategory(String name, boolean recursive) {
        Category category = getCategory(name);
        if (recursive)
            category.getChildCategories().forEach(child -> deleteRecursivelyCategory(child.getName()));
        else 
            category.getChildCategories().forEach(child -> child.setParentCategory(null));
        entityManager.remove(category);
    }
    
    public void deleteCategoryMoveChildren(String name, String newParent){
        getCategory(name).getChildCategories().forEach(child -> {
            getCategory(newParent).getChildCategories().add(child);
            child.setParentCategory(getCategory(newParent));
        });
        getCategory(name).setChildCategories(null);
        
        deleteSingleCategory(name);
    }
}
