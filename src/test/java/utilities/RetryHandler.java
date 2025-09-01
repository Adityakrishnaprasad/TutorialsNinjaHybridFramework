package utilities;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryHandler implements IRetryAnalyzer, IAnnotationTransformer {

    private int retryCount = 0;
    private static final int maxRetryCount = 2; // retry once

    // --- Retry logic ---
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            System.out.println(" Retrying test: " + result.getName() + " | Attempt " + (retryCount + 1));
            return true;
        }
        return false;
    }

    // --- Attach globally ---
    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryHandler.class);
    }
}
