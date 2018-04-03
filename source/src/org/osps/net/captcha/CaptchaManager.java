package org.osps.net.captcha;

import java.util.HashMap;
import java.util.Map;

public class CaptchaManager {

	private static CaptchaManager instance = new CaptchaManager();
	private static final int CAPTCHA_BUMP = 3; // How many attempts for new captcha
	
	public static CaptchaManager getSingleton() {
		return instance;
	}
	
	public boolean requiresCaptcha(String hash) {
		if(!captchaMap.containsKey(hash))
			return true;
		int count = captchaMap.get(hash);
		if(count == 1)
			captchaMap.remove(hash);
		else 
			captchaMap.put(hash, count - 1);	
		return false;
	}
	
	public void updateEntry(String hash) {
		captchaMap.put(hash, CAPTCHA_BUMP);
	}
	
	Map<String, Integer> captchaMap = new HashMap<String, Integer>();
	
}
