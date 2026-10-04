package utilities;

import java.io.File;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Reads settings from .env (local runs) and from environment variables (Jenkins / Docker).
 * Environment variables win over .env. There is no fallback to .env.example, because its
 * placeholder values (e.g. your_username_here) make tests fail in confusing ways.
 */
public class ConfigurationReader {

    private static final Dotenv dotenv;

    static {
        boolean hasEnvFile = new File(System.getProperty("user.dir"), ".env").exists();
        dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .filename(".env")
                .ignoreIfMissing()
                .load();
        LoggerLoad.info(hasEnvFile
                ? "Settings loaded from .env and environment variables"
                : "No .env file found, using environment variables only");
    }

    /**
     * @param key setting name, e.g. baseURL
     * @return the value, or null if it is not set anywhere
     */
    public static String get(String key) {
        String value = dotenv.get(key);
        if (value != null && value.trim().toLowerCase().startsWith("your_")) {
            throw new IllegalStateException("Setting '" + key + "' still has the example value '" + value
                    + "'. Put the real value in .env or set it as an environment variable.");
        }
        return value;
    }

    /**
     * Same as get(), but fails with a clear message when the setting is missing.
     */
    public static String getRequired(String key) {
        String value = get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Setting '" + key + "' is missing. Add it to .env or set it as an environment variable.");
        }
        return value;
    }
}
