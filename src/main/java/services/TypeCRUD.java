/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.mycompany.assignment1soapservice.TypeJpaController;
import com.mycompany.assignment1soapservice.entities.Type;
import com.mycompany.assignment1soapservice.exceptions.NonexistentEntityException;
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
 * @author Aunsha Asaithambi
 */
@WebService(serviceName = "TypeCRUD")
public class TypeCRUD {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "addType")
    public boolean addType(@WebParam(name = "type") Type type) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
            TypeJpaController typeRepo = new TypeJpaController(emf);
            int id = typeRepo.getTypeCount()+1;
            type.setId(BigDecimal.valueOf(id));
            typeRepo.create(type);   
            return true;
        } catch (Exception ex) {
            Logger.getLogger(TypeCRUD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
     //Read
    @WebMethod(operationName = "viewType")
    public List<Type> viewType(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        TypeJpaController typeRepo = new TypeJpaController(emf);
        List<Type> findTypeEntities = typeRepo.findTypeEntities();
        return findTypeEntities;
    }
    
    //Update
    @WebMethod(operationName = "updateType")
    public boolean updateType(Type type){
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
            TypeJpaController typeRepo = new TypeJpaController(emf);
            Type t = new Type();
            t.setId(BigDecimal.valueOf(1));
            t.setType("Movies");
            typeRepo.edit(t);
            
            return true;
        } catch (Exception ex) {
            Logger.getLogger(TypeCRUD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //Delete
    public boolean deleteOperation(@WebParam(name="id") int id){
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
            TypeJpaController typeRepo = new TypeJpaController(emf);
            typeRepo.destroy(BigDecimal.valueOf(id));
            return true;
        }  catch (NonexistentEntityException ex) {
            Logger.getLogger(TypeCRUD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //Find
    @WebMethod(operationName = "findType")
    public Type findType(BigDecimal id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        TypeJpaController typeRepo = new TypeJpaController(emf);
        Type type = typeRepo.findType(id);
        return type;
    }
}
