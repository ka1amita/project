package db.migration;

import java.sql.PreparedStatement;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class R__enable_root extends BaseJavaMigration {
// TODO to it in a normal .sql and only do R__enable_disable_root

    @Autowired
    Context context;
    @Override
    public void migrate(Context context) throws Exception {
        boolean isEnabled = context.getConfiguration()
                                   .getPlaceholders()
                                   .getOrDefault("root", "false")
                                   .equals("true");
        if (isEnabled) {
        String username ="'" + context.getConfiguration()
                              .getPlaceholders()
                              .getOrDefault("username", "root") + "'";
        try (PreparedStatement statement =
                 context
                     .getConnection()
                     .prepareStatement("update app_users set active = true where username = "+ username +" ")) {
            statement.execute();
        }
        }


        // try (PreparedStatement statement =
        //          context
        //              .getConnection()
        //              .prepareStatement("insert into app_users (active, created_at, deleted, email, password, username, verified_at) values (true, now(), false," +
        //                                email + " , '${password}', '${username}', now())")) {
        //     statement.execute();
        // }
            // insert into app_users_roles (app_user_id, role_id) values (
            //                                                               (select id from app_users where username = '${root.username}'),
            //                                                               (select id from roles where name = '${root.role}')
            //                                                           );


            // add to App.java
            //     Flyway flyway = Flyway.configure()
            //     .dataSource(url, user, password)
            //     // Add all Spring-instantiated JavaMigration beans
            //     .javaMigrations(applicationContext.getBeansOfType(JavaMigration.class).values().toArray(new JavaMigration[0]))
            //     .load();
            // flyway.migrate();
    }

    @Override
    public Integer getChecksum() {
        return (int) (Math.random() * 10);
    }
}
