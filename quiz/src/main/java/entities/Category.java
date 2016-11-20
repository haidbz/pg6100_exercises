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
    
    @ManyToOne
    private Category parentCategory;
    
    @OneToMany(fetch = FetchType.EAGER)
    private List<Category> childCategories;
    
    @OneToMany
    private List<Quiz> quizes;
    
    public List<Category> getChildCategories() {
        if (childCategories == null)
            childCategories = new ArrayList<>();
        return childCategories;
    }
    
    public List<Quiz> getQuizes() {
        if (quizes == null)
            quizes = new ArrayList<>();
        return quizes;
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
