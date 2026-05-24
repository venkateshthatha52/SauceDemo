package pageobjects;

import org.openqa.selenium.By;
import com.generic.library.GenericMethods;

public class SignupPage extends GenericMethods {

	By signupButton = By.id("customer_register_link");
	
	public SignupPage() {
		super();
	}

	public void Signup_Flow() throws InterruptedException {
		clickUsingJS(signupButton,"Signup Button");
		Thread.sleep(5000);
	}
	
	

}
