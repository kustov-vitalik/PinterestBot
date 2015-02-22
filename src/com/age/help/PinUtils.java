package com.age.help;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.age.pinterest.config.PinterestAccount;

public class PinUtils {

	private static final Logger logger =  Logger.getLogger(PinUtils.class);
	private static final String PATH_TO_CHROME_DRIVER = "chrome\\chromedriver.exe";
	private static final String PATH_TO_PAHNTOM_DRIVER = "phantomjs\\phantomjs.exe";
	private static final String LOGIN_URL = "https://pinterest.com/login/";

	public static ChromeDriver getChrome() {
		System.setProperty("webdriver.chrome.driver", PATH_TO_CHROME_DRIVER);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability("permissions.default.stylesheet", "2");
		cap.setCapability("permissions.default.image", "2");
		cap.setCapability("dom.ipc.plugins.enabled.libflashplayer.so", "false");
		ChromeDriver chrome = new ChromeDriver(cap);
		chrome.get("https://pinterest.com/");
		return chrome;
	}

	public static HtmlUnitDriver getHtmlUnit() {
		HtmlUnitDriver driver = new HtmlUnitDriver(DesiredCapabilities.firefox());
		driver.setJavascriptEnabled(true);
		return driver;
	}

	public static FirefoxDriver getFirefoxDriver() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("permissions.default.stylesheet", "2");
		profile.setPreference("permissions.default.image", "2");
		FirefoxDriver driver = new FirefoxDriver(profile);
		driver.get("https://pinterest.com/");
		return driver;
	}

	// public static PhantomJSDriver getPhantomDriver() {
	// System.setProperty("http.proxyHost", "162.222.178.194");
	// System.setProperty("http.proxyPort", "80");
	// DesiredCapabilities caps = new DesiredCapabilities();
	// caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
	// PATH_TO_PAHNTOM_DRIVER);
	// return new PhantomJSDriver(caps);
	// }

	public static void login(WebDriver driver, PinterestAccount account) {
		String email = account.getEmail();
		String password = account.getPassword();
		logger.info("Loggin in with user " + email);
		logger.info("Password is " + password);
		try {
			driver.get(LOGIN_URL);
			PinUtils.waitForWithTimeout(By.name("username_or_email"), driver, 1000 * 20).sendKeys(email);
			PinUtils.waitForWithTimeout(By.name("password"), driver, 1000 * 20).sendKeys(password);
			PinUtils.waitForWithTimeout(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div/div/form/div[4]/div/button"), driver, 1000 * 20).click();
		} catch (Exception e) {
			logger.error("Failed to login",e);
		}
		logger.info("Logged as " + email);
	}

	public static void waitForElement(By by, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(by));
	}

	public static WebElement waitForWithTimeout(By by, WebDriver driver, long timeout) {
		long now = System.currentTimeMillis();
		WebElement element = null;
		while (driver.findElements(by).size() <= 0 && System.currentTimeMillis() - now < timeout) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.error("",e);
			}
		}
		try {
			element = driver.findElement(by);
		} catch (Exception e) {
			logger.error("waitForWithTimeout exceptio",e);
		}
		return element;
	}

	public static WebElement waitFor(By by, WebDriver driver) {
		while (driver.findElements(by).size() <= 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.error("",e);
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
