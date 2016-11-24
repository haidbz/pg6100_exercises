package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Håvard on 04.11.2016.
 */
@NamedQueries({
        @NamedQuery(name = Category.GET_ALL, query = "SELECT c FROM Category c")
})
@Entity
public class Category {
    public final static String GET_ALL = "GET_ALL_CATEGORIES";
    
    @Id
    @NotBlank
    private String name;
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Category parentCategory;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentCategory")
    private List<Category> childCategories;
    
    @OneToMany(mappedBy = "subSubCategory")
    private List<Quiz> quizzes;
    
    @Override
    public boolean equals(Object o){
        if (o instanceof Category)
            return getName().equals(((Category) o).getName());
        else
            return o instanceof String && getName().equals(o);
    }
    
    public List<Category> getChildCategories() {
        if (childCategories == null)
            setChildCategories(new ArrayList<>());
        return childCategories;
    }
    
    public List<Quiz> getQuizzes() {
        if (quizzes == null)
            quizzes = new ArrayList<>();
        return quizzes;
    }
    
    public void setChildCategories(List<Category> childCategories) {
        this.childCategories = childCategories;
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
}
