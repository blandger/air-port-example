package org.airport.example;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationPath("/")
@LoginConfig(authMethod="MP-JWT", realmName="MP JWT Realm")
public class AirPortApplication extends Application {

}
