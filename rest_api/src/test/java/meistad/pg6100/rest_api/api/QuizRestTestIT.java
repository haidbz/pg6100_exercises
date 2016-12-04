package meistad.pg6100.rest_api.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
public class QuizRestTestIT {

    private static final String VALID_QUESTION = "A question";
    private static final String[] VALID_ANSWERS = new String[]{"", "", "", ""};
    private static final int VALID_CORRECT_ANSWER = 2;
    private static final String VALID_CATEGORY = "SubSub";

    @BeforeClass
    public static void initClass() {
        JBossUtil.waitForJBoss(10);

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/rest_api/api/quiz";
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
    public void testBadInputCreate() throws Exception {
        QuizDTO dto = new QuizDTO(null, VALID_QUESTION, VALID_ANSWERS, VALID_CORRECT_ANSWER, VALID_CATEGORY);
        
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
}