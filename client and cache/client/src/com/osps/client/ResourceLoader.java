package com.osps.client;


import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;


public class ResourceLoader {

	private static final HashMap<String, Image> loadedImages = new HashMap<String, Image>();

	public Image getImage(String imageName) {
		if(loadedImages.containsKey(imageName))
			return loadedImages.get(imageName);
		Image img = null;
		try {
			img = Toolkit.getDefaultToolkit().getImage(Signlink.findcachedir() + ""+imageName+".png");
		} catch(Exception e) {
			e.printStackTrace();
			img = null;
		}
		if(img != null)
			loadedImages.put(imageName, img);
		return img;
	}
	
	static ResourceLoader rl = new ResourceLoader();
	
	public static Image loadImage(String imageName) {
		URL url = null;
		try {
			url = rl.getClass().getResource("images/" + imageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (url == null) {
			return null;
		}
		return Toolkit.getDefaultToolkit().getImage(url);
	}
}
