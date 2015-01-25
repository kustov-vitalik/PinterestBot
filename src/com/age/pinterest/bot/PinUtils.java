package com.age.pinterest.bot;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PinUtils {
	private static final String PATH_TO_CHROME_DRIVER = "D:\\chromedriver.exe";
	private static final String PATH_TO_PAHNTOM_DRIVER = "D:\\phantomjs\\phantomjs.exe";
	private static final String LOGIN_URL = "https://pinterest.com/login/";

	public static ChromeDriver getChrome() {
		System.setProperty("webdriver.chrome.driver", PATH_TO_CHROME_DRIVER);
		return new ChromeDriver();
	}

	public static HtmlUnitDriver getHtmlUnit() {
		return new HtmlUnitDriver();
	}

	public static PhantomJSDriver getPhantomDriver() {
		System.setProperty("http.proxyHost", "162.222.178.194");
		System.setProperty("http.proxyPort", "80");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PATH_TO_PAHNTOM_DRIVER);
		return new PhantomJSDriver(caps);
	}

	public static void login(WebDriver driver, String email, String password) {
		System.out.println("Loggin in with user " + email);
		System.out.println("Password is " + password);
		try {
			driver.get(LOGIN_URL);
			while (driver.findElements(By.name("username_or_email")).size() <= 0) {
				Thread.sleep(400);
			}
			driver.findElement(By.name("username_or_email")).sendKeys(email);
			driver.findElement(By.name("password")).sendKeys(password);
			driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div/div/form/div[4]/div/button")).click();
		} catch (Exception e) {
			System.out.println("Failed to login");
			e.printStackTrace();
		}
		System.out.println("Logged as " + email);
	}

	public static void waitForElement(By by, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(by));
	}

	public static WebElement waitFor(By by, WebDriver driver) {
		while (driver.findElements(by).size() <= 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return driver.findElement(by);
	}

	public static void waitForPage(WebDriver driver) throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		while (!js.executeScript("return document.readyState").equals("complete")) {
			Thread.sleep(500);
		}

	}

}
