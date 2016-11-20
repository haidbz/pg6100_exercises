package ejbs;

import entities.Category;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void setUp() throws Exception {
        deleterEJB.deleteEntities(Category.class);
    }
    
    @After
    public void tearDown() throws Exception {
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
    
    @Test
    public void testDeleteCategory() throws Exception {
        String root = "root";
        categoryEJB.createCategory(root);
        
        StringBuilder subBuilder = new StringBuilder();
    
        Set<String> subs = generateDummySubCategories(root, 5, 3, subBuilder);
        assertTrue(subs.contains("sub_0"));
        assertTrue(subs.contains("sub_4"));
        assertTrue(subs.contains("sub_0_4"));
        assertTrue(subs.contains("sub_4_0"));
        assertTrue(subs.contains("sub_0_4_0"));
        assertTrue(subs.contains("sub_4_0_4"));
        
        categoryEJB.deleteCategory(root, true);
        List<Category> categories = categoryEJB.getAllCategories();
        
        subs.forEach(s -> assertFalse(categories.contains(s)));
    }
    
    /**
     * Generates dummy categories and writes to database.
     * @param root The name of the root category the generated categories belong to.
     * @param categoriesPerLevel The number of child categories each category has
     * @param levels How many times this method is called recursively.
     * @param builder Required to build the categories' names.
     * @return A list with the names of every category in the order of sub_1 -> sub_1_1 -> sub_1_2 -> sub_2 -> sub_2_1 -> sub_2_2
     */
    private Set<String> generateDummySubCategories(String root, int categoriesPerLevel, int levels, StringBuilder builder) {
        String sub = "sub";
        Set<String> set = new HashSet<>();
        
        for (int i = 0; i < categoriesPerLevel; i++){
            builder.append("_").append(i);
            set.add(builder.toString());
            categoryEJB.createCategory(builder.toString(), root);
            
            if (levels < 0)
                set.addAll(generateDummySubCategories(builder.toString(), categoriesPerLevel, levels - 1, builder));
            
            builder = new StringBuilder(sub);
        }
            
        return set;
    }
}

