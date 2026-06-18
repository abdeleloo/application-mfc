package fr.mfc.desktop.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class AppConfig {
    private AppConfig() {
    }

    public static Properties load() {
        Properties props = new Properties();
        props.setProperty("db.host", "127.0.0.1");
        props.setProperty("db.port", "3306");
        props.setProperty("db.name", "mfc");
        props.setProperty("db.user", "root");
        props.setProperty("db.password", "");

        Path localConfig = Path.of("config", "app.properties");
        if (Files.exists(localConfig)) {
            try (InputStream in = new FileInputStream(localConfig.toFile())) {
                props.load(in);
            } catch (Exception ignored) {
            }
        } else {
            try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("app.properties")) {
                if (in != null) {
                    props.load(in);
                }
            } catch (Exception ignored) {
            }
        }

        props.putIfAbsent("db.password", "");
        return props;
    }
}
