package com.generic.library;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import static com.reports.ExtentReportManager.*;

public class TestBase {

	private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
	private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

	public WebDriver getDriver() {
		return driverThread.get();
	}

	@BeforeTest
	public void initializeReports(ITestContext context) {
		// Initialize the report
		createExtentReport(context);
	}

	@Parameters({ "browser" })
	@BeforeMethod
	public void setup(String browser, ITestResult result) {
		selectbrowser(browser);
		String testName = result.getMethod().getMethodName();
		createTest(testName);
		log(Status.INFO, "Test Started  : " + testName);
		log(Status.INFO, "Browser       : " + browserDetails(getDriver()));
		log(Status.INFO, "OS            : " + System.getProperty("os.name"));

	}

	@AfterMethod
	public void teardown(ITestResult result) {
		updateTestResult(result,getDriver());
		closebrowser();
		
		// ✅ Change 4 — cleanup ThreadLocals — prevents memory leaks
		driverThread.remove();
		testThread.remove();
	}

	@AfterSuite
	public void flushReport() {
		extent.flush(); // VERY IMPORTANT
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

	public static String browserDetails(WebDriver driver) {
		Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
		return capabilities.getBrowserName() + "(" + capabilities.getBrowserVersion() + ")";
	}
}
