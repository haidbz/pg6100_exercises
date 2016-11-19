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
    @After
    public void emptyDatabase() throws Exception {
        deleterEJB.deleteEntities(Quiz.class);
    }
    
    private void createQuiz(String question){
        createSubSubCategory();
        quizEJB.createQuiz(question, new String[]{"alpha", "beta", "gamma", "epsilon"}, 0, "Sub");
    }
    
    private void createSubSubCategory(){
        categoryEJB.createCategory("Dom");
        categoryEJB.createCategory("Switch", "Dom");
        categoryEJB.createCategory("Sub", "Switch");
    }
    
    @Test
    public void testCanCreateQuiz() throws Exception {
        try {
            createQuiz("What is the air airspeed velocity of an unladen swallow?");
        }
        catch (Exception e){
            fail();
        }
    }
    
}
