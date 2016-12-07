package meistad.pg6100.rest_api.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import meistad.pg6100.rest_api.api.quiz.QuizRestAPI;
import meistad.pg6100.rest_api.dto.CategoryDTO;
import meistad.pg6100.rest_api.dto.QuizDTO;
import meistad.pg6100.utils.JBossUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by haidbz on 29.11.16.
 */
public class CategoryRestTestIT {
    
    private static final String VALID_QUESTION = "A question";
    private static final String[] VALID_ANSWERS = new String[]{"a", "b", "c", "d"};
    private static final int VALID_CORRECT_ANSWER = 2;
    private static final String VALID_SUB_SUB_CATEGORY = "SubSub";
    private static final String VALID_ROOT = "root";
    
    @BeforeClass
    public static void initClass() {
        JBossUtil.waitForJBoss(10);

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/rest_api/api/category";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Before
    public void setUp() throws Exception {
        clean();
    }

    @After
    public void tearDown() throws Exception {
        clean();
    }

    private void clean() {
        List<QuizDTO> list = Arrays.asList(given()
                .accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .as(QuizDTO[].class));

        list.forEach(dto -> given()
                .pathParam(QuizRestAPI.ID, dto.id)
                .delete(QuizRestAPI.PATH_ID)
                .then()
                .statusCode(204));

        get().then()
                .statusCode(200)
                .body("size()", is(0));
    }
    
    @Test
    public void testCreateRootCategory() throws Exception {
        CategoryDTO dto = createDummyRootCategoryDTO();
        
        get().then().statusCode(200).body("size()", is(0));
        
        String name = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract()
                .asString();
    }
    
/*
    @Test
    public void testBadInputCreate() throws Exception {
        QuizDTO dto = createDummyQuizDTO();
    
        get().then().statusCode(200).body("size()", is(0));

        try {
            given()
                    .contentType(ContentType.JSON)
                    .body(dto)
                    .post()
                    .then()
                    .statusCode(500);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        
        
    }
*/
    
    private CategoryDTO createDummyRootCategoryDTO() {
        CategoryDTO dto = new CategoryDTO();
        dto.name = VALID_ROOT;
        return dto;
    }
    
    private CategoryDTO createDummySubCategoryDTO() {
        return null;
    }
    
    private CategoryDTO createDummySubSubCategoryDTO() {
        return null;
    }
    
    private QuizDTO createDummyQuizDTO() {
        return new QuizDTO(null, VALID_QUESTION, VALID_ANSWERS, VALID_CORRECT_ANSWER, VALID_SUB_SUB_CATEGORY);
    }
}
