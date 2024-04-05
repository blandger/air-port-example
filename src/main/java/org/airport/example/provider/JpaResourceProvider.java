package org.airport.example.provider;

import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.ext.Provider;

/**
 * This class uses Contexts and Dependency Injection to alias Jakarta EE resources,
 * such as the persistence context, to Contexts and Dependency Injection beans
 * Example injection on a managed bean field:
 * </p>
 * <p>
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
@Provider
public class JpaResourceProvider {
    @Produces
    @PersistenceContext
    private EntityManager em;
}
