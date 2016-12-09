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

import java.util.*;

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
        assertTrue(categoryEJB.create(name));
        assertFalse(categoryEJB.create(name));
    }
    
    @Test
    public void testCreateSubCategories() throws Exception {
        String root = "Root";
        String sub1 = "Sub_1";
        
        categoryEJB.create(root);
        assertTrue(categoryEJB.create(sub1, root));
        assertTrue(categoryEJB.create(sub1 + "_1", sub1));
        assertTrue(categoryEJB.create(sub1 + "_2", sub1));
    
        assertFalse(categoryEJB.create(sub1 + "_2", sub1));
        assertFalse(categoryEJB.create("Sub_2", root + "_2"));
    }
    
    @Test
    public void testGetCategory() throws Exception {
        String monthyPython = "Monthy Python";
        categoryEJB.create(monthyPython);
        assertEquals(monthyPython, categoryEJB.get(monthyPython).getName());
        
        String holyGrail = "Holy Grail";
        categoryEJB.create(holyGrail, monthyPython);
        assertEquals(holyGrail, categoryEJB.get(holyGrail).getName());
        assertEquals(monthyPython, categoryEJB.get(holyGrail).getParentCategory().getName());
    
        assertEquals(1, categoryEJB.get(monthyPython).getChildCategories().size());
        assertEquals(holyGrail, categoryEJB.get(monthyPython).getChildCategories().get(0).getName());
    }
    
    @Test
    public void testBadInput() throws Exception {
        try {
            categoryEJB.create("    ");
            fail();
        }
        catch (EJBException e){e.printStackTrace();}
        assertFalse(categoryEJB.create("Bad_sub", "Null"));
    }
    
    @Test
    public void testDeleteSingleCategory() throws Exception {
        String root = "root";
        categoryEJB.create(root);
    
        List<String> exampleSubNames = checkGeneratedCategories(root);
    
        assertEquals(exampleSubNames.get(0), categoryEJB.get(exampleSubNames.get(0)).getName());
        categoryEJB.deleteSingle(exampleSubNames.get(0));
        assertNull(categoryEJB.get(exampleSubNames.get(0)));
        assertNotNull(categoryEJB.get(exampleSubNames.get(2)));
        assertNotNull(categoryEJB.get(root));
    }
    
    @Test
    public void testDeleteRecursively() throws Exception {
        String root = "root";
        categoryEJB.create(root);
        checkGeneratedCategories(root);
    
        List<Category> subsOfRoot = categoryEJB.get(root).getChildCategories();
        categoryEJB.deleteRecursively(root);
        List<Category> allCategories = categoryEJB.getAll();
        subsOfRoot.forEach(sub -> assertFalse(allCategories.contains(sub)));
    }
    
    @Test
    public void testDeleteAndMoveChildren() throws Exception {
        String root = "root";
        categoryEJB.create(root);
    
        List<String> exampleSubNames = checkGeneratedCategories(root);
        
        Category deleted = categoryEJB.get(exampleSubNames.get(0));
        Category newParent = categoryEJB.get(exampleSubNames.get(1));
        List<Category> childCategories = deleted.getChildCategories();
        
        categoryEJB.deleteMoveChildren(deleted.getName(), newParent.getName());
    
        deleted = categoryEJB.get(exampleSubNames.get(0));
        Category finalNewParent = categoryEJB.get(exampleSubNames.get(1));
        
        assertNull(deleted);
        assertTrue(finalNewParent.getChildCategories().containsAll(childCategories));
    }
    
    private List<String> checkGeneratedCategories(String root) {
        generateDummySubCategories(root, 3, 3);
        List<String> exampleSubNames = Arrays.asList("sub_0", "sub_2", "sub_0_2", "sub_2_0", "sub_0_2_0", "sub_2_0_2");
        exampleSubNames.forEach(name -> assertNotNull(categoryEJB.get(name)));
        return exampleSubNames;
    }
    
    /**
     * Generates dummy categories and writes to database.
     * @param root The name of the root category the generated categories belong to.
     * @param categoriesPerLevel The number of child categories each category has
     * @param levels How many times this method is called recursively.
     * @return A list with the names of every category in the order of sub_1 -> sub_1_1 -> sub_1_2 -> sub_2 -> sub_2_1 -> sub_2_2
     */
    private void generateDummySubCategories(String root, int categoriesPerLevel, int levels) {
        String sub = "sub";
        if (root.contains(sub))
            sub = root;
        
        for (int i = 0; i < categoriesPerLevel; i++)
            createDummySub(root, categoriesPerLevel, levels, sub, i);
    }
    
    private void createDummySub(String root, int categoriesPerLevel, int levels, String sub, int i) {
        StringBuilder builder = new StringBuilder(sub);
        builder.append("_").append(i);
        categoryEJB.create(builder.toString(), root);
        
        if (levels > 0) 
            generateDummySubCategories(builder.toString(), categoriesPerLevel, levels - 1);
    }
}

