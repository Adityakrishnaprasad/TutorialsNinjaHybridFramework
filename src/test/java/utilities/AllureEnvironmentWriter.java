package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class AllureEnvironmentWriter {

    private static final String ALLURE_RESULTS_DIR = "allure-results";
    private static final String ALLURE_ENV_FILE = "environment.properties";

    public static void writeEnv(WebDriver driver) {
        try {
            Path allureResultsPath = Paths.get(ALLURE_RESULTS_DIR);
            if (Files.notExists(allureResultsPath)) {
                Files.createDirectories(allureResultsPath);
            }

            Properties props = new Properties();

            // Executor
            String executor = System.getenv("JENKINS_HOME") != null ? "Jenkins"
                    : System.getProperty("user.name", "Local");
            props.setProperty("Executor", executor);

            // Browser + Version
            if (driver != null) {
                try {
                    Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
                    props.setProperty("Browser", caps.getBrowserName());

                    String version = null;
                    try {
                        version = caps.getBrowserVersion(); // Selenium 4+
                    } catch (Throwable t) {
                        Object v1 = caps.getCapability("browserVersion");
                        if (v1 != null) version = v1.toString();
                        Object v2 = caps.getCapability("version");
                        if (v2 != null) version = v2.toString();
                    }
                    props.setProperty("Browser.Version", version == null ? "Unknown" : version);
                } catch (Exception e) {
                    props.setProperty("Browser", "Unknown");
                    props.setProperty("Browser.Version", "Unknown");
                }
            }

            // OS
            String os = System.getProperty("os.name", "Unknown");
            String osVersion = System.getProperty("os.version", "");
            props.setProperty("OS", os + (osVersion.isEmpty() ? "" : " " + osVersion));

            // JDK version
            props.setProperty("Java Version", System.getProperty("java.version", "Unknown"));

            // Git  branch
          
            String gitBranch = getEnvOrSys("GIT_BRANCH");

          
            if (gitBranch == null || gitBranch.isEmpty()) {
                gitBranch = runCommand("git", "rev-parse", "--abbrev-ref", "HEAD");
            }


            if (gitBranch != null && !gitBranch.isEmpty()) props.setProperty("Git.Branch", gitBranch);

            // Write file into allure-results/environment.properties
            
            Path envFilePath = allureResultsPath.resolve(ALLURE_ENV_FILE);
            try (FileOutputStream fos = new FileOutputStream(new File(envFilePath.toString()))) {
                props.store(fos, "Allure environment properties");
            }

            System.out.println("[AllureEnvWriter] Wrote environment info to: " + envFilePath.toString());
        } catch (Exception e) {
            System.err.println("[AllureEnvWriter] Failed to write environment properties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getEnvOrSys(String key) {
        String val = System.getenv(key);
        if (val == null || val.isEmpty()) {
            val = System.getProperty(key);
        }
        return val;
    }

    private static String runCommand(String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line = reader.readLine();
                if (line != null) return line.trim();
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}