package ejbs;

import entities.Category;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.impl.client.deployment.ValidationException;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.DeleterEJB;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;

/**
 * Created by Håvard on 19.11.2016.
 */
@RunWith(Arquillian.class)
public class CategoryEJBTest {
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml")
                .addPackages(true, "ejbs", "entities")
                .addClass(DeleterEJB.class);
    }
    
    @EJB
    CategoryEJB categoryEJB;
    @EJB
    DeleterEJB deleterEJB;
    
    @Before
    @After
    public void emptyDatabase() throws Exception {
        deleterEJB.deleteEntities(Category.class);
    }
    
    @Test
    public void testCreateParentCategory() throws Exception {
        String name = "Arbitrary_category";
        assertTrue(categoryEJB.createCategory(name));
        assertFalse(categoryEJB.createCategory(name));
    }
    
    @Test
    public void testCreateSubCategories() throws Exception {
        String root = "Root";
        String sub1 = "Sub_1";
        
        categoryEJB.createCategory(root);
        assertTrue(categoryEJB.createCategory(sub1, root));
        assertTrue(categoryEJB.createCategory(sub1 + "_1", sub1));
        assertTrue(categoryEJB.createCategory(sub1 + "_2", sub1));
    
        assertFalse(categoryEJB.createCategory(sub1 + "_2", sub1));
        assertFalse(categoryEJB.createCategory("Sub_2", root + "_2"));
    }
    
    @Test
    public void testGetCategory() throws Exception {
        String monthyPython = "Monthy Python";
        categoryEJB.createCategory(monthyPython);
        assertEquals(monthyPython, categoryEJB.getCategory(monthyPython).getName());
        
        String holyGrail = "Holy Grail";
        categoryEJB.createCategory(holyGrail, monthyPython);
        assertEquals(holyGrail, categoryEJB.getCategory(holyGrail).getName());
        assertEquals(monthyPython, categoryEJB.getCategory(holyGrail).getParentCategory().getName());
    
        assertEquals(1, categoryEJB.getCategory(monthyPython).getChildCategories().size());
        assertEquals(holyGrail, categoryEJB.getCategory(monthyPython).getChildCategories().get(0).getName());
    }
    
    @Test
    public void testBadInput() throws Exception {
        try {
            categoryEJB.createCategory("    ");
            fail();
        }
        catch (EJBException e){e.printStackTrace();}
        assertFalse(categoryEJB.createCategory("Bad_sub", "Null"));
    }
}
