package utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Re-runs a failed test once. Helps with one-off problems like a slow page load.
 * A test that fails twice is reported as failed. The first attempt stays visible
 * in the Allure report (Retries tab), so flaky tests are not hidden.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 1;
    private int attempts = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (attempts < MAX_RETRIES) {
            attempts++;
            LoggerLoad.warn("Retrying '" + result.getMethod().getMethodName() + "' (retry " + attempts + " of "
                    + MAX_RETRIES + ") after: " + result.getThrowable());
            return true;
        }
        return false;
    }
}
