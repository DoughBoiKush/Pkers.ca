package com.osps.client;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.GregorianCalendar;

final class RSFrame extends Frame {
	
	public String location = Signlink.findcachedir() + "Sprites/";
	private static final long serialVersionUID = 1L;
	
	public RSFrame(RSApplet RSApplet_, int i, int j) {
		rsApplet = RSApplet_;
		//Image image = Toolkit.getDefaultToolkit().createImage(location+"Client/Background.png");
		//setIconImage(image);
		Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
		setIconImage(icon);
		setTitle("Pkers.Ca");
		setFocusTraversalKeysEnabled(false);
		setVisible(true);
		toFront();
		setSize(i + 8, j + 28);
		setResizable(true);
		setLocationRelativeTo(null);
	}

	public RSFrame(RSApplet rsapplet, int width, int height, boolean undecorative, boolean resizable) {
		rsApplet = rsapplet;
		Image icon = Toolkit.getDefaultToolkit().getImage(location+"Client/icon.png");
		setIconImage(icon);
		setTitle("Pkers.Ca");
		setFocusTraversalKeysEnabled(false);
		setUndecorated(undecorative);
		setResizable(resizable);
		setVisible(true);
		Insets insets = this.getInsets();
		//setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);//28
		//setLocation((screenWidth - width) / 2, ((screenHeight - height) / 2) - screenHeight == Client.getMaxHeight() ? 0 : undecorative ? 0 : 70);
		requestFocus();
		toFront();
		setBackground(Color.BLACK);
	}
	
	public static String checkDay() {
		Calendar cal = new GregorianCalendar();
		int DAY_OF_WEEK = cal.get(Calendar.DAY_OF_WEEK);
		if (DAY_OF_WEEK == Calendar.MONDAY)
		    return "Monday";
		if (DAY_OF_WEEK == Calendar.TUESDAY)
		    return "Tuesday";
		if (DAY_OF_WEEK == Calendar.WEDNESDAY)
		    return "Wednesday";
		if (DAY_OF_WEEK == Calendar.THURSDAY)
		    return "Thursday";
		if (DAY_OF_WEEK == Calendar.FRIDAY)
		    return "Friday";
		if (DAY_OF_WEEK == Calendar.SATURDAY)
		    return "Saturday";
		if (DAY_OF_WEEK == Calendar.SUNDAY)
		    return "Sunday";
		else
		    return "";
		}

	@Override
	public Graphics getGraphics() {
		Graphics g = super.getGraphics();
		Insets insets = this.getInsets();
		g.translate(insets.left ,insets.top);
		return g;
	}
	
	public int getFrameWidth() {
		Insets insets = this.getInsets();
		return getWidth() - (insets.left + insets.right);
	}
	
	public int getFrameHeight() {
		Insets insets = this.getInsets();
		return getHeight() - (insets.top + insets.bottom);
	}

	@Override
	public void update(Graphics g) {
		rsApplet.update(g);
	}

	@Override
	public void paint(Graphics g) {
		rsApplet.paint(g);
	}

	private final RSApplet rsApplet;
	public Toolkit toolkit = Toolkit.getDefaultToolkit();
	public Dimension screenSize = toolkit.getScreenSize();
	public int screenWidth = (int)screenSize.getWidth();
	public int screenHeight = (int)screenSize.getHeight();
}
