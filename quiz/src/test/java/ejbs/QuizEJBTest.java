package ejbs;

import entities.Quiz;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.DeleterEJB;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import java.util.ArrayList;
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
    
    @Test
    public void testBadInput() throws Exception {
        try {
            quizEJB.createQuiz("    ", new String[]{"", "", "", ""}, 0, "Sub");
            fail();
        }
        catch (EJBException ignored){
            
        }
    
        try {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 30; i++)
                builder.append("Arbitrary");
            quizEJB.createQuiz(builder.toString(), new String[]{"", "", "", ""}, 0, "Sub");
            fail();
        }
        catch (EJBException ignored){}
    
        try {
            quizEJB.createQuiz("Arbitrary", new String[]{"", "", ""}, 0, "Sub");
            fail();
        }
        catch (EJBException ignored){}
    
        try {
            quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", "", ""}, 0, "Sub");
            fail();
        }
        catch (EJBException ignored){}
    
        try {
            quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", ""}, -1, "Sub");
            fail();
        }
        catch (EJBException e){}
    
        try {
            quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", ""}, 5, "Sub");
            fail();
        }
        catch (EJBException e){}
    
        try {
            quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", ""}, 0, "Switch");
            fail();
        }
        catch (EJBException e){}
    
        try {
            quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", ""}, 0, "Root");
            fail();
        }
        catch (EJBException e){}
    
        try {
            quizEJB.createQuiz("Arbitrary", new String[]{"", "", "", ""}, 0, "Wrong");
            fail();
        }
        catch (EJBException e){}
    }
    
    @Test
    public void testGetQuiz() throws Exception {
        List<Quiz> list = quizEJB.getAllQuizes(5);
        assertEquals(0, list.size());
        
        String question = "An arbitrary question?";
        long id = createQuiz(question);
    
        assertNotNull(quizEJB.getQuiz(id));
        assertEquals(question, quizEJB.getQuiz(id).getQuestion());
        
        list = quizEJB.getAllQuizes(5);
        assertNotNull(list);
        assertEquals(question, list.get(0).getQuestion());
    }
}
