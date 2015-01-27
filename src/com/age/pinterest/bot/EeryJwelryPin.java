package com.age.pinterest.bot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.age.pinterest.config.EeryPin;
import com.age.pinterest.config.PinterestAccount;

public class EeryJwelryPin {
	private static final String BOARDS_URL_FORMAT = "http://www.pinterest.com/%s/%s";
	private final WebDriver driver;
	private final String board;

	public EeryJwelryPin(WebDriver driver, String board) {
		this.driver = driver;
		this.board = board;
	}

	public void generatePin() throws InterruptedException, JsonGenerationException, JsonMappingException, IOException {
		String nextPage = "";
		ArrayList<String> hrefs = new ArrayList<String>();
		ArrayList<EeryPin> pins = new ArrayList<EeryPin>();
		for (int i = 2; true; i++) {
			driver.get("http://eeryjewelry.com/product-category/necklaces/" + nextPage);
			String bodyText = PinUtils.waitFor(By.tagName("body"), driver).getAttribute("class");
			PinUtils.waitForPage(driver);
			if (bodyText.equals("error404")) {
				break;
			}
			WebElement content = PinUtils.waitFor(By.id("content"), driver);
			for (WebElement element : content.findElements(By.tagName("h2"))) {
				WebElement a = element.findElement(By.tagName("a"));
				hrefs.add(a.getAttribute("href"));
			}

			nextPage = "page/" + Integer.toString(i);
		}
		for (String href : hrefs) {
			try {
				driver.navigate().to(href);
				PinUtils.waitForPage(driver);
				System.out.println("IMage");
				String imgXpath = "#kd_image_large_gallery > div.owl-wrapper-outer > div > div:nth-child(1) > div";
				WebElement pic = PinUtils.waitFor(By.cssSelector(imgXpath), driver);
				String image = pic.findElement(By.tagName("a")).getAttribute("href");
				System.out.println("Descr");
				WebElement descrDiv = PinUtils.waitFor(By.id("tab-description"), driver);
				String descr = descrDiv.findElement(By.tagName("p")).getText();

				EeryPin pin = new EeryPin();
				pin.setBoard(board);
				pin.setDescription(descr);
				pin.setSource(href);
				pin.setImage(image);
				pins.add(pin);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		for (int i = 0; i < pins.size(); i++) {
			mapper.writeValue(new File("d:\\Pins\\" + Integer.toString(i) + ".json"), pins.get(i));
		}
	}

	public void startPining() throws InterruptedException, JsonParseException, JsonMappingException, IOException {
		String location = "New York";
		PinterestAccount acc = new PinterestAccount();
		acc.setEmail("globalamericaselfdefensejohn@gmail.com");
		acc.setPassword("Geni0us!");
		acc.setUser("globalamericase");
		PinUtils.login(driver, acc);
		ArrayList<String> files = FileUtill.getAllFiles("d:\\Pins\\");
		ObjectMapper mapper = new ObjectMapper();
		for (String pathToFile : files) {
			EeryPin pin = mapper.readValue(new File(pathToFile), EeryPin.class);
			System.out.println("Pinning to board: " + board);
			BufferedImage image1 = null;
			String tmpFile = "tmp.jpg";
			URL url = new URL(pin.getImage());
			image1 = ImageIO.read(url);
			File file = null;
			if (image1 != null) {
				file = new File(tmpFile);
				file.createNewFile();
				ImageIO.write(image1, "jpg", file);
			}
			driver.navigate().to(String.format("http://www.pinterest.com/%s/%s", acc.getUser(), board));
			PinUtils.waitForPage(driver);
			String ccsPath = "body > div.App.full.AppBase.Module > div.appContent > div.mainContainer > div.BoardPage.Module > div.locationBoardPageContentWrapper > div > div.hasFooter.Grid.Module > div.padItems.Module.centeredWithinWrapper.GridItems.variableHeightLayout > div:nth-child(1) > a";
			PinUtils.waitFor(By.cssSelector(ccsPath), driver).click();

			String uploadBtnCss = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > form > div > button.Module.Button.borderless.addPinButton.addPinUpload.hasText";
			PinUtils.waitFor(By.cssSelector(uploadBtnCss), driver).click();

			String fileCss = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > form > div > div.Module.Button.btn.large.primary.leftRounded.hasText > input[type=\"file\"]";
			PinUtils.waitFor(By.cssSelector(fileCss), driver).sendKeys(file.getAbsolutePath());
			String descr = pin.getDescription();
			if (descr.length() > 500) {
				descr = descr.substring(0, 499);
			}
			System.out.println("Lenght  " + descr.length());
			String descriptionId = "pinFormDescription";
			PinUtils.waitFor(By.id(descriptionId), driver).sendKeys(descr);
			String cssPath = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > div > form > div.formFooter > div.formFooterButtons > button.Module.Button.btn.rounded.primary.repinSmall.pinIt";
			PinUtils.waitFor(By.cssSelector(cssPath), driver).click();

			PinUtils.waitForPage(driver);
			List<WebElement> ls = this.getPinList(board);
			ls.get(0).click();

			String editXpaht = "/html/body/div[1]/div[2]/div[2]/div/div[1]/div/div[2]/button[1]";
			PinUtils.waitFor(By.xpath(editXpaht), driver).click();

			String sourceId = "pinFormLink";
			PinUtils.waitFor(By.id(sourceId), driver).sendKeys(pin.getSource());

			String cityXpaht = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > div > form > ul > li.placeWrapper > div > div > div > div > input";
			WebElement city = PinUtils.waitFor(By.cssSelector(cityXpaht), driver);
			city.sendKeys(location);
			Actions act = new Actions(driver);
			Thread.sleep(1000);
			act.moveToElement(city).moveByOffset(10, 10).click().perform();

			String saveBtnXpath = "html/body/div[8]/div[2]/div/div/div/div/div/form/div[2]/div[2]/button[2]";
			PinUtils.waitFor(By.xpath(saveBtnXpath), driver).click();

			File f = new File(pathToFile);
			if (f.exists()) {
				f.delete();
			}
			Thread.sleep(1000*60*30);
		}

	}

	private List<WebElement> getPinList(String board) {
		driver.navigate().to(String.format(BOARDS_URL_FORMAT, "globalamericase", board));
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
}
