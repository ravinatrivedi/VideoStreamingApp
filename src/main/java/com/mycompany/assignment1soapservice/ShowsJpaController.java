/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment1soapservice;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.assignment1soapservice.entities.Category;
import com.mycompany.assignment1soapservice.entities.Shows;
import com.mycompany.assignment1soapservice.entities.Type;
import com.mycompany.assignment1soapservice.exceptions.NonexistentEntityException;
import com.mycompany.assignment1soapservice.exceptions.PreexistingEntityException;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Aunsha Asaithambi
 */
public class ShowsJpaController implements Serializable {

    public ShowsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Shows shows) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category categoryid = shows.getCategoryid();
            if (categoryid != null) {
                categoryid = em.getReference(categoryid.getClass(), categoryid.getId());
                shows.setCategoryid(categoryid);
            }
            Type typeid = shows.getTypeid();
            if (typeid != null) {
                typeid = em.getReference(typeid.getClass(), typeid.getId());
                shows.setTypeid(typeid);
            }
            em.persist(shows);
            if (categoryid != null) {
                categoryid.getShowsCollection().add(shows);
                categoryid = em.merge(categoryid);
            }
            if (typeid != null) {
                typeid.getShowsCollection().add(shows);
                typeid = em.merge(typeid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findShows(shows.getId()) != null) {
                throw new PreexistingEntityException("Shows " + shows + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Shows shows) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Shows persistentShows = em.find(Shows.class, shows.getId());
            Category categoryidOld = persistentShows.getCategoryid();
            Category categoryidNew = shows.getCategoryid();
            Type typeidOld = persistentShows.getTypeid();
            Type typeidNew = shows.getTypeid();
            if (categoryidNew != null) {
                categoryidNew = em.getReference(categoryidNew.getClass(), categoryidNew.getId());
                shows.setCategoryid(categoryidNew);
            }
            if (typeidNew != null) {
                typeidNew = em.getReference(typeidNew.getClass(), typeidNew.getId());
                shows.setTypeid(typeidNew);
            }
            shows = em.merge(shows);
            if (categoryidOld != null && !categoryidOld.equals(categoryidNew)) {
                categoryidOld.getShowsCollection().remove(shows);
                categoryidOld = em.merge(categoryidOld);
            }
            if (categoryidNew != null && !categoryidNew.equals(categoryidOld)) {
                categoryidNew.getShowsCollection().add(shows);
                categoryidNew = em.merge(categoryidNew);
            }
            if (typeidOld != null && !typeidOld.equals(typeidNew)) {
                typeidOld.getShowsCollection().remove(shows);
                typeidOld = em.merge(typeidOld);
            }
            if (typeidNew != null && !typeidNew.equals(typeidOld)) {
                typeidNew.getShowsCollection().add(shows);
                typeidNew = em.merge(typeidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = shows.getId();
                if (findShows(id) == null) {
                    throw new NonexistentEntityException("The shows with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Shows shows;
            try {
                shows = em.getReference(Shows.class, id);
                shows.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The shows with id " + id + " no longer exists.", enfe);
            }
            Category categoryid = shows.getCategoryid();
            if (categoryid != null) {
                categoryid.getShowsCollection().remove(shows);
                categoryid = em.merge(categoryid);
            }
            Type typeid = shows.getTypeid();
            if (typeid != null) {
                typeid.getShowsCollection().remove(shows);
                typeid = em.merge(typeid);
            }
            em.remove(shows);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Shows> findShowsEntities() {
        return findShowsEntities(true, -1, -1);
    }

    public List<Shows> findShowsEntities(int maxResults, int firstResult) {
        return findShowsEntities(false, maxResults, firstResult);
    }

    private List<Shows> findShowsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Shows.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Shows findShows(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Shows.class, id);
        } finally {
            em.close();
        }
    }

    public int getShowsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Shows> rt = cq.from(Shows.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
