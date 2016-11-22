package validators;

import entities.Category;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Håvard on 19.11.2016.
 */
class CategoriesValidator implements ConstraintValidator<ParentCategories, Category> {
   private int parentCategories;
   
   @Override
   public void initialize(ParentCategories constraint) {
      parentCategories = constraint.parents();
   }

   @Override
   public boolean isValid(Category base, ConstraintValidatorContext context) {
      return checkSuperCategoryCount(base, parentCategories);
   }
   
   private boolean checkSuperCategoryCount(Category category, int expectedParents){
      boolean actualRoot = category.getParentCategory() == null;
      boolean expectedRoot = expectedParents == 0;
      // If the category is expected to have a parent and it does, call this method recursively
      if (!(actualRoot || expectedRoot))
         return checkSuperCategoryCount(category.getParentCategory(), expectedParents - 1);
      return actualRoot && expectedRoot;
   }
}
