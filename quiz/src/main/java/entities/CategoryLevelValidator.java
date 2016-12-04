package entities;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by haidbz on 04.12.16.
 */
public class CategoryLevelValidator implements ConstraintValidator<ParentCategories, Category> {
    private int categoryLevels;
    
    @Override
    public void initialize(ParentCategories constraintAnnotation) {
        categoryLevels = constraintAnnotation.categoryLevel();
    }

    @Override
    public boolean isValid(Category category, ConstraintValidatorContext context) {
        return category.getLevel() == categoryLevels;
    }
}
