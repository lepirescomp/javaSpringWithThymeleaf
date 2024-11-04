package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Random;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		System.setProperty("webdriver.chrome.driver","/Users/leticiapires/Downloads/chromedriver-mac-arm64/chromedriver");
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		WebElement successmsg = null;
		WebElement errormsg = null;
		try {
			 successmsg = driver.findElement(By.id("success-msg"));
			 errormsg = driver.findElement(By.id("error-msg"));
		} catch(Exception e) {

		}

		if(successmsg != null){
			Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("successfully"));
		}
		if(errormsg != null) {
			Assertions.assertTrue(driver.findElement(By.id("error-msg")).getText().contains("already"));
		}

	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	public String createRandomString(){
		return RandomStringUtils.randomAlphanumeric(3).toUpperCase();
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp(createRandomString(),createRandomString(),createRandomString(),createRandomString());
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		String username = createRandomString();
		String password = createRandomString();
		doMockSignUp(createRandomString(),createRandomString(),username,password);
		doLogIn(username, password);

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));

		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		String username = createRandomString();
		String password = createRandomString();
		doMockSignUp(createRandomString(),createRandomString(),username,password);
		doLogIn(username, password);

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("file")));
		WebElement fileSelectButton = driver.findElement(By.id("file"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void testAddNote() {
		// Create a test account
		String username = createRandomString();
		String password = createRandomString();
		doMockSignUp(createRandomString(),createRandomString(),username,password);
		doLogIn(username, password);

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		driver.findElement(By.xpath("//*[@id='nav-notes-tab']")).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNoteButton")));
		WebElement addNoteButton = driver.findElement(By.id("addNoteButton"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteTitle")));
		WebElement noteTitle = driver.findElement(By.name("noteTitle"));
		noteTitle.sendKeys("TestSelenium1Title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteDescription")));
		WebElement noteDescription = driver.findElement(By.name("noteDescription"));
		noteDescription.sendKeys("TestSelenium1Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSubmit2")));
		WebElement noteSubmit = driver.findElement(By.id("noteSubmit2"));
		noteSubmit.click();

		Assertions.assertTrue(driver.getPageSource().contains("TestSelenium1Title"));
		Assertions.assertTrue(driver.getPageSource().contains("TestSelenium1Description"));

	}

	@Test
	public void testEditNote() {
		// Create a test account
		String username = createRandomString();
		String password = createRandomString();
		doMockSignUp(createRandomString(),createRandomString(),username,password);
		doLogIn(username, password);

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		driver.findElement(By.xpath("//*[@id='nav-notes-tab']")).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNoteButton")));
		WebElement addNoteButton = driver.findElement(By.id("addNoteButton"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteTitle")));
		WebElement noteTitle = driver.findElement(By.name("noteTitle"));
		noteTitle.sendKeys("TestSelenium1Title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteDescription")));
		WebElement noteDescription = driver.findElement(By.name("noteDescription"));
		noteDescription.sendKeys("TestSelenium1Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSubmit2")));
		WebElement noteSubmit = driver.findElement(By.id("noteSubmit2"));
		noteSubmit.click();

		driver.findElement(By.xpath("//*[@id='nav-notes-tab']")).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editNote")));
		WebElement addNoteButton1 = driver.findElement(By.id("editNote"));
		addNoteButton1.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteTitle")));
		WebElement noteTitle1 = driver.findElement(By.name("noteTitle"));
		noteTitle1.sendKeys("TestSelenium1TitleModified");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteDescription")));
		WebElement noteDescription1 = driver.findElement(By.name("noteDescription"));
		noteDescription1.sendKeys("TestSelenium1DescriptionModified");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSubmit2")));
		WebElement noteSubmit1 = driver.findElement(By.id("noteSubmit2"));
		noteSubmit1.click();

		driver.findElement(By.xpath("//*[@id='nav-notes-tab']")).click();

		Assertions.assertTrue(driver.getPageSource().contains("Modified"));

	}
	@Test
	public void testDeleteNote() {
		// Create a test account
		String username = createRandomString();
		String password = createRandomString();
		doMockSignUp(createRandomString(),createRandomString(),username,password);
		doLogIn(username, password);

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		driver.findElement(By.xpath("//*[@id='nav-notes-tab']")).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNoteButton")));
		WebElement addNoteButton = driver.findElement(By.id("addNoteButton"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteTitle")));
		WebElement noteTitle = driver.findElement(By.name("noteTitle"));
		noteTitle.sendKeys("TestSelenium1Title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("noteDescription")));
		WebElement noteDescription = driver.findElement(By.name("noteDescription"));
		noteDescription.sendKeys("TestSelenium1Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSubmit2")));
		WebElement noteSubmit = driver.findElement(By.id("noteSubmit2"));
		noteSubmit.click();

		driver.findElement(By.xpath("//*[@id='nav-notes-tab']")).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteNote")));
		WebElement deleteNoteButton1 = driver.findElement(By.id("deleteNote"));
		deleteNoteButton1.click();

		driver.findElement(By.xpath("//*[@id='nav-notes-tab']")).click();

		Assertions.assertFalse(driver.getPageSource().contains("TestSelenium1Description"));
		Assertions.assertFalse(driver.getPageSource().contains("TestSelenium1Title"));

	}

	@Test
	public void testAddCredential() {
		// Create a test account
		String username = createRandomString();
		String password = createRandomString();
		doMockSignUp(createRandomString(),createRandomString(),username,password);
		doLogIn(username, password);

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		driver.findElement(By.xpath("//*[@id='nav-credentials-tab']")).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredentialButton")));
		WebElement addNoteButton = driver.findElement(By.id("addCredentialButton"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.sendKeys("testUrl");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.sendKeys("testUsername");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys("testPassword");


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSubmit2")));
		WebElement noteSubmit = driver.findElement(By.id("credentialSubmit2"));
		noteSubmit.click();

		driver.findElement(By.xpath("//*[@id='nav-credentials-tab']")).click();

		Assertions.assertTrue(driver.getPageSource().contains("testUrl"));

	}

	@Test
	public void testEditCredential() {
		// Create a test account
		String username = createRandomString();
		String password = createRandomString();
		doMockSignUp(createRandomString(),createRandomString(),username,password);
		doLogIn(username, password);

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		driver.findElement(By.xpath("//*[@id='nav-credentials-tab']")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredentialButton")));
		WebElement addNoteButton = driver.findElement(By.id("addCredentialButton"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.sendKeys("testUrl");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.sendKeys("testUsername");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys("testPassword");


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSubmit2")));
		WebElement noteSubmit = driver.findElement(By.id("credentialSubmit2"));
		noteSubmit.click();

		driver.findElement(By.xpath("//*[@id='nav-credentials-tab']")).click();


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialEdit")));
		WebElement addNoteButton1 = driver.findElement(By.id("credentialEdit"));
		addNoteButton1.click();


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrl2 = driver.findElement(By.id("credential-url"));
		credentialUrl2.sendKeys("testUrl-modified");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsername2 = driver.findElement(By.id("credential-username"));
		credentialUsername2.sendKeys("testUsernamemodified");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPassword2 = driver.findElement(By.id("credential-password"));
		credentialPassword2.sendKeys("testPasswordmodified");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSubmit2")));
		WebElement noteSubmit2 = driver.findElement(By.id("credentialSubmit2"));
		noteSubmit2.click();

		driver.findElement(By.xpath("//*[@id='nav-notes-tab']")).click();

		Assertions.assertTrue(driver.getPageSource().contains("modified"));

	}

	@Test
	public void testDeleteCredential() {
		// Create a test account
		String username = createRandomString();
		String password = createRandomString();
		doMockSignUp(createRandomString(),createRandomString(),username,password);
		doLogIn(username, password);

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";


		driver.findElement(By.xpath("//*[@id='nav-credentials-tab']")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredentialButton")));
		WebElement addNoteButton = driver.findElement(By.id("addCredentialButton"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.sendKeys("testUrl");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.sendKeys("testUsername");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys("testPassword");


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSubmit2")));
		WebElement noteSubmit = driver.findElement(By.id("credentialSubmit2"));
		noteSubmit.click();

		driver.findElement(By.xpath("//*[@id='nav-credentials-tab']")).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialDelete")));
		WebElement deleteNoteButton1 = driver.findElement(By.id("credentialDelete"));
		deleteNoteButton1.click();

		Assertions.assertFalse(driver.getPageSource().contains("testPassword"));
	}
}
