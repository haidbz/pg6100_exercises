package ejbs;

import entities.Category;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
        // If category exists or the given parent category does not, return false
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
        category.setLevel(setCategoryLevel(category));
        
        entityManager.persist(category);
        return true;
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

    public void deleteCategoryMoveChildren(String name, String newParent){
        getCategory(name).getChildCategories().forEach(child -> {
            getCategory(newParent).getChildCategories().add(child);
            child.setParentCategory(getCategory(newParent));
            setCategoryLevel(child);
        });
        getCategory(name).setChildCategories(null);
        
        deleteSingleCategory(name);
    }

    private int setCategoryLevel(Category category) {
        return setCategoryLevel(category, 0);
    }

    private int setCategoryLevel(Category category, int level) {
        if (category.getParentCategory() == null)
            return level;
        else
            return setCategoryLevel(category.getParentCategory(), level + 1);
    }

    private boolean categoryIsNull(String name) {
        Category category = getCategory(name);
        return category == null;
    }

    private void deleteCategory(String name, boolean recursive) {
        Category category = getCategory(name);
        if (recursive)
            category.getChildCategories().forEach(child -> deleteRecursivelyCategory(child.getName()));
        else
            category.getChildCategories().forEach(child -> {
                child.setParentCategory(null);
                setCategoryLevel(child);
            });
        entityManager.remove(category);
    }
}
