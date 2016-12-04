package meistad.pg6100.rest_api.api.category.root_category;

import io.swagger.annotations.Api;
import meistad.pg6100.rest_api.api.category.CategoryRestAPIBase;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by haidbz on 04.12.16.
 */
@Api(value = "/categories", description = "Handling of creating, retrieving and answering categories")
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public interface CategoryRestAPI extends CategoryRestAPIBase {
}
