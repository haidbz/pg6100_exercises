package meistad.pg6100.rest_api.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import meistad.pg6100.rest_api.api.category.root_category.CategoryRestAPI;
import meistad.pg6100.rest_api.dto.CategoryDTO;
import meistad.pg6100.rest_api.dto.QuizDTO;
import meistad.pg6100.utils.JBossUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;

/**
 * Created by haidbz on 08.12.16.
 */
public abstract class RestTestBase {
    private static final String API_BASE_PATH = "/rest_api/api";
    protected static final String PATH_CATEGORIES = "/categories";
    private static final String PATH_SUB_CATEGORIES = "/sub-categories";
    private static final String PATH_SUB_SUB_CATEGORIES = "/sub-sub-categories";
    protected static final String VALID_ROOT_CATEGORY = "root";
    protected static final String VALID_SUB_CATEGORY = "Sub";
    protected static final String VALID_SUB_SUB_CATEGORY = "SubSub";
    protected static final String VALID_QUESTION = "A question";
    protected static final String[] VALID_ANSWERS = new String[]{"a", "b", "c", "d"};
    protected static final int VALID_CORRECT_ANSWER = 2;

    protected static void initRestAssured(final String basePath) {
        JBossUtil.waitForJBoss(10);

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = API_BASE_PATH + basePath;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected void clean() {
        List<CategoryDTO> list = Arrays.asList(given()
                .accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .as(CategoryDTO[].class));

        list.forEach(dto -> given()
                .pathParam(CategoryRestAPI.ID, dto.name)
                .delete(CategoryRestAPI.ID_PATH)
                .then()
                .statusCode(204));

        get().then()
                .statusCode(200)
                .body("size()", is(0));
    }

    protected CategoryDTO createDummyRootCategoryDTO() {
        CategoryDTO dto = new CategoryDTO();
        dto.name = VALID_ROOT_CATEGORY;
        dto.level = 0;
        return dto;
    }

    protected CategoryDTO createDummySubCategoryDTO() {
        if (!Arrays
                .stream(given()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(CategoryDTO[].class)
                ).map(categoryDTO -> categoryDTO.name)
                .collect(Collectors.toList())
                .contains(VALID_ROOT_CATEGORY)
            ) {
            given().basePath(API_BASE_PATH + PATH_CATEGORIES)
                    .contentType(ContentType.JSON)
                    .body(createDummyRootCategoryDTO())
                    .post()
                    .then()
                    .statusCode(204);
        }
        
        CategoryDTO dto = new CategoryDTO();
        dto.name = VALID_SUB_CATEGORY;
        dto.parent = VALID_ROOT_CATEGORY;
        dto.level = 1;
        return dto;
    }

    protected CategoryDTO createDummySubSubCategoryDTO() {
        if (!Arrays
                .stream(given()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(CategoryDTO[].class)
                ).map(categoryDTO -> categoryDTO.name)
                .collect(Collectors.toList())
                .contains(VALID_SUB_CATEGORY)
                ) {
            given().basePath(API_BASE_PATH + PATH_SUB_CATEGORIES)
                    .contentType(ContentType.JSON)
                    .body(createDummySubCategoryDTO())
                    .post()
                    .then()
                    .statusCode(204);
        }

        CategoryDTO dto = new CategoryDTO();
        dto.name = VALID_SUB_SUB_CATEGORY;
        dto.parent = VALID_SUB_CATEGORY;
        dto.level = 2;
        return dto;
    }

    protected QuizDTO createDummyQuizDTO() {
        if (!Arrays
                .stream(given()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(CategoryDTO[].class)
                ).map(categoryDTO -> categoryDTO.name)
                .collect(Collectors.toList())
                .contains(VALID_SUB_SUB_CATEGORY)
                ) {
            given().basePath(API_BASE_PATH + PATH_SUB_SUB_CATEGORIES)
                    .contentType(ContentType.JSON)
                    .body(createDummySubSubCategoryDTO())
                    .post()
                    .then()
                    .statusCode(204);
        }

        return new QuizDTO(null, VALID_QUESTION, VALID_ANSWERS, VALID_CORRECT_ANSWER, VALID_SUB_SUB_CATEGORY);
    }
}
