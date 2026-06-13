package utilities;

import java.nio.file.Paths;

import org.testng.IExecutionListener;
import org.testng.ITestListener;

public class Listeners implements ITestListener, IExecutionListener {

	
	@Override
    public void onExecutionFinish() {

        try {
            System.out.println("...Generating Allure report...");

            String allureHome = System.getenv("ALLURE_HOME");
            if (allureHome == null || allureHome.isEmpty()) {
                throw new IllegalStateException("ALLURE_HOME environment variable is not set.");
            }

            String os = System.getProperty("os.name").toLowerCase();
            String allureExecutable = os.contains("win") ? "allure.bat" : "allure";

            String allureCmd = Paths.get(allureHome, "bin", allureExecutable).toString();

            // Step 1: Generate Allure Report
            ProcessBuilder generateReport = new ProcessBuilder(allureCmd, "generate", "--clean", "allure-results", "-o", "allure-report");
            generateReport.inheritIO();
            Process p1 = generateReport.start();
            p1.waitFor();

            // Step 2: Serve locally only (not on Jenkins)
            if (System.getenv("JENKINS_HOME") == null) {
                System.out.println("Launching Allure report in browser (local only)...");
                ProcessBuilder serveReport = new ProcessBuilder(allureCmd, "open", "allure-report");
                serveReport.inheritIO();
                serveReport.start(); // Don't wait, non-blocking
            } else {
                System.out.println("Detected Jenkins environment. Skipping 'allure open'.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to generate Allure report.");
        }
    }
	
}
