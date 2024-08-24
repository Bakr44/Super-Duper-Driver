package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.time.Duration;
import java.util.List;

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
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.close();
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testHome() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Home", driver.getTitle());
    }

    @Test
    public void testSignupPage() {
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    public void testUnauthorizedResult() {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        driver.get("http://localhost:" + this.port + "/home/result");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button")));
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testSignupLoginLogout() {
        doMockSignUp("Bakr", "Hawsawi", "tester", "444");

        doLogIn("tester", "444");


        WebElement logoutButton = driver.findElement(By.id("logout-button"));
        logoutButton.click();

        Assertions.assertNotEquals("Home", driver.getTitle());
        Assertions.assertEquals("Login", driver.getTitle());
    }


    public void clickNotesTab() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        driver.get("http://localhost:" + this.port + "/home");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        driver.findElement(By.id("nav-notes-tab")).click();
    }

    @Test
    public void testAddNote() {
        doMockSignUp("Bakr", "Test", "tester", "444");


        doLogIn("tester", "444");

        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
        Assertions.assertTrue(driver.findElement(By.id("nav-notes")).isDisplayed());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-button")));
        driver.findElement(By.id("add-note-button")).click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        driver.findElement(By.id("note-title")).sendKeys("Test Note");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        driver.findElement(By.id("note-description")).sendKeys("testing ...");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-note-button")));
        driver.findElement(By.id("submit-note-button")).click();

        clickNotesTab();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        Assertions.assertTrue(driver.findElement(By.id("t-note-title")).getText().contains("Test Note"));
    }

    @Test
    public void testEditNote() {
        testAddNote();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 4);


        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note-button")));
        driver.findElement(By.id("edit-note-button")).click();


        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement descriptionField = driver.findElement(By.id("note-description"));
        descriptionField.click();
        descriptionField.clear();
        descriptionField.sendKeys("edited description");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-note-button")));
        WebElement submitNote = driver.findElement(By.id("submit-note-button"));
        submitNote.click();

        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("edit-note-button")));

        clickNotesTab();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        String actual = driver.findElement(By.id("t-note-description")).getText();
        System.out.println("Actual description: " + actual);
        Assertions.assertTrue(actual.contains("edited description"));
    }


    @Test
    public void deleteNote() {

        testAddNote();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note-button")));
        driver.findElement(By.id("delete-note-button")).click();

        clickNotesTab();

        WebElement notesTable = driver.findElement(By.id("userTable"));
        List<WebElement> notesList = notesTable.findElements(By.tagName("tbody"));

        Assertions.assertEquals(0, notesList.size());
    }


    public void clickCredentialsTab() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        driver.get("http://localhost:" + this.port + "/home");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        driver.findElement(By.id("nav-credentials-tab")).click();
    }

    @Test
    public void addCredential() {
        doMockSignUp("Bakr", "Test", "tester", "444");

        doLogIn("tester", "444");

        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String inputCredentialPassword = "1234";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials-button-add")));
        WebElement addCredentialsButton = driver.findElement(By.id("credentials-button-add"));
        addCredentialsButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement inputURL = driver.findElement(By.id("credential-url"));
        inputURL.click();
        inputURL.sendKeys("https://www.google.com/");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement inputUsername = driver.findElement(By.id("credential-username"));
        inputUsername.click();
        inputUsername.sendKeys("Bakr");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement inputPassword = driver.findElement(By.id("credential-password"));
        inputPassword.click();
        inputPassword.sendKeys(inputCredentialPassword);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-Submit")));
        driver.findElement(By.id("credential-Submit")).click();

        clickCredentialsTab();

        WebElement credentialsTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> credList = credentialsTable.findElements(By.tagName("tbody"));

        Assertions.assertEquals(1, credList.size());


        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        Assertions.assertNotEquals(driver.findElement(By.id("table-cred-password")).getText(), inputCredentialPassword);
    }

    @Test
    public void updateCredential() {

        addCredential();
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-credential-button")));
        driver.findElement(By.id("edit-credential-button")).click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        driver.findElement(By.id("credential-url")).click();
        driver.findElement(By.id("credential-url")).clear();
        driver.findElement(By.id("credential-url")).sendKeys("https://github.com");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        String inputPassword = driver.findElement(By.id("credential-password")).getAttribute("value");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-Submit")));
        driver.findElement(By.id("credential-Submit")).click();
        clickCredentialsTab();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        Assertions.assertTrue(driver.findElement(By.id("table-cred-url")).getText().contains("https://github.com/Bakr44"));

        Assertions.assertNotEquals(driver.findElement(By.id("table-cred-password")).getText(), inputPassword);
    }


    @Test
    public void deleteCredential() {

        addCredential();
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Wait for the delete button to be visible and click it
        WebElement deleteButton = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-credential-button")));
        deleteButton.click();

        // Wait for the credential tab to refresh and the table to update
        clickCredentialsTab();

        // Wait until the credential table is visible
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

        // Verify the credential is removed by checking the number of rows in the table
        WebElement credentialTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> rows = credentialTable.findElements(By.tagName("tr"));

        Assertions.assertEquals(1, rows.size(), "Credential was not deleted");

        // Alternatively, you could check for the absence of the deleted credential directly
        boolean isCredentialDeleted = rows.stream().noneMatch(row -> row.getText().contains("Your changes were successfully"));
        Assertions.assertTrue(isCredentialDeleted, "Credential was not deleted from the table");
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/

    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
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
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You have successfully signed up"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
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

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
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


}
