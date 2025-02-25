package io.castled.commands;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.castled.CastledApplication;
import io.castled.CastledConfiguration;
import io.castled.CastledStateStore;
import io.castled.ObjectRegistry;
import io.castled.daos.InstallationDAO;
import io.castled.events.CastledEventsClient;
import io.castled.events.NewInstallationEvent;
import io.castled.services.UsersService;
import io.castled.utils.AsciiArtUtils;
import io.dropwizard.cli.ServerCommand;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;

import java.util.UUID;

public class CastledServerCommand extends ServerCommand<CastledConfiguration> {

    public CastledServerCommand(CastledApplication castledApplication) {
        super(castledApplication, "castled-server", "Runs the castled server");
    }

    protected void run(Environment environment, Namespace namespace, CastledConfiguration configuration) throws Exception {
        runMigrations(configuration);
        super.run(environment, namespace, configuration);
        AsciiArtUtils.drawCastled();

    }

    private void runMigrations(CastledConfiguration configuration) {
        Flyway flyway = new Flyway();
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(configuration.getDatabase().getUrl());
        flyway.setDataSource(mysqlDataSource);
        flyway.setLocations("migration");
        flyway.migrate();

        //create test team and user if required
        UsersService usersService = ObjectRegistry.getInstance(UsersService.class);
        if (usersService.getUser() == null) {
            usersService.createTestTeamAndUser();
        }
        createNewInstallationIfRequired();

    }

    private void createNewInstallationIfRequired() {
        InstallationDAO installationDAO = ObjectRegistry.getInstance(Jdbi.class).onDemand(InstallationDAO.class);
        String installId = installationDAO.getInstallation();
        if (installId != null) {
            CastledStateStore.installId = installId;
            return;
        }
        String newInstallationId = UUID.randomUUID().toString();
        installationDAO.createInstallation(newInstallationId);
        CastledStateStore.installId = newInstallationId;
        ObjectRegistry.getInstance(CastledEventsClient.class).publishCastledEvent(new NewInstallationEvent(newInstallationId));
    }
}
