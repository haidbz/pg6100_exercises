package entities;

import javax.persistence.Entity;

/**
 * Created by Håvard on 04.11.2016.
 */
@Entity
public class Quiz {
    String[] answers;
    int correctAnswer;
    Category subSubCategory;
}
