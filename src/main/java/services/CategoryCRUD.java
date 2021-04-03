/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.mycompany.assignment1soapservice.CategoryJpaController;
import com.mycompany.assignment1soapservice.entities.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author RAVINA TRIVEDI
 */
@WebService(serviceName = "CategoryCRUD")
public class CategoryCRUD {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "addCategory")
    public boolean addCategory(@WebParam(name = "cat") Category category) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
            CategoryJpaController catRepo = new CategoryJpaController(emf);
            int id = catRepo.getCategoryCount() + 1;
            category.setId(BigDecimal.valueOf(id));
            catRepo.create(category);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(CategoryCRUD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @WebMethod(operationName = "viewCategory")
    public List<Category> viewCategory() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        CategoryJpaController catRepo = new CategoryJpaController(emf);
        List<Category> findCategoryEntities = catRepo.findCategoryEntities();
        return findCategoryEntities;
    }
    
    //Find
    @WebMethod(operationName = "findCategory")
    public Category findCategory(BigDecimal id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        CategoryJpaController typeRepo = new CategoryJpaController(emf);
        Category category = typeRepo.findCategory(id);
        return category;
    }
}
