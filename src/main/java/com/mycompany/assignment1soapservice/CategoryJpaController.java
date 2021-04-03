/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment1soapservice;

import com.mycompany.assignment1soapservice.entities.Category;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.assignment1soapservice.entities.Shows;
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
public class CategoryJpaController implements Serializable {

    public CategoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Category category) throws PreexistingEntityException, Exception {
        if (category.getShowsCollection() == null) {
            category.setShowsCollection(new ArrayList<Shows>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Shows> attachedShowsCollection = new ArrayList<Shows>();
            for (Shows showsCollectionShowsToAttach : category.getShowsCollection()) {
                showsCollectionShowsToAttach = em.getReference(showsCollectionShowsToAttach.getClass(), showsCollectionShowsToAttach.getId());
                attachedShowsCollection.add(showsCollectionShowsToAttach);
            }
            category.setShowsCollection(attachedShowsCollection);
            em.persist(category);
            for (Shows showsCollectionShows : category.getShowsCollection()) {
                Category oldCategoryidOfShowsCollectionShows = showsCollectionShows.getCategoryid();
                showsCollectionShows.setCategoryid(category);
                showsCollectionShows = em.merge(showsCollectionShows);
                if (oldCategoryidOfShowsCollectionShows != null) {
                    oldCategoryidOfShowsCollectionShows.getShowsCollection().remove(showsCollectionShows);
                    oldCategoryidOfShowsCollectionShows = em.merge(oldCategoryidOfShowsCollectionShows);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategory(category.getId()) != null) {
                throw new PreexistingEntityException("Category " + category + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category persistentCategory = em.find(Category.class, category.getId());
            Collection<Shows> showsCollectionOld = persistentCategory.getShowsCollection();
            Collection<Shows> showsCollectionNew = category.getShowsCollection();
            Collection<Shows> attachedShowsCollectionNew = new ArrayList<Shows>();
            for (Shows showsCollectionNewShowsToAttach : showsCollectionNew) {
                showsCollectionNewShowsToAttach = em.getReference(showsCollectionNewShowsToAttach.getClass(), showsCollectionNewShowsToAttach.getId());
                attachedShowsCollectionNew.add(showsCollectionNewShowsToAttach);
            }
            showsCollectionNew = attachedShowsCollectionNew;
            category.setShowsCollection(showsCollectionNew);
            category = em.merge(category);
            for (Shows showsCollectionOldShows : showsCollectionOld) {
                if (!showsCollectionNew.contains(showsCollectionOldShows)) {
                    showsCollectionOldShows.setCategoryid(null);
                    showsCollectionOldShows = em.merge(showsCollectionOldShows);
                }
            }
            for (Shows showsCollectionNewShows : showsCollectionNew) {
                if (!showsCollectionOld.contains(showsCollectionNewShows)) {
                    Category oldCategoryidOfShowsCollectionNewShows = showsCollectionNewShows.getCategoryid();
                    showsCollectionNewShows.setCategoryid(category);
                    showsCollectionNewShows = em.merge(showsCollectionNewShows);
                    if (oldCategoryidOfShowsCollectionNewShows != null && !oldCategoryidOfShowsCollectionNewShows.equals(category)) {
                        oldCategoryidOfShowsCollectionNewShows.getShowsCollection().remove(showsCollectionNewShows);
                        oldCategoryidOfShowsCollectionNewShows = em.merge(oldCategoryidOfShowsCollectionNewShows);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = category.getId();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
            Collection<Shows> showsCollection = category.getShowsCollection();
            for (Shows showsCollectionShows : showsCollection) {
                showsCollectionShows.setCategoryid(null);
                showsCollectionShows = em.merge(showsCollectionShows);
            }
            em.remove(category);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Category> rt = cq.from(Category.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
