package com.age.pinterest.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.age.pinterest.bot.FileUtill;
import com.age.pinterest.bot.ImageScraper;
import com.age.pinterest.bot.Pin;
import com.age.pinterest.bot.PinUtils;

public class PinTask extends Task {
	private static final String DESCRIPTIONS_LOCATION = "D:\\Descriptions\\";
	private static final String BOARDS_URL_FORMAT = "http://www.pinterest.com/%s/%s";
	private final String boardsLocation;
	private final String board;
	private final WebDriver driver;
	private final String location;
	private final String source;
	private final String keywords;
	private final String tag;
	Iterator<String> descrIter;
	Iterator<String> imgIter;
	List<String> descrList;
	List<String> imgList;

	public PinTask(WebDriver driver, String boardsLocation, String board, String location, String source, String keywords, String tag, long interval)
			throws IOException, InterruptedException {
		super(interval);
		this.boardsLocation = boardsLocation;
		this.board = board;
		this.location = location;
		this.source = source;
		this.driver = driver;
		this.keywords = keywords.replaceAll(" ", "+");
		this.tag = tag;
		configure();
	}

	private void configure() throws IOException, InterruptedException {
		ImageScraper scrapper = new ImageScraper(driver, keywords, tag);
		scrapper.scan();
		imgList = FileUtill.getAllFiles(scrapper.getDownloadLocation());
		this.buildDescriptionList(tag);
		descrList = FileUtill.getFileListContents(DESCRIPTIONS_LOCATION + tag);
		descrIter = descrList.iterator();
		imgIter = imgList.iterator();
	}

	public void pin() {
		if (this.intervalPassed()) {
			return;
		}
		String description = null;
		String image = null;
		if (descrIter.hasNext()) {
			description = descrIter.next();
		} else {
			descrIter = descrList.iterator();
		}
		if (imgIter.hasNext()) {
			image = imgIter.next();
		} else {
			imgIter = imgList.iterator();
		}
		this.pinLocalFile(new Pin(board, image, description, location, source));

	}

	private void pinLocalFile(Pin pin) {
		try {
			String description = pin.getDescription();
			String board = pin.getBoard();
			String pathToFile = pin.getPathToFile();
			String location = pin.getLocation();
			String source = pin.getSource();

			System.out.println("Pinning to board: " + board);

			driver.navigate().to(String.format("http://www.pinterest.com/%s/%s", boardsLocation, board));

			String ccsPath = "body > div.App.full.AppBase.Module > div.appContent > div.mainContainer > div.BoardPage.Module > div.locationBoardPageContentWrapper > div > div.hasFooter.Grid.Module > div.padItems.Module.centeredWithinWrapper.GridItems.variableHeightLayout > div:nth-child(1) > a";
			this.waitFor(By.cssSelector(ccsPath), driver).click();

			String uploadBtnCss = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > form > div > button.Module.Button.borderless.addPinButton.addPinUpload.hasText";
			this.waitFor(By.cssSelector(uploadBtnCss), driver).click();

			String fileCss = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > form > div > div.Module.Button.btn.large.primary.leftRounded.hasText > input[type=\"file\"]";
			this.waitFor(By.cssSelector(fileCss), driver).sendKeys(pathToFile);

			String descriptionId = "pinFormDescription";
			this.waitFor(By.id(descriptionId), driver).sendKeys(description);

			String cssPath = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > div > form > div.formFooter > div.formFooterButtons > button.Module.Button.btn.rounded.primary.repinSmall.pinIt";
			this.waitFor(By.cssSelector(cssPath), driver).click();

			List<WebElement> ls = this.getPinList(board);
			ls.get(0).click();

			String editXpaht = "/html/body/div[1]/div[2]/div[2]/div/div[1]/div/div[2]/button[1]";
			this.waitFor(By.xpath(editXpaht), driver).click();

			String sourceId = "pinFormLink";
			this.waitFor(By.id(sourceId), driver).sendKeys(source);

			String cityXpaht = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > div > form > ul > li.placeWrapper > div > div > div > div > input";
			WebElement city = this.waitFor(By.cssSelector(cityXpaht), driver);
			city.sendKeys(location);
			Actions act = new Actions(driver);
			Thread.sleep(1000);
			act.moveToElement(city).moveByOffset(10, 10).click().perform();

			String saveBtnXpath = "html/body/div[8]/div[2]/div/div/div/div/div/form/div[2]/div[2]/button[2]";
			this.waitFor(By.xpath(saveBtnXpath), driver).click();

			File f = new File(pathToFile);
			if (f.exists()) {
				f.delete();
			}
		} catch (Exception e) {
			System.out.println("Pin failed  " + e.getMessage());
		}
		System.out.println("Pin sucessfull");
	}

	private void buildDescriptionList(String keyword) throws FileNotFoundException, UnsupportedEncodingException {
		String num = "";
		String urlFormat = "http://www.brainyquote.com/quotes/topics/topic_" + keyword + "%s.html";
		driver.get(String.format(urlFormat, num));
		for (int i = 2; i <= 2; i++) {
			PinUtils.waitForElement(By.id("quotesList"), driver);
			List<WebElement> elements = driver.findElement(By.id("quotesList")).findElements(By.cssSelector("*"));
			for (WebElement we : elements) {
				try {
					if (we.getTagName().equals("span")) {
						String quote = we.getText();
						if (quote.length() > 0 && !quote.equals("Share")) {
							FileUtill.saveToFile(DESCRIPTIONS_LOCATION + keyword, System.currentTimeMillis() + ".txt", quote);
							System.out.println(quote);
						}
					}
				} catch (Exception e) {
				}

			}
			num = Integer.toString(i);
			driver.get(String.format(urlFormat, num));
		}
	}

	private List<WebElement> getPinList(String board) {
		driver.navigate().to(String.format(BOARDS_URL_FORMAT, this.boardsLocation, board));
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

	private WebElement waitFor(By by, WebDriver driver) {
		while (driver.findElements(by).size() <= 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return driver.findElement(by);
	}

	@Override
	public void execute() {
		this.pin();

	}

}
