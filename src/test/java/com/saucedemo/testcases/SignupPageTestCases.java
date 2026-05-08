package com.saucedemo.testcases;

import org.testng.annotations.Test;

import com.generic.library.TestBase;

import pageobjects.SignupPage;

public class SignupPageTestCases extends TestBase{
	SignupPage lm;
	
	@Test
	public void validateLogin() throws InterruptedException {
		lm= new SignupPage();
		lm.Signup_Flow();
	}
	
	@Test
	public void validateSignup() throws InterruptedException {
		lm= new SignupPage();
		lm.Signup_Flow();
	}
	
}
