package org.airport.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.inject.Inject;
import org.airport.example.service.UserService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Run integration tests with Arquillian to be able to test CDI beans
 */
@ExtendWith(ArquillianExtension.class)
public class UserServiceTestIT {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "UserServiceTestIT.war")
                .addClass(UserService.class);
    }

    @Inject
    UserService service;

    @Test
    public void testService() {
        String result = service.hello("World", "User");
        assertEquals("Hello 'World' (User).", result);

        result = service.hello("Monde", "User");
        assertEquals("Hello 'Monde' (User).", result);
    }
}