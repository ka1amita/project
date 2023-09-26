package db.migration;

import java.sql.PreparedStatement;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
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
        String username = "'" + context.getConfiguration()
                                       .getPlaceholders()
                                       .getOrDefault("root.username", "root") + "'";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = "'" + encoder.encode(password) + "'";

        if (!isEnabled) {
            try (PreparedStatement statement = context
                .getConnection()
                .prepareStatement(
                    "update app_users set active = false where username = " + username)) {
                statement.execute();

            }
        } else if (isEnabled && !password.isEmpty()) {
            try (PreparedStatement statement = context
                .getConnection()
                .prepareStatement(
                    "update app_users set active = true, password = " + encodedPassword +
                    " where username = " + username)) {
                statement.execute();
            }
        } else {
            throw new IllegalArgumentException("Wrong root credentials");
        }
    }

    @Override
    public Integer getChecksum() {
        return (int) ( Math.random() * Integer.MAX_VALUE );
    }
}
