package com.age.pinterest.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.WebDriver;

import com.age.help.DescriptionGenerator;
import com.age.help.FileUtill;
import com.age.help.ImageScraper;
import com.age.help.PinUtils;
import com.age.pinterest.bot.PinBot;
import com.age.pinterest.config.Pin;

public class BasicPinCreate {
	
	public static String IMAGES_DIR = "D:/image";
	public static String DESCRIPTIONS_DIR = "D:/description";
	public static String TEST_DIR = "D:/testDir";
	public final String PINS_ROOT = TEST_DIR;  //todo:::
	
	private DescriptionGenerator descrpGen;
	private WebDriver driver;
	private ImageScraper imgScrap;
	private String keyword;

	public BasicPinCreate(String keyword){
		this.driver = PinUtils.getChrome();
		this.keyword = keyword;
		descrpGen = new DescriptionGenerator(driver,DESCRIPTIONS_DIR);
		imgScrap = new ImageScraper(this.driver,this.keyword,this.keyword);
	}
	
	public String generateContent() throws InterruptedException, IOException{	
		descrpGen.buildDescriptionList(this.keyword);
		imgScrap.scan();
		return "Done Generating.Go and delete ugly images";	
	}
	
	public void createPins(int numberOfPins) throws InterruptedException, IOException{
		int currentPinNumber = 0;
		
		generateContent();
		System.out.println("Remove ugly images and press any key to continue");
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		int ch = stdin.read();
		if(ch !=-1){
			try {
				ArrayList<String> imagesPaths = FileUtill.getAllFiles(IMAGES_DIR + "/" + this.keyword);
				ArrayList<String> descriptionsPaths = FileUtill.getAllFiles(DESCRIPTIONS_DIR + "/" + this.keyword);
				Iterator<String> imageItr = imagesPaths.iterator();
				Iterator<String> descriptionItr = descriptionsPaths.iterator();
					while(imageItr.hasNext()){
							Pin pin = new Pin();
							pin.setDescription(descriptionItr.next());
							pin.setSource(" ");
							pin.setImage(imageItr.next());
							ObjectMapper mapper = new ObjectMapper();
							String path = PINS_ROOT + "/" + this.keyword + "/";
							File file = new File(PINS_ROOT + "/" + this.keyword + "/");
							file.mkdirs();
							path = path + Integer.toString(currentPinNumber) + ".json";
						    mapper.writeValue(new File(path), pin);
						    currentPinNumber++;
						    if(!descriptionItr.hasNext()){
						    	descriptionItr=descriptionsPaths.iterator();
						    	}
						    if(currentPinNumber ==numberOfPins){
						    	break;
						    }
					}
				}catch (Exception e) {
				System.out.println("Failed to set up pins.  " + e.getMessage());
			}
		}
	}
		
	public static void main(String args[]) throws InterruptedException, IOException{
		String keyword = "dress";
		BasicPinCreate bPinGen = new BasicPinCreate(keyword);
		bPinGen.createPins(35);
	}
}
