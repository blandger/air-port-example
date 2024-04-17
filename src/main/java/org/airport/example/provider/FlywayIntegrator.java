package org.airport.example.provider;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateOutput;
import org.flywaydb.core.api.output.MigrateResult;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Component is used on app deploy during app startup to apply database migrations using FlyWay.
 * All SQL scripts applied from a folder: src/main/java/resources/database/migration/
 */
@Slf4j
public class FlywayIntegrator implements Integrator {

    /**
     * Method is called by server container on start up
     * @param metadata internal data, not used
     * @param sessionFactory factory instance, not used
     * @param serviceRegistry service instantiated by container from registered file
     *  /src/main/resources/META-INF/services/org.hibernate.integrator.spi.Integrator
     */
    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
                          SessionFactoryServiceRegistry serviceRegistry) {

        log.info("Migrating database to the latest version");

        final JdbcServices jdbcServices = serviceRegistry.getService(JdbcServices.class);
        DataSource dataSource = null;

        try (Connection connection = jdbcServices.getBootstrapJdbcConnectionAccess().obtainConnection()) {
            final Method method = connection != null ? connection.getClass().getMethod("getDataSource", null) : null;
            dataSource = (DataSource) (method != null ? method.invoke(connection, null) : null);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | SQLException e) {
            log.error("FlyWay, getting Datasource Error", e);
        }
        log.debug("FlyWay dataSource is ready, {}", dataSource);

        Flyway flyway =
                Flyway.configure()
                        .dataSource(dataSource)
                        .locations("classpath:database/migration/")
                        .load();
        MigrateResult migrateResult = flyway.migrate();

        log.info("Updated by flyway version: {},", migrateResult.flywayVersion);
        for (MigrateOutput migration : migrateResult.migrations) {
            log.info("Migration version: {}, path: {}", migration.version, migration.filepath);
        }
        log.info("Successfully migrated to database version: {}", flyway.info().current().getVersion());
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        // Not needed here
    }
}
