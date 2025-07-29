package utilities;

import org.testng.IExecutionListener;
import org.testng.ITestListener;

public class Listeners implements ITestListener, IExecutionListener {

	@Override
	public void onExecutionFinish() {
		
	    try {
	        System.out.println("Launching Allure report...");

	        // This assumes allure is added to system PATH
	        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "allure serve allure-results");
	        pb.inheritIO(); // Show output in console
	        Process p = pb.start();
	        p.waitFor();

	        System.out.println("Allure report has been successfully generated and launched.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Unable to generate Allure report.");
	    }

		
	}
	
}
