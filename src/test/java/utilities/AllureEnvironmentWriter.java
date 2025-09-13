package utilities;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AllureEnvironmentWriter {
    private static final String ALLURE_RESULTS_DIR = "allure-results";
    private static final String ALLURE_ENV_FILE = "environment.properties";

    private static final LinkedHashSet<String> browsers = new LinkedHashSet<>();

    public static synchronized void writeEnv(WebDriver driver) {
        try {
            Path resultsDir = Paths.get(ALLURE_RESULTS_DIR);
            Files.createDirectories(resultsDir);
            Path envFile = resultsDir.resolve(ALLURE_ENV_FILE);

            Properties props = new Properties();
            if (Files.exists(envFile)) {
                try (FileInputStream fis = new FileInputStream(envFile.toFile())) {
                    props.load(fis);
                }
            }

            // Collect browsers
            if (driver != null) {
                Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
                String browser = formatName(caps.getBrowserName());
                browsers.add(browser);
            }

            // Save joined browsers (order preserved)
            if (!browsers.isEmpty()) {
                props.setProperty("Browsers", String.join(", ", browsers));
            }

            props.putIfAbsent("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
            props.putIfAbsent("Java Version", System.getProperty("java.version"));
            props.putIfAbsent("Executor", System.getProperty("user.name"));

            try (FileOutputStream fos = new FileOutputStream(envFile.toFile())) {
                props.store(fos, "Allure environment");
            }
        } catch (Exception e) {
            System.err.println("[AllureEnvWriter] " + e.getMessage());
        }
    }

    private static String formatName(String name) {
        if (name == null) return "Unknown";
        String n = name.toLowerCase();
        if (n.contains("edge")) return "Edge";
        if (n.contains("firefox")) return "Firefox";
        if (n.contains("chrome")) return "Chrome";
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
