package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Håvard on 04.11.2016.
 */
@Entity
public class Category {
    @Id
    @NotBlank
    private String name;
    
    @ManyToOne
    private Category parentCategory;
    
    @OneToMany
    private Category childCategories;
    
    @OneToMany
    private List<Quiz> quizzes;
    
    public List<Quiz> getQuizzes() {
        if (quizzes == null)
            quizzes = new ArrayList<>();
        return quizzes;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Category getParentCategory() {
        return parentCategory;
    }
    
    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
    
    public Category getChildCategories() {
        return childCategories;
    }
    
    public void setChildCategories(Category childCategories) {
        this.childCategories = childCategories;
    }
}
