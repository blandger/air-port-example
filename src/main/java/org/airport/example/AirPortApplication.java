package org.airport.example;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

/**
 * Main web pplication class is started by application container
 */
@ApplicationPath("/")
@LoginConfig(authMethod="MP-JWT", realmName="MP JWT Realm")
public class AirPortApplication extends Application {

}
