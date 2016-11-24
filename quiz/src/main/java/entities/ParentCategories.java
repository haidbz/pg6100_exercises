package entities;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Håvard on 19.11.2016.
 */
@Constraint(validatedBy = CategoriesValidator.class)
@Target({
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParentCategories {
    String message() default "Not the expected amount of super categories.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    int parents() default 0;
}
