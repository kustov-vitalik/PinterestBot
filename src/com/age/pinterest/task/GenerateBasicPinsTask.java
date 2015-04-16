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
import com.age.param.GeneratePinsParam;

public class GenerateBasicPinsTask extends Task {
	private final GeneratePinsParam generateParam;

	public GenerateBasicPinsTask(GeneratePinsParam generateParam) {
		super("GeneratePinsLog");
		this.generateParam = generateParam;
	}

	@Override
	public void run() {
		try {
			logger.log("Generating pins for " + generateParam.getTag());
			List<String> imagePaths = FileUtill.getAllFiles(BotPaths.IMAGES_ROOT + generateParam.getTag());
			DescriptionGenerator generator = new DescriptionGenerator();
			List<String> quotes = generator.getQuotes(generateParam.getTag());
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
				pin.setSource(generateParam.getSource());
				String tagDir = BotPaths.PINS_ROOT + generateParam.getTag();
				File pathDirFile = new File(tagDir);
				pathDirFile.mkdirs();
				File jsonFile = new File(pathDirFile, r.nextInt() + ".json");
				mapper.writeValue(jsonFile, pin);
			}
			logger.log("Done generating pins for  " + generateParam.getTag());

		} catch (Exception e) {
			logger.log("Failed to generate pins " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public TaskType getType() {
		return TaskType.GENERATE;
	}

}
