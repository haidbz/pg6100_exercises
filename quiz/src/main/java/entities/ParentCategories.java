package entities;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Håvard on 19.11.2016.
 */
@Constraint(validatedBy = CategoryLevelValidator.class)
@Target({
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParentCategories {
    String message() default "The category is not of the expected category level.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    int categoryLevel() default 0;
}
