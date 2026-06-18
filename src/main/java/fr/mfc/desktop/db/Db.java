package fr.mfc.desktop.db;

import fr.mfc.desktop.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class Db {
    private Db() {
    }

    public static Connection connect() throws Exception {
        Properties props = AppConfig.load();
        String host = props.getProperty("db.host");
        String port = props.getProperty("db.port");
        String db = props.getProperty("db.name");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
        return DriverManager.getConnection(url, user, pass);
    }
}
