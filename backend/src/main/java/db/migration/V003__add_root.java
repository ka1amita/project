package db.migration;

import java.sql.PreparedStatement;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V003__add_root extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (PreparedStatement statement =
                 context
                     .getConnection()
                     .prepareStatement("insert into app_users (active, created_at, deleted, email, password, username, verified_at) values (true, now(), false, '${root.email}', '${root.password}', '${root.username}', now())")) {
            statement.execute();
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
    }
}
