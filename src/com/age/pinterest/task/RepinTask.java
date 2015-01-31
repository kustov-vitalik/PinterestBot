package com.age.pinterest.task;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.age.help.FileUtill;
import com.age.help.PinUtils;

public class RepinTask extends Task {

	private final String folder;
	private final WebDriver driver;

	public RepinTask(String folder, WebDriver driver) {
		this.folder = folder;
		this.driver = driver;
	}

	@Override
	public void execute() {
		try {
			while (true) {
				pinFromFolder();
				Thread.sleep(1000 * 10);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void pinFromFolder() throws IOException, InterruptedException {
		File dir = new File(folder);
		for (File f : dir.listFiles()) {
			String pin = FileUtill.getFileContents(f.getAbsolutePath());
			driver.get(pin);
			PinUtils.waitForPage(driver);
			String xpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[1]/div/div[1]/button[1]/em";
			PinUtils.waitFor(By.xpath(xpath), driver).click();
			PinUtils.waitForPage(driver);
			String cssInput = "body > div.Module.Modal.webNewContentNewRepin.absoluteCenter.show > div.modalScroller > div > div > div > div > div.boardsWrapper > div.Module.BoardPicker.ui-Picker.pinCreate3 > div.top > div > input";
			PinUtils.waitFor(By.cssSelector(cssInput), driver).sendKeys("Jewelry");
			String cssList = "body > div.Module.Modal.webNewContentNewRepin.absoluteCenter.show > div.modalScroller > div > div > div > div > div.boardsWrapper > div.Module.BoardPicker.ui-Picker.pinCreate3 > div.Module.ui-SelectList.pinCreate3 > div > ul.section.allBoards > ul";
			WebElement sugestion = PinUtils.waitFor(By.cssSelector(cssList), driver);
			PinUtils.waitForPage(driver);
			for (WebElement element : sugestion.findElements(By.cssSelector("*"))) {
				try {
					if (element.getText().equals("Jewelry")) {
						element.click();
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			f.delete();
		}
	}

	private void pinFromFolder2() throws IOException, InterruptedException {
		File dir = new File(folder);
		for (File f : dir.listFiles()) {
			String pin = FileUtill.getFileContents(f.getAbsolutePath());
			driver.get(pin);
			PinUtils.waitForPage(driver);
			String xpath = "/html/body/div[1]/div[2]/div[1]/div[2]/div[1]/div/div[1]/button[1]/em";
			PinUtils.waitFor(By.xpath(xpath), driver).click();
			PinUtils.waitForPage(driver);
			String cssPinIt = "body > div.Module.Modal.absoluteCenter.show > div.modalScroller > div > div > div > div > div > form > div.formFooter > div.formFooterButtons > button.Module.Button.btn.rounded.primary.repinSmall.pinIt > em";
			PinUtils.waitFor(By.cssSelector(cssPinIt), driver).click();
			f.delete();
		}
	}
}
