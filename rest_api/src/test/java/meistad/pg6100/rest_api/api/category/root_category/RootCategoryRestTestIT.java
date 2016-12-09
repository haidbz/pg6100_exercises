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

        postDummy(dto);

        get().then()
                .statusCode(200)
                .body("size()", is(1));
        
        given().pathParam(CategoryRestAPI.ID, dto.name)
                .get(CategoryRestAPI.ID_PATH)
                .then()
                .statusCode(200)
                .body("name", is(VALID_ROOT_CATEGORY));
    }

    @Test
    public void testReplaceRoot() throws Exception {
        CategoryDTO dto = createDummySubSubCategoryDTO();
        postDummy(dto);

        String replacement = "Replacement";
        dto.name = replacement;
        given().contentType(ContentType.JSON)
                .pathParam(CategoryRestAPI.ID, VALID_SUB_SUB_CATEGORY)
                .body(dto)
                .put(CategoryRestAPI.ID_PATH)
                .then()
                .statusCode(204);

        given().pathParam(CategoryRestAPI.ID, replacement)
                .get(CategoryRestAPI.ID_PATH)
                .then()
                .statusCode(200)
                .body("name", is(dto.name));
    }

    private void postDummy(CategoryDTO dto) {
        given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(204);
    }

}
