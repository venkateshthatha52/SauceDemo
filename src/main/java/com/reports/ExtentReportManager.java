package com.reports;


import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {
	private static ExtentReports extent;
    private static ExtentSparkReporter htmlReporter;
    
   
   
  
    public static ExtentReports createExtentReport() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
            htmlReporter = new ExtentSparkReporter(reportPath);

            htmlReporter.config().setTheme(Theme.STANDARD);
            htmlReporter.config().setDocumentTitle("Sauce Demo Test Report");
            htmlReporter.config().setReportName("Sauce Demo Automation Test Report");
            htmlReporter.config().setEncoding("UTF-8");

            htmlReporter.config().setCss(
                "#custom-report-header {" +
                "   background-color: #ffffff;" +
                "   padding: 15px 20px;" +
                "   border-bottom: 3px solid #00b4d8;" +
                "   border-top: 3px solid #00b4d8;" +
                "   width: 100%;" +
                "   box-sizing: border-box;" +
                "   position: sticky !important;" +
                "   top: 0 !important;" +
                "   z-index: 99999 !important;" +
                "   display: block !important;" +
                "}" +
                "#custom-report-header h2 {" +
                "   color: #00b4d8; font-size: 20px; margin: 0 0 10px 0; padding-left: 10px;" +
                "}" +
                "#custom-report-header table {" +
                "   border-collapse: collapse; margin-left: 10px; width: 70%;" +
                "}" +
                "#custom-report-header table td {" +
                "   color: #444444; font-size: 13px; padding: 4px 40px 4px 0; text-align: left;" +
                "}" +
                "#custom-report-header table td b { color: #111111; }"
            );

            // ✅ Use a JS variable placeholder — value injected later via updateBrowserInfo()
            htmlReporter.config().setJs(
                // ✅ Browser value stored in a JS variable — updated dynamically
                "var browserName = 'Loading...';" +

                "function buildHeaderHTML() {" +
                "   return '<h2>&#x1F680; Sauce Demo Automation Test Report</h2>' +" +
                "       '<table>' +" +
                "           '<tr>' +" +
                "               '<td><b>Project:</b></td><td>Sauce Demo E-Commerce Application</td>' +" +
                "               '<td><b>Tester:</b></td><td>Venkatesh Thatha</td>' +" +
                "           '</tr>' +" +
                "           '<tr>' +" +
                "               '<td><b>Environment:</b></td><td>Production</td>' +" +
                "               '<td><b>OS:</b></td><td>" + System.getProperty("os.name") + "</td>' +" +
                "           '</tr>' +" +
                "           '<tr>' +" +
                // ✅ Reads JS variable at render time — not Java compile time
                "               '<td><b>Browser:</b></td><td>' + browserName + '</td>' +" +
                "               '<td><b>Execution Date:</b></td><td>" + new java.util.Date() + "</td>' +" +
                "           '</tr>' +" +
                "       '</table>';" +
                "}" +

                "function injectHeader() {" +
                "   var existing = document.getElementById('custom-report-header');" +
                "   if (existing && document.body.firstChild === existing) return;" +
                "   if (existing) existing.remove();" +
                "   var h = document.createElement('div');" +
                "   h.id = 'custom-report-header';" +
                "   h.innerHTML = buildHeaderHTML();" +  // ✅ rebuilt each time with latest browserName
                "   document.body.insertBefore(h, document.body.firstChild);" +
                "}" +

                "window.addEventListener('load', function() {" +
                "   injectHeader();" +
                "   setInterval(injectHeader, 300);" +
                "});"
            );

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
        }
        return extent;
    }
    
    // ✅ Injects browser name into the live HTML report via a <script> tag
    public static void updateBrowserInfo(String browser) {
        if (extent != null && htmlReporter != null) {
            // ✅ Override the JS variable with actual browser value
            htmlReporter.config().setJs(
                htmlReporter.config().getJs() +
                // Appended script updates the JS variable in the page
                "var browserName = '" + browser + "';" +
                "injectHeader();"
            );
        }
    }
    public static ExtentTest createTest(String testName) {
        return extent.createTest(testName);
    }
    
    

    // Close the report
    public static void closeReport() {
        extent.flush();
    }
}
