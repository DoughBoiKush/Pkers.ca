package com.osps.client;
import java.math.BigInteger;


public class Configuration {
	
	/**
	 * True = connect to local host False = connect to VPS
	 */
	public static boolean localHost = true;

	public static final String localAddress = "127.0.0.1";
	public static final String liveAddress = "172.81.132.122";


	/**
	 * Client Dimensions
	 */		
	public static int 
		clientSize = 0,
		clientWidth = 765,
		clientHeight = 503, 
		REGULAR_WIDTH = 765,
		REGULAR_HEIGHT = 503,
		RESIZABLE_WIDTH = 800,
		RESIZABLE_HEIGHT = 600;
	public static boolean normalLogin = true;	
	public static boolean showChatComponents = true;
	public static boolean showTabComponents = true;
	public static boolean transparentTabArea = false;
	
	
	public static boolean
		runClicked;
	
	/**
	 * Smooth texture shading
	 */
	
	public static boolean smoothShading = false;

	public static boolean hdTexturing = false;

	public static boolean distanceFog = false;
	
	public static boolean Roofs = false;
	
	public static boolean isOneClick = false;
	
	public static boolean timers = false;
	
	public static int cameraZoom = 600;
	
	/**
	 * Gets the store address.
	 */
	public static final String STORE_DOCUMENT_BASE = "";
	
	/**
	 * Toggles a security feature called RSA to prevent packet sniffers
	 */

	static final BigInteger RSA_MODULUS = new BigInteger("140581091427477215036784685160785408283128952373555829746916160706282580491956001658740331418566869520173985124598054723562429561059091521740982488538452973965004409614958869054717683346490127886261733448050695124463890596106254745773220451120493416350635197453836368326546015269766424823730332899603355203529");

	static final BigInteger RSA_EXPONENT = new BigInteger("65537");
        public static final boolean DECODE_RSA = true;
	
}
