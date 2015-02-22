package com.age.pinterest.task;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.help.BotPaths;
import com.age.help.FileUtill;
import com.age.help.PinUtils;
import com.age.pinterest.api.AccountManager;
import com.age.pinterest.config.Pin;
import com.age.pinterest.config.PinterestAccount;

public class PinTask extends Task {
	private static final Logger logger =  Logger.getLogger(PinTask.class);
	private static final String BOARDS_URL_FORMAT = "http://www.pinterest.com/%s/%s";
	private static final String PINS_LOCATION_URL = BotPaths.ROOT_DIR + "/Users/%s/pins";
	private final long interval;
	private final String board;
	private final PinterestAccount acc;

	public PinTask(PinterestAccount acc, String board, long interval) {
		this.interval = interval;
		this.board = board;
		this.acc = acc;

	}

	@Override
	public void run() {
		WebDriver driver = PinUtils.getChrome();
		PinUtils.login(driver, acc);
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<String> pinFiles = FileUtill.getAllFiles(String.format(PINS_LOCATION_URL, acc.getUser()));
		while (!pinFiles.isEmpty()) {
			if (this.intervalPassed(interval)) {
				try {
					String filePath = pinFiles.get(0);
					Pin pin = mapper.readValue(new File(filePath), Pin.class);
					this.pin(pin, driver);
					new File(filePath).delete();
					pinFiles.remove(0);
				} catch (Exception e) {
					logger.error("Failed to pin  " + e.getMessage());
				}
			}
		}
		logger.info("No more pins for " + acc.getUser());
		driver.quit();
	}

	private boolean pin(Pin pin, WebDriver driver) throws InterruptedException, IOException {
		boolean result = false;
		try {
			String pathToFile = pin.getImage();
			logger.info("Pinning to board: " + board);

			driver.navigate().to(String.format(BOARDS_URL_FORMAT, acc.getUser(), board));
			PinUtils.waitForPage(driver);
			String ccsPath = "body > div.App.full.AppBase.Module > div.appContent > div.mainContainer > div.BoardPage.Module > div.locationBoardPageContentWrapper > div > div.hasFooter.Grid.Module > div.padItems.Module.centeredWithinWrapper.GridItems.variableHeightLayout > div:nth-child(1) > a";
			PinUtils.waitFor(By.cssSelector(ccsPath), driver).click();

			String uploadBtnCss = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > form > div > button.Module.Button.borderless.addPinButton.addPinUpload.hasText";
			PinUtils.waitFor(By.cssSelector(uploadBtnCss), driver).click();

			String fileCss = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > form > div > div.Module.Button.btn.large.primary.leftRounded.hasText > input[type=\"file\"]";
			PinUtils.waitFor(By.cssSelector(fileCss), driver).sendKeys(pathToFile);
			String descr = pin.getDescription();
			if (descr.length() > 500) {
				descr = descr.substring(0, 499);
			}
			logger.info("Lenght  " + descr.length());
			String descriptionId = "pinFormDescription";
			PinUtils.waitFor(By.id(descriptionId), driver).sendKeys(descr);
			String cssPath = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > div > form > div.formFooter > div.formFooterButtons > button.Module.Button.btn.rounded.primary.repinSmall.pinIt";
			PinUtils.waitFor(By.cssSelector(cssPath), driver).click();

			PinUtils.waitForPage(driver);
			List<WebElement> ls = this.getPinList(board, driver);
			ls.get(0).click();

			String editXpaht = "/html/body/div[1]/div[2]/div[2]/div/div[1]/div/div[2]/button[1]";
			PinUtils.waitFor(By.xpath(editXpaht), driver).click();

			String sourceId = "pinFormLink";
			PinUtils.waitFor(By.id(sourceId), driver).sendKeys(pin.getSource());

			String saveBtnXpath = "html/body/div[8]/div[2]/div/div/div/div/div/form/div[2]/div[2]/button[2]";
			PinUtils.waitFor(By.xpath(saveBtnXpath), driver).click();

		} catch (Exception e) {
			logger.error("",e);
		}
		return result;
	}

	private List<WebElement> getPinList(String board, WebDriver driver) {
		driver.navigate().to(String.format(BOARDS_URL_FORMAT, acc.getUser(), board));
		String cssPath = "body > div.App.full.AppBase.Module > div.appContent > div.mainContainer > div.Module.BoardPage > div.locationBoardPageContentWrapper > div > div.Module.Grid.hasFooter > div.Module.GridItems.centeredWithinWrapper.padItems.variableHeightLayout";
		PinUtils.waitForElement(By.cssSelector(cssPath), driver);
		ArrayList<WebElement> pins = new ArrayList<WebElement>();
		for (WebElement e : driver.findElement(By.cssSelector(cssPath)).findElements(By.cssSelector("*"))) {
			if (e.getAttribute("class").equals("hoverMask")) {
				pins.add(e);
			}
		}
		return pins;
	}

	private List<Pin> setUpPins() {
		ArrayList<Pin> pins = new ArrayList<Pin>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			for (String pinFile : FileUtill.getAllFiles(String.format(PINS_LOCATION_URL, acc.getUser()))) {
				Pin pin = mapper.readValue(new File(pinFile), Pin.class);
				pins.add(pin);
			}
		} catch (Exception e) {
			logger.error("Failed to set up pins.  " + e.getMessage());
		}
		return pins;
	}

}
