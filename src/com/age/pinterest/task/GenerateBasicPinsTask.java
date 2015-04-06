package com.age.pinterest.task;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.codehaus.jackson.map.ObjectMapper;

import com.age.data.PinData;
import com.age.help.BotPaths;
import com.age.help.DescriptionGenerator;
import com.age.help.FileUtill;
import com.age.ui.Log;

public class GenerateBasicPinsTask extends Task {
	private final String tag;
	private final String source;

	public GenerateBasicPinsTask(String tag, String source) {
		this.tag = tag;
		this.source = source;
	}

	@Override
	public void run() {
		try {
			Log.log("Generating pins for " + tag);
			List<String> imagePaths = FileUtill.getAllFiles(BotPaths.IMAGES_ROOT + tag);
			DescriptionGenerator generator = new DescriptionGenerator();
			List<String> quotes = generator.getQuotes(tag);
			Log.log("Will generate " + imagePaths.size() + " pins");
			Iterator<String> imageIter = imagePaths.iterator();
			Iterator<String> quoteIter = quotes.iterator();
			ObjectMapper mapper = new ObjectMapper();
			Random r = new Random();
			while (imageIter.hasNext()) {
				if (!quoteIter.hasNext()) {
					quoteIter = quotes.iterator();
				}
				String imagePath = imageIter.next();
				String quote = quoteIter.next();
				PinData pin = new PinData();
				pin.setDescription(quote);
				pin.setImage(imagePath);
				pin.setSource(source);
				String tagDir = BotPaths.PINS_ROOT + this.tag;
				File pathDirFile = new File(tagDir);
				pathDirFile.mkdirs();
				File jsonFile = new File(pathDirFile, r.nextInt() + ".json");
				mapper.writeValue(jsonFile, pin);
			}
			Log.log("Done generating pins for  " + tag);

		} catch (Exception e) {
			Log.log("Failed to generate pins " + e.getMessage());
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.GENERATE;
	}

}
