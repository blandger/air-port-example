package org.airport.example.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.airport.example.repository.entity.AirPortEntity;

import java.util.List;
import java.util.Objects;

/**
 * AirPort repository component for managing data in 'airports' table.
 */
@ApplicationScoped
@Slf4j
public class AirPortRepository {
    @Inject
    private EntityManager em;

    /**
     * Store new airport record
     * @param airPort entity to be stored
     */
    @Transactional
    public void createAirPort(AirPortEntity airPort) {
        Objects.requireNonNull(airPort, "airPort is NULL");
        em.persist(airPort);
    }

    /**
     * Update existing airport record by new values
     * If updated 'code' value is not unique in database the error will happen.
     * @param airPort entity to be updated
     */
    @Transactional
    public void update(AirPortEntity airPort) {
        Objects.requireNonNull(airPort, "airPort is NULL");
        em.merge(airPort);
    }

    /**
     * Delete existing AirPort record and return the deleted records number.
     * @param airPortId instance with ID value
     * @return one if it's deleted zero otherwise
     */
    @Transactional
    public int delete(Long airPortId) {
        Objects.requireNonNull(airPortId, "airport entity ID is NULL");
        int deleted = em.createQuery("delete from AirPortEntity where id = :id")
                .setParameter("id", airPortId)
                .executeUpdate();
        log.trace("Deleted count by '{}' = {}", airPortId, deleted);
        return deleted;
    }

    /**
     * Find airport by using airport's name OR code.
     * Both can be specified but OR predicate will be used in such case.
     *
     * When neither 'Name' nor 'Code' is specified the all records are selected
     *
     * @param airPortName airPort name (optional)
     * @param code airPort code (optional)
     * @return list of found records
     */
    public List findByNameOrCode(String airPortName, String code) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<AirPortEntity> criteriaQuery =
                criteriaBuilder.createQuery(AirPortEntity.class);
        Root<AirPortEntity> root = criteriaQuery.from(AirPortEntity.class);
        var select = criteriaQuery.select(root).distinct(true);
        if (airPortName != null && !airPortName.isEmpty() && code != null && !code.isEmpty()) {
            // like NAME or CODE
            var likeName = criteriaBuilder.like(root.get("name"), "%" + airPortName + "%");
            var likeCode = criteriaBuilder.like(root.get("code"), "%" + code + "%");
            select.where(criteriaBuilder.or(likeName, likeCode));
            System.out.println("LIKE airPortName = " + airPortName + "OR code = " + code);
        } else if (airPortName != null && !airPortName.isEmpty()) {
            // like NAME only
            select.where(criteriaBuilder.like(root.get("name"), "%" + airPortName + "%"));
            System.out.println("Like airPortName = " + airPortName);
        } else if (code != null && !code.isEmpty()) {
            // like CODE only
            select.where(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
            System.out.println("Like code = " + code);
        }
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
        // otherwise return ALL
        TypedQuery<AirPortEntity> query = em.createQuery(criteriaQuery);
        return query.getResultList();
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

}
