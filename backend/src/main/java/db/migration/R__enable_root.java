package db.migration;

import java.sql.PreparedStatement;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// @Component
public class R__enable_root extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        boolean isEnabled = context.getConfiguration()
                                   .getPlaceholders()
                                   .getOrDefault("root.enable", "false")
                                   .equals("true");
        String password = context.getConfiguration()
                                 .getPlaceholders()
                                 .get("root.password");
        if (isEnabled && !password.isEmpty()) {
            String username = "'" + context.getConfiguration()
                              .getPlaceholders()
                                           .getOrDefault("root.username", "root") + "'";
            String email = "'" + context.getConfiguration()
                                        .getPlaceholders()
                                        .getOrDefault("root.email", username + "@gfa.com") + "'";
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = "'" + encoder.encode(password) + "'";
            try (PreparedStatement statement = context
                     .getConnection()
                     .prepareStatement(
                         "update app_users set active = true, password = " + encodedPassword +
                         " where username = " + username + " ")) {
            statement.execute();
        }
        }
    }

    @Override
    public Integer getChecksum() {
        return (int) ( Math.random() * Integer.MAX_VALUE );
    }
}
