/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.mycompany.assignment1soapservice.ShowsJpaController;
import com.mycompany.assignment1soapservice.entities.Shows;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.ws.soap.MTOM;

/**
 *
 * @author Aunsha Asaithambi
 */
@WebService(serviceName = "ShowCRUD")
@MTOM(enabled=true,threshold = 1000000)
@HandlerChain(file = "/ShowCRUD_handler.xml")
public class ShowCRUD {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "addShow")
    public Shows addShow(@WebParam(name = "showsAdd") Shows show) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
            ShowsJpaController showRepo = new ShowsJpaController(emf);
            List<Shows> allShows = showRepo.findShowsEntities();
            ArrayList<BigDecimal> allShowIds= new ArrayList<BigDecimal>();
            for(Shows s:allShows){
                allShowIds.add(s.getId());
            }  
            show.setId(Collections.max(allShowIds).add(BigDecimal.ONE));
            System.out.println(Collections.max(allShowIds));
            showRepo.create(show);   
            return show;
        } catch (Exception ex) {
            Logger.getLogger(ShowCRUD.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @WebMethod(operationName = "updateShow")
    public boolean updateShow(@WebParam(name = "updateShow") Shows show) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
            ShowsJpaController showRepo = new ShowsJpaController(emf);
            showRepo.edit(show);   
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ShowCRUD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @WebMethod(operationName = "deleteShow")
    public boolean deleteShow(@WebParam(name = "deleteShow") BigDecimal id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
            ShowsJpaController showRepo = new ShowsJpaController(emf);
            showRepo.destroy(id);   
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ShowCRUD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    //Read
    @WebMethod(operationName = "viewShow")
    public List<Shows> viewShow(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        ShowsJpaController showRepo = new ShowsJpaController(emf);
        List<Shows> findShowEntities = showRepo.findShowsEntities();
        return findShowEntities;
    }
    
    //Find
    @WebMethod(operationName = "findShow")
    public Shows findShow(BigDecimal id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        ShowsJpaController showRepo = new ShowsJpaController(emf);
        Shows show = showRepo.findShows(id);
        return show;
    }
    
}
