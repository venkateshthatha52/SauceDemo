package com.reports;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
	public static ExtentReports extent;
	private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
	private static String timestamp;
	public static ExtentReports createExtentReport(ITestContext context) {
		String suiteName = context.getSuite().getName();
		if (extent == null) {
			ExtentSparkReporter spark = new ExtentSparkReporter(createReportFolder()+"/"+suiteName+"_ExtentReport_"+timestamp+".html");
			spark.config().setReportName("Automation Report");
			spark.config().setDocumentTitle("Test Results");

			extent = new ExtentReports();
			extent.attachReporter(spark);
		}

		return extent;
	}

	public static void updateTestResult(ITestResult result, WebDriver driver) {
		if (result.getStatus() == ITestResult.FAILURE) {
			try {
				// ✅ getDriver() — thread safe
				String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

				// ✅ getTest() — thread safe
				getTest().log(Status.FAIL, "Test Case Failed : " + result.getMethod().getMethodName());
				getTest().log(Status.FAIL, "Reason : " + result.getThrowable().getMessage());
				getTest().fail("Failure Screenshot : ",
						MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());

			} catch (Exception e) {
				log(Status.FAIL, "❌ FAILED : " + result.getThrowable().getMessage());
			}

		} else if (result.getStatus() == ITestResult.SUCCESS) {
			log(Status.PASS, "✅ Test PASSED");

		} else if (result.getStatus() == ITestResult.SKIP) {
			log(Status.SKIP, "⚠️ Test SKIPPED : " + result.getThrowable().getMessage());
		}
	}

	// Create test
	public static void createTest(String testName) {

		ExtentTest test = extent.createTest(testName);
		testThread.set(test);
	}

	// Get test
	public static ExtentTest getTest() {

		return testThread.get();
	}

	public static void log(Status status, String message) {
		if (getTest() != null) { // ✅ getTest()
			getTest().log(status, message);
		}
	}
	
	 public static String createReportFolder() {
		 
            timestamp =new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());        
	         // reports/08_05_2026
	        String folderPath = "reports/"+new SimpleDateFormat("dd_MM_yyyy").format(new Date());

	        File folder = new File(folderPath);
	         // Create folder if not exists
	        if (!folder.exists()) {

	            folder.mkdirs();
	        }

	        return folderPath;
	    }

}
