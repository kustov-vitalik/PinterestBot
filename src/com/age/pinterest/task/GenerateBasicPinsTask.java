package com.age.pinterest.task;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.codehaus.jackson.map.ObjectMapper;

import com.age.data.Pin;
import com.age.help.BotPaths;
import com.age.help.DescriptionGenerator;
import com.age.help.FileUtill;

public class GenerateBasicPinsTask extends Task {
	private final String tag;
	private final String source;

	public GenerateBasicPinsTask(String tag, String source) {
		super("GeneratePinsLog");
		this.tag = tag;
		this.source = source;
	}

	@Override
	public void run() {
		try {
			logger.log("Generating pins for " + tag);
			List<String> imagePaths = FileUtill.getAllFiles(BotPaths.IMAGES_ROOT + tag);
			DescriptionGenerator generator = new DescriptionGenerator();
			List<String> quotes = generator.getQuotes(tag);
			logger.log("Will generate " + imagePaths.size() + " pins");
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
				Pin pin = new Pin();
				pin.setDescription(quote);
				pin.setImage(imagePath);
				pin.setSource(source);
				String tagDir = BotPaths.PINS_ROOT + this.tag;
				File pathDirFile = new File(tagDir);
				pathDirFile.mkdirs();
				File jsonFile = new File(pathDirFile, r.nextInt() + ".json");
				mapper.writeValue(jsonFile, pin);
			}
			logger.log("Done generating pins for  " + tag);

		} catch (Exception e) {
			logger.log("Failed to generate pins " + e.getMessage());
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.GENERATE;
	}

}
