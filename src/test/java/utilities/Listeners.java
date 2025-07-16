package utilities;

import org.testng.IExecutionListener;
import org.testng.ITestListener;

public class Listeners implements ITestListener, IExecutionListener {

	@Override
	public void onExecutionFinish() {
		 try {
		      
		        System.out.println("--Opening the Allure reports--");
		        ProcessBuilder pb = new ProcessBuilder(
		        	    "C:\\Program Files\\allure-2.34.1\\bin\\allure.bat",
		        	    "serve",
		        	    "allure-results"
		        	);
		        pb.inheritIO();
		        Process ps = pb.start();
		        ps.waitFor(); 
		    } 
		 catch (Exception e) {
		        e.printStackTrace();
		        System.out.println("Unable to generate the Allure report.");
		    }
	}
}
