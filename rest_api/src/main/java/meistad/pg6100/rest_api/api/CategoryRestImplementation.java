package meistad.pg6100.rest_api.api;

import ejbs.CategoryEJB;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Created by haidbz on 04.12.16.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CategoryRestImplementation implements CategoryRestAPI {
    @EJB
    CategoryEJB ejb;
}
