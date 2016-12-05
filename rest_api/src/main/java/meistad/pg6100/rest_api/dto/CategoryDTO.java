package meistad.pg6100.rest_api.dto;

import java.util.List;

/**
 * Created by haidbz on 04.12.16.
 */
public class CategoryDTO {
    public String name;
    public String parent;
    public List<String> children;
    public List<Long> quizzes;
    public int level;

    public CategoryDTO() {}

    public CategoryDTO(String name, String parent, List<String> children, List<Long> quizzes, int level) {
        this.name = name;
        this.parent = parent;
        this.children = children;
        this.quizzes = quizzes;
        this.level = level;
    }
}
