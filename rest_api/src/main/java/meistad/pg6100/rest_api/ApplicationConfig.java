package meistad.pg6100.rest_api;

import io.swagger.jaxrs.config.BeanConfig;
import meistad.pg6100.rest_api.api.category.root_category.CategoryRestImplementation;
import meistad.pg6100.rest_api.api.category.sub_category.SubCategoryRestImplementation;
import meistad.pg6100.rest_api.api.category.sub_sub_category.SubSubCategoryRestImplementation;
import meistad.pg6100.rest_api.api.quiz.QuizRestImplementation;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Håvard on 24.11.2016.
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    private final Set<Class<?>> classes;
    
    public ApplicationConfig() {
        BeanConfig beanConfig = createConfiguration();
        beanConfig.setScan(true);
    
        classes = Collections.unmodifiableSet(provideRestApi());
    }
    
    private BeanConfig createConfiguration() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("0.1");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/rest_api/api");
        beanConfig.setResourcePackage("meistad.pg6100.rest_api");
        return beanConfig;
    }
    
    private HashSet<Class<?>> provideRestApi() {
        HashSet<Class<?>> classHashSet = new HashSet<>();
        
        // Which classes provide REST API
        classHashSet.add(QuizRestImplementation.class);
        classHashSet.add(CategoryRestImplementation.class);
        classHashSet.add(SubCategoryRestImplementation.class);
        classHashSet.add(SubSubCategoryRestImplementation.class);
        
        // further configuration to activate SWAGGER
        classHashSet.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        classHashSet.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        return classHashSet;
    }
    
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
