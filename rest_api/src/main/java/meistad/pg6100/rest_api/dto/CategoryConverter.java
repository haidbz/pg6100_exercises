package meistad.pg6100.rest_api.dto;

import entities.Category;
import entities.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by haidbz on 04.12.16.
 */
public class CategoryConverter {
    public static CategoryDTO transform (Category category) {
        Objects.requireNonNull(category);

        CategoryDTO dto = new CategoryDTO();
        dto.name = category.getName();
        dto.level = category.getLevel();
        
        if (category.getParentCategory() != null)
            dto.parent = category.getParentCategory().getName();
        
        dto.children = category
                .getChildCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        
        dto.quizzes = category
                .getQuizzes()
                .stream()
                .map(Quiz::getId)
                .collect(Collectors.toList());
        
        return dto;
    }
    
    public static List<CategoryDTO> transform (List<Category> categories) {
        Objects.requireNonNull(categories);
        
        return categories.stream()
                .map(CategoryConverter::transform)
                .collect(Collectors.toList());
    }
}
