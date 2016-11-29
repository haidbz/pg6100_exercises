package ejbs;

import entities.Quiz;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.spi.ArquillianProxyException;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.DeleterEJB;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Håvard on 19.11.2016.
 */
@RunWith(value = Arquillian.class)
public class QuizEJBTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml")
                .addPackages(true, "ejbs", "entities")
                .addClass(DeleterEJB.class);
    }
    
    @EJB
    private QuizEJB quizEJB;
    @EJB
    private CategoryEJB categoryEJB;
    @EJB
    private DeleterEJB deleterEJB;
    
    @Before
    public void setUp() throws Exception {
        deleterEJB.deleteEntities(Quiz.class);
        createSubSubCategory();
    }
    
    @After
    public void tearDown() throws Exception {
        deleterEJB.deleteEntities(Quiz.class);
    }
    
    private long createQuiz(String question){
        return quizEJB.createQuiz(question, new String[]{"alpha", "beta", "gamma", "epsilon"}, 0, "Sub");
    }
    
    private void createSubSubCategory(){
        categoryEJB.createCategory("Dom");
        categoryEJB.createCategory("Switch", "Dom");
        categoryEJB.createCategory("Sub", "Switch");
    }
    
    @Test
    public void testCreateQuiz() throws Exception {
        try {
            createQuiz("What is the airspeed velocity of an unladen swallow?");
        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
    
    @Test(expected = ArquillianProxyException.class)
    public void testCreateWithBlankQuestion() throws Exception {
        quizEJB.createQuiz("    ", new String[]{"", "", "", ""}, 0, "Sub");
    }
    
    @Test(expected = ArquillianProxyException.class)
    public void testCreateWithLongQuestion() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 30; i++)
            builder.append("Arbitrary");
        quizEJB.createQuiz(builder.toString(), new String[]{"", "", "", ""}, 0, "Sub");
    }
    
    @Test(expected = ArquillianProxyException.class)
    public void testCreateWithFewAnswers() throws Exception {
        quizEJB.createQuiz("Arbitrary", new String[]{"", "", ""}, 0, "Sub");
    }
    
    @Test(expected = ArquillianProxyException.class)
    public void testCreateWithManyAnswers() throws Exception {
        quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", "", ""}, 0, "Sub");
    }
    
    @Test(expected = ArquillianProxyException.class)
    public void testCreateWithCorrectAnswerOutOfBounds() throws Exception {
        quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", ""}, -1, "Sub");
    }
    
    @Test(expected = EJBException.class)
    public void testCreateWithSubCategory() throws Exception {
        quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", ""}, 0, "Switch");
    }
    
    @Test(expected = EJBException.class)
    public void testCreateWithRootCategory() throws Exception {
        quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", ""}, 0, "Dom");
    }
    
    @Test(expected = EJBException.class)
    public void testCreateWithNonexistantCategory() throws Exception {
        quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", ""}, 0, "Wrong");
    }
    
    @Test
    public void testGetQuiz() throws Exception {
        List<Quiz> list = quizEJB.getAll();
        assertEquals(0, list.size());
        
        String question = "An arbitrary question?";
        long id = createQuiz(question);
    
        assertNotNull(quizEJB.getQuiz(id));
        assertEquals(question, quizEJB.getQuiz(id).getQuestion());
        
        list = quizEJB.getAll();
        assertNotNull(list);
        assertEquals(question, list.get(0).getQuestion());
    }

    @Test
    public void testUpdateQuiz() throws Exception {
        String question = "question";
        String newQuestion = "Another question";
        long id = createQuiz(question);

        assertTrue(quizEJB.updateQuiz(id, newQuestion, new String[]{"alpha", "beta", "gamma", "epsilon"}, 0, "Sub"));
        assertNotEquals(question, quizEJB.getQuiz(id).getQuestion());
        assertEquals(newQuestion, quizEJB.getQuiz(id).getQuestion());
    }

    @Test
    public void testDeleteQuiz() throws Exception {
        List<Quiz> list = quizEJB.getAll();
        assertEquals(0, list.size());
        
        String saveString = "save";
        String deleteString = "delete";
        long saveId = createQuiz(saveString);
        long deleteId = createQuiz(deleteString);
        list = quizEJB.getAll();
        
        assertEquals(saveString, quizEJB.getQuiz(saveId).getQuestion());
        assertEquals(deleteString, quizEJB.getQuiz(deleteId).getQuestion());
        assertEquals(2, list.size());
        
        quizEJB.deleteQuiz(deleteId);
        list = quizEJB.getAll();
        assertNull(quizEJB.getQuiz(deleteId));
        assertEquals(1, list.size());
        assertEquals(saveString, list.get(0).getQuestion());
    }
}
