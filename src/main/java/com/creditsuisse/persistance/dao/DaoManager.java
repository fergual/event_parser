package com.creditsuisse.persistance.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DaoManager<T>  {

    private Class<T> clazz;
    private EntityManagerFactory entityManagerFactory;

    public DaoManager(Class<T> clazz) {
        this.clazz = clazz;
        this.entityManagerFactory = Persistence.createEntityManagerFactory("event-unit");
    }

    public void persist(T event) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(event);
        em.getTransaction().commit();
        em.close();
    }

    public List<T> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    public void destroy() {
        entityManagerFactory.close();
    }
}
