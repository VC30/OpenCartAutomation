package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date; // Ensure this import is also included
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ExtentReportManager implements ITestListener{
   public ExtentSparkReporter sparkReporter;
   public ExtentReports extent;
   public ExtentTest test;
   //public SimpleDateFormat timeStamp;
   
   String repName;
   
   
   //from chapgpt 
   
   public void onStart(ITestContext testContext) {

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");
	    String timeStamp = LocalDateTime.now().format(formatter);

	    repName = "Test-Report-" + timeStamp + ".html";
	    //sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "\\reports\\" + repName);
	    sparkReporter = new ExtentSparkReporter(".\\reports\\"+repName);

	    sparkReporter.config().setDocumentTitle("openCart Automation Report");
	    sparkReporter.config().setReportName("opencart Functional Testing");
	    sparkReporter.config().setTheme(Theme.DARK);

	    extent = new ExtentReports();
	    extent.attachReporter(sparkReporter);
	    extent.setSystemInfo("Application", "opencart");
	    extent.setSystemInfo("Module", "Admin");
	    extent.setSystemInfo("Sub Module", "Customers");
	    extent.setSystemInfo("User Name", System.getProperty("user.name"));
	    extent.setSystemInfo("Environment", "QA");

	    String os = testContext.getCurrentXmlTest().getParameter("os");
	    extent.setSystemInfo("Operating System", os);

	    String browser = testContext.getCurrentXmlTest().getParameter("browser");
	    extent.setSystemInfo("Browser", browser);

	    List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();

	    if (!includedGroups.isEmpty()) {
	        extent.setSystemInfo("Groups", includedGroups.toString());
	    }
	}
   
//   public void onStart(ITestContext testContext) {
//	   
//	   /*SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
//	   Date dt = new Date();
//	   String currentdatetimestamp = df.format(dt);*/ //this is same as the timeStamp method below
//	   
//	   String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//	   repName = "Test-report-"+timeStamp+".xml";
//	   sparkReporter = new ExtentSparkReporter(".\\reports"+repName);
//	   
//	   sparkReporter.config().setDocumentTitle("openCart Automation Report");
//	   sparkReporter.config().setReportName("opencart Functional Testing");
//	   sparkReporter.config().setTheme(Theme.DARK);
//	   
//	   
//	   extent = new ExtentReports();
//	   extent.attachReporter(sparkReporter);
//	   extent.setSystemInfo("Application", "opencart");
//	   extent.setSystemInfo("Module", "Admin");
//	   extent.setSystemInfo("Sub Module", "Customers");
//	   extent.setSystemInfo("User Name", System.getProperty("user.name"));
//	   extent.setSystemInfo("Environment", "QA");
//	   
//	   String os = testContext.getCurrentXmlTest().getParameter("os");
//	   extent.setSystemInfo("Operating System", os);
//	   
//	   String browser = testContext.getCurrentXmlTest().getParameter("browser");
//	   extent.setSystemInfo("Browser", browser);
//	   
//	   List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
//	   
//	   if(!includedGroups.isEmpty()) {
//		   extent.setSystemInfo("Groups", includedGroups.toString());
//	   }
//	   
//   }
   
   public void onTestSuccess(ITestResult result) {
	   
	   test = extent.createTest(result.getTestClass().getName());
	   test.assignCategory(result.getMethod().getGroups());
	   test.log(Status.PASS, result.getName()+" got successfully executed");
	   
   }
   
   public void onTestFailure(ITestResult result) {
	   
	   test = extent.createTest(result.getTestClass().getName());
	   test.assignCategory(result.getMethod().getGroups());
	   test.log(Status.FAIL, result.getName()+"got failed");
	   test.log(Status.INFO, result.getThrowable().getMessage());
	   
	   try{
		   String imgPath = new BaseClass().captureScreen(result.getName());
	       test.addScreenCaptureFromPath(imgPath);
	   }
	   catch(IOException e1){
		   e1.printStackTrace();
	   }
	   
   }
   
   public void onTestSkipped(ITestResult result) {
	   test = extent.createTest(result.getTestClass().getName());
	   test.assignCategory(result.getMethod().getGroups());
	   test.log(Status.SKIP, result.getName()+"got skipped");
	   test.log(Status.INFO, result.getThrowable().getMessage());
	   
   }
   
   public void onFinish (ITestResult result) {
	   
	   extent.flush();
	   
	   //open report automatically
	   String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+repName;
	   File extentReport = new File(pathOfExtentReport);
	   
	   try {
		   Desktop.getDesktop().browse(extentReport.toURI());
	   }
	   catch(IOException e){
		   e.printStackTrace();
	   }
	   
   }
   
}
