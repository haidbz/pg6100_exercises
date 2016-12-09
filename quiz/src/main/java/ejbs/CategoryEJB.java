package ejbs;

import entities.Category;
import entities.Quiz;
import org.hibernate.validator.constraints.NotBlank;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Håvard on 19.11.2016.
 */
@Stateless
public class CategoryEJB {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @EJB
    private QuizEJB quizEJB;
    
    public CategoryEJB() {}
    
    public boolean create(@NotBlank String name){
        return create(name, null);
    }
    
    public boolean create(@NotBlank String name, String parent){
        // If category exists or the given parent category does not, return false
        if (isPresent(name) || (parent != null && !isPresent(parent)))
            return false;
    
        Category category = new Category();
        category.setName(name);
        category.getChildCategories();
        if (parent != null) {
            Category parentCategory = get(parent);
            category.setParentCategory(parentCategory);
            parentCategory.getChildCategories().add(category);
        }
        category.setLevel(setCategoryLevel(category));
        
        entityManager.persist(category);
        return true;
    }

    public Category get(String name) {
        return entityManager.find(Category.class, name);
    }

    public List<Category> getAll(){
        return getAll(50);
    }

    public List<Category> getAll(int maxResults){
        Query query = entityManager.createNamedQuery(Category.GET_ALL);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }
    
    public void replace(@NotNull String name, 
                           String parent, 
                           List<String> children, 
                           List<Long> quizzes, 
                           @NotNull int level) throws IllegalArgumentException {
        if (!isPresent(name))
            throw new IllegalArgumentException("The specified category does not exist.");
        if (parent != null && !isPresent(parent))
            throw new IllegalArgumentException("The specified parent category does not exist.");
        
        Category category = get(name);
        category.setName(name);
        category.setParentCategory(entityManager.find(Category.class, parent));

        setChildren(children, category);
        setQuizzes(quizzes, category);

        if (level != setCategoryLevel(category))
            throw new IllegalArgumentException("The given category level does not match actual level.");
        category.setLevel(level);
        
        // Dunno if necessary, just in case
        entityManager.persist(category);
    }

    public void deleteRecursively(String name) {
        delete(name, true);
    }

    public void deleteSingle(String name){
        delete(name, false);
    }

    public void deleteMoveChildren(String name, String newParent){
        get(name).getChildCategories().forEach(child -> {
            get(newParent).getChildCategories().add(child);
            child.setParentCategory(get(newParent));
            setCategoryLevel(child);
        });
        get(name).setChildCategories(null);
        
        deleteSingle(name);
    }

    private void delete(String name, boolean recursive) {
        Category category = get(name);
        if (recursive)
            category.getChildCategories().forEach(child -> deleteRecursively(child.getName()));
        else
            category.getChildCategories().forEach(child -> {
                child.setParentCategory(null);
                setCategoryLevel(child);
            });
        entityManager.remove(category);
    }

    public boolean isPresent(String name) {
        return get(name) != null;
    }

    private void setChildren(List<String> children, Category category) {
        if (children != null) {
            category.setChildCategories(children
                    .stream()
                    .map(child -> {
                        if (!isPresent(child))
                            throw new IllegalArgumentException("A specified child category does not exist.");
                        return get(child);
                    })
                    .collect(Collectors.toList()));
        }
    }

    private void setQuizzes(List<Long> quizzes, Category category) {
        if (quizzes != null) {
            category.setQuizzes(quizzes
                    .stream()
                    .map(quiz -> {
                        if (!quizEJB.isPresent(quiz))
                            throw new IllegalArgumentException("A specified quiz does not exist.");
                        return quizEJB.getQuiz(quiz);
                    })
                    .collect(Collectors.toList()));
        }
    }

    private int setCategoryLevel(Category category) {
        int level = 0;
        Category parent = category.getParentCategory();
        while (true) {
            if (parent == null)
                break;
            level++;
            parent = parent.getParentCategory();
        }
        return level;
    }
/*
        return setCategoryLevel(category, 0);
    }

    */
/**
     * This method should never be used except in {@code setCategoryLevel(Category category)}.
     *//*
 
    private int setCategoryLevel(Category category, int level) {
        if (category.getParentCategory() == null)
            return level;
        else
            return setCategoryLevel(category.getParentCategory(), level + 1);
    }
*/
}
