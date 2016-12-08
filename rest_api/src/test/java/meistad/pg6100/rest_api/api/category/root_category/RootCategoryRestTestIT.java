package meistad.pg6100.rest_api.api.category.root_category;

import io.restassured.http.ContentType;
import meistad.pg6100.rest_api.api.RestTestBase;
import meistad.pg6100.rest_api.dto.CategoryDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by haidbz on 29.11.16.
 */
public class RootCategoryRestTestIT extends RestTestBase {

    @BeforeClass
    public static void initClass() {
        initRestAssured(PATH_CATEGORIES);
    }

    @Before
    public void setUp() throws Exception {
        clean();
    }

    @After
    public void tearDown() throws Exception {
        clean();
    }

    @Test
    public void testCreateRootCategory() throws Exception {
        CategoryDTO dto = createDummyRootCategoryDTO();
        
        get().then()
                .statusCode(200)
                .body("size()", is(0));
        
        given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(204);

        get().then()
                .statusCode(200)
                .body("size()", is(1));
        
        given().pathParam(CategoryRestAPI.ID, dto.name)
                .get(CategoryRestAPI.ID_PATH)
                .then()
                .statusCode(200)
                .body("name", is(VALID_ROOT_CATEGORY));
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

}
