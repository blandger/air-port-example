package org.airport.example.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.airport.example.repository.entity.AirPortEntity;

import java.util.List;
import java.util.Objects;

/**
 * AirPort repository component for managing data in 'airports' table.
 */
@ApplicationScoped
public class AirPortRepository {

    @Inject
    private EntityManager em;

    /**
     * Find airport by ID
     * @param portId id
     * @return found instance or null
     */
    public AirPortEntity findById(Long portId) {
        Objects.requireNonNull(portId, "port ID is NULL");
        return em.find(AirPortEntity.class, portId);
    }

    /**
     * Get all records. TODO! Of course we should do PAGINATION here
     * @return all records
     */
    public List<AirPortEntity> getAll() {
        return em.createQuery("select port from AirPortEntity port", AirPortEntity.class).getResultList();
    }

    /**
     * Store new airport record
     * @param airPort entity to be stored
     */
    @Transactional
    public void create(AirPortEntity airPort) {
        Objects.requireNonNull(airPort, "airPort is NULL");
        em.persist(airPort);
    }

    /**
     * Update existing airport record
     * @param airPort entity to be updated
     */
    @Transactional
    public void update(AirPortEntity airPort) {
        Objects.requireNonNull(airPort, "airPort is NULL");
        em.merge(airPort);
    }

    /**
     * Find airport by unique code
     * @param code unique code
     * @return found instance or null
     */
    public AirPortEntity findByCode(String code) {
        Objects.requireNonNull(code, "code is NULL");
        try {
            Query query = em.createQuery("select port from AirPortEntity port where port.code = :code", AirPortEntity.class);
            query.setParameter("code", code);
            return (AirPortEntity) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Find aiport by city name
     * @param city city name or partial name
     * @return list of found records
     */
    public List findByCity(String city) {
        Objects.requireNonNull(city, "city is NULL");
        Query query = em.createQuery("select port from AirPortEntity port where port.city like '%:city%'", AirPortEntity.class);
        query.setParameter("city", city);
        return query.getResultList();
    }

}
