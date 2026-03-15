package com.generic.library;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitMethods extends TestBase {
	public WebDriverWait wait;
	private static final int DEFAULT_TIMEOUT = 10; 
	
	public WaitMethods(WebDriver driver) {
		this.wait= new WebDriverWait(driver,Duration.ofSeconds(DEFAULT_TIMEOUT));
	}
	
	
	public void wait_element(By el) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(el));
	}
	
	public void wait_in_Seconds(int seconds) throws InterruptedException {
		Thread.sleep(seconds*1000);
	}
	
	

}
