package com.generic.library;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.aventstack.extentreports.Status; 

public class GenericMethods extends TestBase{
	 WaitMethods wait;
	 private JavascriptExecutor js;
	 
	 
	 public GenericMethods() {
		 	this.wait= new WaitMethods(getDriver());
	        this.js= (JavascriptExecutor) getDriver();
	 }

//********************* Common Actions ************************************

	    public WebElement getElement(By el) {
	        return getDriver().findElement(el);
	    }
	    
	    public void clickUsingJS(By el,String elename) {
	    	try {
	    	wait.wait_element(el);
	    	js.executeScript("arguments[0].click();", getElement(el));
	    	log(Status.INFO, "Clicked on element: " + elename);
	    	}catch(Exception e) {
	    		 e.printStackTrace();
	    		 Assert.fail(e.getMessage()); 
	    	}
	    }
	    
	    protected void log(Status status, String message) {
	        if (getTest() != null) {
	        	getTest().log(status, message);
	        }
	        System.out.println("[" + status + "] " + message);
	    }

}
