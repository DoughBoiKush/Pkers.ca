package org.osps.net.captcha;

import java.awt.Color;
import java.io.ByteArrayOutputStream;


import javax.imageio.ImageIO;

import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.noise.StraightLineNoiseProducer;

public class ImageCaptcha implements Captcha {
	
	nl.captcha.Captcha c;
	
	public ImageCaptcha() {
		c = new nl.captcha.Captcha.Builder(150, 40)
		        .addNoise(new CurvedLineNoiseProducer())
		        .addBackground(new GradiatedBackgroundProducer(Color.GRAY, Color.GREEN))
		        .addBorder()
				.addText()
		        .addNoise(new StraightLineNoiseProducer())
		        //.gimp(new StretchGimpyRenderer())
		        .build();

	}
	
	@Override
	public Object getKey(int identifier) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(c.getImage(), "jpg", baos );
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getAnswer() {
		return c.getAnswer();
	}

	@Override
	public int getExpectedLength() {
		return -1;
	}

}
