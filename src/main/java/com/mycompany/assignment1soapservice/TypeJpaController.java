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
import com.mycompany.assignment1soapservice.entities.Shows;
import com.mycompany.assignment1soapservice.entities.Type;
import com.mycompany.assignment1soapservice.exceptions.NonexistentEntityException;
import com.mycompany.assignment1soapservice.exceptions.PreexistingEntityException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Aunsha Asaithambi
 */
public class TypeJpaController implements Serializable {

    public TypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Type type) throws PreexistingEntityException, Exception {
        if (type.getShowsCollection() == null) {
            type.setShowsCollection(new ArrayList<Shows>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Shows> attachedShowsCollection = new ArrayList<Shows>();
            for (Shows showsCollectionShowsToAttach : type.getShowsCollection()) {
                showsCollectionShowsToAttach = em.getReference(showsCollectionShowsToAttach.getClass(), showsCollectionShowsToAttach.getId());
                attachedShowsCollection.add(showsCollectionShowsToAttach);
            }
            type.setShowsCollection(attachedShowsCollection);
            em.persist(type);
            for (Shows showsCollectionShows : type.getShowsCollection()) {
                Type oldTypeidOfShowsCollectionShows = showsCollectionShows.getTypeid();
                showsCollectionShows.setTypeid(type);
                showsCollectionShows = em.merge(showsCollectionShows);
                if (oldTypeidOfShowsCollectionShows != null) {
                    oldTypeidOfShowsCollectionShows.getShowsCollection().remove(showsCollectionShows);
                    oldTypeidOfShowsCollectionShows = em.merge(oldTypeidOfShowsCollectionShows);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findType(type.getId()) != null) {
                throw new PreexistingEntityException("Type " + type + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Type type) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Type persistentType = em.find(Type.class, type.getId());
            Collection<Shows> showsCollectionOld = persistentType.getShowsCollection();
            Collection<Shows> showsCollectionNew = type.getShowsCollection();
            Collection<Shows> attachedShowsCollectionNew = new ArrayList<Shows>();
            for (Shows showsCollectionNewShowsToAttach : showsCollectionNew) {
                showsCollectionNewShowsToAttach = em.getReference(showsCollectionNewShowsToAttach.getClass(), showsCollectionNewShowsToAttach.getId());
                attachedShowsCollectionNew.add(showsCollectionNewShowsToAttach);
            }
            showsCollectionNew = attachedShowsCollectionNew;
            type.setShowsCollection(showsCollectionNew);
            type = em.merge(type);
            for (Shows showsCollectionOldShows : showsCollectionOld) {
                if (!showsCollectionNew.contains(showsCollectionOldShows)) {
                    showsCollectionOldShows.setTypeid(null);
                    showsCollectionOldShows = em.merge(showsCollectionOldShows);
                }
            }
            for (Shows showsCollectionNewShows : showsCollectionNew) {
                if (!showsCollectionOld.contains(showsCollectionNewShows)) {
                    Type oldTypeidOfShowsCollectionNewShows = showsCollectionNewShows.getTypeid();
                    showsCollectionNewShows.setTypeid(type);
                    showsCollectionNewShows = em.merge(showsCollectionNewShows);
                    if (oldTypeidOfShowsCollectionNewShows != null && !oldTypeidOfShowsCollectionNewShows.equals(type)) {
                        oldTypeidOfShowsCollectionNewShows.getShowsCollection().remove(showsCollectionNewShows);
                        oldTypeidOfShowsCollectionNewShows = em.merge(oldTypeidOfShowsCollectionNewShows);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = type.getId();
                if (findType(id) == null) {
                    throw new NonexistentEntityException("The type with id " + id + " no longer exists.");
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
            Type type;
            try {
                type = em.getReference(Type.class, id);
                type.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The type with id " + id + " no longer exists.", enfe);
            }
            Collection<Shows> showsCollection = type.getShowsCollection();
            for (Shows showsCollectionShows : showsCollection) {
                showsCollectionShows.setTypeid(null);
                showsCollectionShows = em.merge(showsCollectionShows);
            }
            em.remove(type);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Type> findTypeEntities() {
        return findTypeEntities(true, -1, -1);
    }

    public List<Type> findTypeEntities(int maxResults, int firstResult) {
        return findTypeEntities(false, maxResults, firstResult);
    }

    private List<Type> findTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Type.class));
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

    public Type findType(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Type.class, id);
        } finally {
            em.close();
        }
    }

    public int getTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Type> rt = cq.from(Type.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
