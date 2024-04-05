package org.airport.example.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.airport.example.repository.entity.UserEntity;

import java.util.Objects;

/**
 * Users repository component for managing data in 'users' table.
 */
@ApplicationScoped
public class UserRepository {

    @Inject
    private EntityManager em;

    /**
     * Find User by unique Id
     * @param userId id value
     * @return found user or null
     */
    public UserEntity findById(Long userId) {
        Objects.requireNonNull(userId, "user ID is NULL");
        return em.find(UserEntity.class, userId);
    }

    /**
     * Create new user record
     * @param user user's data
     */
    @Transactional
    public void createUser(UserEntity user) {
        Objects.requireNonNull(user, "user is NULL");
        em.persist(user);
    }

    /**
     * Find user's record by his email
     * @param email specified email
     * @return found instance or null
     */
    public UserEntity findByEmail(String email) {
        Objects.requireNonNull(email, "email is NULL");
        try {
            Query query = em.createQuery("select u from UserEntity u where u.email = :email", UserEntity.class);
            query.setParameter("email", email);
            return (UserEntity) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
