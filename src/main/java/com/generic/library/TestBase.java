package com.generic.library;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.reports.ExtentReportManager;

public class TestBase {
	// ✅ Change 1 — ThreadLocal instead of plain field
	private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
	private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

	// ✅ Change 2 — Getters replace direct field access
	public WebDriver getDriver() {
		return driverThread.get();
	}

	protected ExtentTest getTest() {
		return testThread.get();
	}

	@BeforeSuite
	public void initializeReports() {
		// Initialize the report
		ExtentReportManager.createExtentReport();
	}

	@Parameters({ "browser" })
	@BeforeMethod
	public void setup(String browser, ITestResult result) {
		selectbrowser(browser);
		// ✅ Step 2: Get browser details after driver is ready
		String browserInfo = browserDetails(getDriver());

		// ✅ Step 3: Update report header with real browser name
		ExtentReportManager.updateBrowserInfo(browserInfo);
		// Get the name of the test method
		String testName = result.getMethod().getMethodName();
		testThread.set(ExtentReportManager.createExtentReport().createTest(testName));
		log(Status.INFO, "Test Started  : " + testName);
		log(Status.INFO, "Browser       : " + browserDetails(getDriver()));
		log(Status.INFO, "OS            : " + System.getProperty("os.name"));

	}

	@AfterMethod
	public void teardown(ITestResult result) {

		if (result.getStatus() == ITestResult.FAILURE) {
			try {
				// ✅ getDriver() — thread safe
				String base64 = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BASE64);

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

		closebrowser();
		ExtentReportManager.closeReport();

		// ✅ Change 4 — cleanup ThreadLocals — prevents memory leaks
		driverThread.remove();
		testThread.remove();
	}

	public void selectbrowser(String browser) {
		WebDriver driver;
		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else {
			throw new IllegalArgumentException("❌ Unsupported browser: " + browser);
		}
		// ✅ Store in ThreadLocal
		driverThread.set(driver);
		openapplication();
	}

	public void openapplication() {
		getDriver().get("https://sauce-demo.myshopify.com/");
		getDriver().manage().window().maximize();
	}

	public void closebrowser() {
		try {
			if (getDriver() != null) { // ✅ getDriver()
				getDriver().quit();
				System.out.println("Browser closed: Thread " + Thread.currentThread().getName());
			}
		} catch (Exception e) {
			System.out.println("Error closing browser: " + e.getMessage());
		}
	}

	protected void log(Status status, String message) {
		if (getTest() != null) { // ✅ getTest()
			getTest().log(status, message);
		}
		System.out.println("[Thread-" + Thread.currentThread().getName() + "] [" + status + "] " + message);
	}

	public static String browserDetails(WebDriver driver) {
		Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
		return capabilities.getBrowserName() + "(" + capabilities.getBrowserVersion() + ")";
	}
}
