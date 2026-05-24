package com.generic.library;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.Alert;
import java.util.ArrayList;
import java.util.List;
import com.aventstack.extentreports.Status;
import static com.reports.ExtentReportManager.*;

public class GenericMethods extends TestBase {
	WaitMethods wait;
	private JavascriptExecutor js;

	public GenericMethods() {
		this.wait = new WaitMethods(getDriver());
		this.js = (JavascriptExecutor) getDriver();
	}

//********************* Common Actions ************************************

	public WebElement getElement(By el) {
		return getDriver().findElement(el);
	}

	public void clickUsingJS(By el, String elename) {
		try {
			wait.wait_element(el);
			highlightElement(el,elename);
			js.executeScript("arguments[0].click();", getElement(el));
			log(Status.PASS, "Successfully Clicked on " + elename);
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "Failed to Click on " + elename + " - " + e.getMessage());
		}
	}

	public void enterTextUsingJS(By el, String value, String elename) {
		try {
			wait.wait_element(el);
			getElement(el).clear();
			highlightElement(el,elename);
			js.executeScript("arguments[0].value='" + value + "';", getElement(el));
			log(Status.PASS, "Successfully entered text on " + elename);
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "Failed to entered text on " + elename + " - " + e.getMessage());
		}
	}

	public void selectDropdown(By el, String visibleText, String elename) {
		try {
			wait.wait_element(el);
			highlightElement(el,elename);
			Select sel = new Select(getElement(el));
			sel.selectByVisibleText(visibleText);
			log(Status.PASS, "Selected '" + visibleText + "' on " + elename);
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "Failed to select '" + visibleText + "' on " + elename + " - " + e.getMessage());
		}
	}

	public void acceptAlert() {
		try {
			Alert alert = wait.waitForAlert();
			alert.accept();
			log(Status.PASS, "Accepted alert successfully");
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "No alert to accept - " + e.getMessage());
		}
	}

	public void dismissAlert() {
		try {
			Alert alert = wait.waitForAlert();
			alert.accept();
			log(Status.PASS, "Dismessed alert successfully");
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "No alert to dismiss - " + e.getMessage());
		}
	}

	public String getAlertText() {
		try {
			Alert alert = wait.waitForAlert();
			String text = alert.getText();
			log(Status.PASS, "Got alert text on : '" + text + "'");
			return text;
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "No alert present to get text on - " + e.getMessage());
			return null;
		}
	}

	public boolean enterTextInAlert(String input) {
		try {
			Alert alert = wait.waitForAlert();
			alert.sendKeys(input);
			alert.accept();
			log(Status.PASS, "Entered text into alert on : '" + input + "'");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "Failed to enter text into alert on - " + e.getMessage());
			return false;
		}
	}

	public void switchToChildWindow() {
		String parentHandle = getDriver().getWindowHandle();
		for (String handle : getDriver().getWindowHandles()) {
			if (!handle.equals(parentHandle)) {
				getDriver().switchTo().window(handle);
				log(Status.PASS, "Switched to child window with title: '" + getDriver().getTitle() + "'");
			}
		}
		log(Status.FAIL, "No child window found");
	}

	public void switchToWindowByIndex(int wid) {
		try {
			List<String> handles = new ArrayList<>(getDriver().getWindowHandles());
			if (wid < 0 || wid >= handles.size()) {
				log(Status.FAIL, "Invalid window index: " + wid + ". Available windows: " + handles.size());
			}
			String handle = handles.get(wid);
			getDriver().switchTo().window(handle);
			log(Status.PASS, "Switched to window index " + wid + " with title: '" + getDriver().getTitle() + "'");
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "Failed to switch to window index " + wid + " - " + e.getMessage());
		}
	}

	public void switchToFrame(By el) {
		try {
			getDriver().switchTo().frame(getElement(el));
			log(Status.PASS, "Switched to frame");
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "Failed to switch to frame - " + e.getMessage());
		}
	}

	public void switchToDefaultContent() {
		try {
			getDriver().switchTo().defaultContent();
			log(Status.PASS, "Switched to default content");
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "Failed to switch to default content - " + e.getMessage());
		}
	}

	public String getElementText(By el, String elename) {
		try {
			wait.wait_element(el);
			String text = getElement(el).getText();
			log(Status.PASS, "Got text from " + elename + " : '" + text + "'");
			return text;
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "Failed to get text from " + elename + " - " + e.getMessage());
			return null;
		}
	}

	public void scrollUntilVisible(By el, String elename) {
		try {
			WebElement element = getElement(el);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			e.printStackTrace();
			log(Status.FAIL, "Failed while scrolling to " + elename + " - " + e.getMessage());
		}
	}

	public void highlightElement(By element, String elename) {
		try {
			String originalStyle = getElement(element).getAttribute("style");
			js.executeScript("arguments[0].setAttribute('style', 'border: 3px solid red; background: yellow;');", getElement(element));
			Thread.sleep(500); // Highlight for 0.5 seconds
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", getElement(element), originalStyle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
