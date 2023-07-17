package com.mycompany.libreria.persistece;

import com.mycompany.libreria.entity.Cliente;
import com.mycompany.libreria.entity.Prestamo;
import com.mycompany.libreria.persistece.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class PrestamoJpaController implements Serializable {

    public PrestamoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public PrestamoJpaController() {
        emf = Persistence.createEntityManagerFactory("libreriaJPAPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prestamo prestamo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(prestamo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prestamo prestamo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            prestamo = em.merge(prestamo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = prestamo.getId();
                if (findPrestamo(id) == null) {
                    throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prestamo prestamo;
            try {
                prestamo = em.getReference(Prestamo.class, id);
                prestamo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.", enfe);
            }
            em.remove(prestamo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Prestamo> findPrestamoEntities() {
        return findPrestamoEntities(true, -1, -1);
    }

    public List<Prestamo> findPrestamoEntities(int maxResults, int firstResult) {
        return findPrestamoEntities(false, maxResults, firstResult);
    }

    private List<Prestamo> findPrestamoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prestamo.class));
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

    public Prestamo findPrestamo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prestamo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrestamoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prestamo> rt = cq.from(Prestamo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Prestamo> buscarPrestamosPorCliente(Cliente cliente) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM Prestamo p JOIN FETCH p.libro l WHERE p.cliente.id = :clienteId";
            TypedQuery<Prestamo> query = em.createQuery(jpql, Prestamo.class);
            query.setParameter("clienteId", cliente.getId());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Prestamo> buscarPrestamosPorLibro(Long isbn) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p FROM Prestamo p JOIN FETCH p.libro l WHERE l.isbn = :isbn ORDER BY p.fechaDevolucion ASC";
            TypedQuery<Prestamo> query = em.createQuery(jpql, Prestamo.class);
            query.setParameter("isbn", isbn);
            query.setMaxResults(3); // Limitar a 3
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Prestamo buscarPrestamoPorLibro(Long isbn) {
        EntityManager em = getEntityManager();
        try {

            String jpql = "SELECT p FROM Prestamo p JOIN FETCH p.libro l WHERE l.isbn = :isbn ORDER BY p.fechaDevolucion ASC";
            TypedQuery<Prestamo> query = em.createQuery(jpql, Prestamo.class);
            query.setParameter("isbn", isbn);
            query.setMaxResults(1); // Limitar a 1
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
