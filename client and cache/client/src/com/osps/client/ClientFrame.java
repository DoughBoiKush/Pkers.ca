package com.osps.client;




import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;

/**
 * Creates a new user interface to display the client
 * 
 * @author Arithium
 *
 */
public class ClientFrame extends Client implements ActionListener {

	/**
	 * The default serialized version
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create our game panel to display the client
	 */
	private JPanel gamePanel;

	/**
	 * Our jpanel for the menu bar
	 */
	private JPanel menuPanel;

	/**
	 * Creates a new jframe to display the client
	 * 
	 * @param args
	 */
	public ClientFrame(String args[]) {
		try {
			initializeUserInterface();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Initializes the jframe
	 */
	public void initializeUserInterface() {
		try {

			/*
			 * Initialize our look and feel
			 */
			// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			// JFrame.setDefaultLookAndFeelDecorated(true);
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);

			UIManager.put("InternalFrame.activeTitleBackground", new ColorUIResource(Color.black));
			UIManager.put("InternalFrame.activeTitleForeground", new ColorUIResource(Color.WHITE));
			UIManager.put("InternalFrame.titleFont", new Font("Dialog", Font.PLAIN, 11));

			/*
			 * Initialize the jframe which will hold everything
			 */
			frame = new JFrame(Constants.CLIENT_NAME);
			frame.setLayout(new BorderLayout());
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
			frame.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

			Set<KeyStroke> forwardKeys = new HashSet<KeyStroke>(1);
			forwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_MASK));
			setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

			Set<KeyStroke> backwardKeys = new HashSet<KeyStroke>(1);
			backwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
			setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);

			/*
			 * Set our frame size
			 */
			int width = REGULAR_WIDTH;
			int height = REGULAR_HEIGHT;
			Insets insets = getInsets();
			frame.setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
			/*
			 * Fetch our screen size and set the location to the center of the
			 * screen
			 */
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			int x = (int) (toolkit.getScreenSize().getWidth() / 10 - (765 / 10));
			int y = (int) (toolkit.getScreenSize().getHeight() / 10 - (503 / 10));
			frame.setLocation(new Point(x, y));
			//frame.setLocationRelativeTo(null);
			/*
			 * Add our window listener to check when the x is clicked
			 */
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent we) {
					String options[] = { "Yes", "No" };
					int userPrompt = JOptionPane.showOptionDialog(null, "Are you sure you wish to exit?",
							Constants.CLIENT_NAME, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
							options[1]);
					if (userPrompt == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				}

				@Override
				public void windowGainedFocus(WindowEvent e) {
					getGamePanel().requestFocusInWindow();
					getGamePanel().requestFocus();
				}

			});

			/*
			 * Fetch our icon and set it
			 */
			Image icon = ResourceLoader.loadImage("logo.png");

			if (icon != null) {
				frame.setIconImage(icon);
			}

			/*
			 * Initialize our menu bar to be displayed on top
			 */
			initializeMenuBar();

			/*
			 * We initialize our game panel
			 */
			initializeGamePanel();

			/*
			 * Set the frame as non focusable
			 */
			frame.setFocusable(false);

			/*
			 * Pack the frame to remove any empty space and fit the applet
			 */
			frame.pack();

			/*
			 * Finally set our frame to visible
			 */
			frame.setVisible(true); // can see the client

			/*
			 * Create our applet
			 */
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the game panel to display the applet
	 */
	private void initializeGamePanel() {

		/*
		 * Initialize
		 */
		setGamePanel(new JPanel());

		/*
		 * Set the layout to border layout
		 */
		getGamePanel().setLayout(new BorderLayout());

		/*
		 * Add the client to the game panel
		 */
		getGamePanel().add(this);

		/*
		 * Set the size of the game panel
		 */
		Dimension dimension = new Dimension(REGULAR_WIDTH, REGULAR_HEIGHT);
		getGamePanel().setPreferredSize(dimension);
		getGamePanel().setSize(dimension);

		/*
		 * Set the game panel as focusable
		 */
		getGamePanel().setFocusable(false);

		/*
		 * Request the focus to the game panel
		 */
		getGamePanel().requestFocus();

		/*
		 * Disable focus traversal keys
		 */
		getGamePanel().setFocusTraversalKeysEnabled(false);

		getGamePanel().setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		getGamePanel().setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

		/*
		 * Place the game panel which will display the applet in the center
		 */
		frame.getContentPane().add(getGamePanel(), BorderLayout.CENTER);
	}

	/**
	 * Initializes the menu bar
	 */
	public void initializeMenuBar() {

		/*
		 * Initialize our menu panel to hold our menu buttons
		 */
		menuPanel = new JPanel();

		/*
		 * Set the menu panel as non focusable
		 */
		menuPanel.setFocusable(false);
		
		/*
		 * Disable focus traversal keys
		 */
		menuPanel.setFocusTraversalKeysEnabled(false);

		menuPanel.setBackground(Color.decode("0x6dc066"));

		menuPanel.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		menuPanel.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

		/*
		 * Create our buttons
		 */
		JButton homeButton = createButton("Home", "home_icon.png", "Open the Pkers.Ca homepage.");
		JButton forumButton = createButton("Forum", "forum_icon.png", "Open the Pkers.Ca forums.");
		JButton storeButton = createButton("Store", "store_icon.png", "Open the official Pkers.Ca store.");
		JButton voteButton = createButton("Vote", "vote_icon.png", "Open the official Pkers.Ca page.");
		JButton hiscoresButton = createButton("Hiscores", "hiscore_icon.png", "Open the Pkers.Ca Hiscores.");

		/*
		 * Add our buttons to the menu panel
		 */

		menuPanel.add(homeButton);
		menuPanel.add(forumButton);
		menuPanel.add(storeButton);
		menuPanel.add(voteButton);
		menuPanel.add(hiscoresButton);

		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(1, 18));
		separator.setBackground(Color.BLACK);
		separator.setBorder(BorderFactory.createEmptyBorder());
		menuPanel.add(separator);

		JButton facebook = createButton("", "facebook_icon.png", "Open the Pkers.Ca facebook page.");
		JButton youtube = createButton("", "youtube_icon.png", "Open the Pkers.Ca youtube page.");

		menuPanel.add(facebook);
		menuPanel.add(youtube);

		JSeparator separator2 = new JSeparator(SwingConstants.VERTICAL);
		separator2.setPreferredSize(new Dimension(1, 18));
		separator2.setBackground(Color.BLACK);
		separator2.setBorder(BorderFactory.createEmptyBorder());
		menuPanel.add(separator2);

		JButton screenshot = createButton("Screenshot", "monitor_icon.png", "Open the Pkers.Ca facebook page.");
		JButton history = createButton("History", "history_icon.png", "Open the Pkers.Ca youtube page.");

		menuPanel.add(screenshot);
		menuPanel.add(history);

		/*
		 * Add our menu panel to our frame
		 */
		frame.getContentPane().add(menuPanel, BorderLayout.NORTH);
	}

	/**
	 * Creates a button on the menu panel
	 * 
	 * @param title
	 *            The Title of the button
	 * @param image
	 *            The image to display
	 * @param tooltip
	 *            The tooltip when hovering over the button
	 * @return The created button
	 */
	private JButton createButton(String title, String image, String tooltip) {
		final JButton button = new JButton(title);
		if (image != null) {
			Image img = ResourceLoader.loadImage(image);
			if (img != null) {
				ImageIcon icon = new ImageIcon(img);
				// Image resized = icon.getImage().getScaledInstance(30, 30,
				// Image.SCALE_SMOOTH);
				// icon = new ImageIcon(resized);
				if (icon != null)
					button.setIcon(icon);
			}
		}
		button.addActionListener(this);
		if (tooltip != null)
			button.setToolTipText(tooltip);
		button.setFocusable(false);
		button.setContentAreaFilled(false);
		button.setFont(new Font("Dialog", Font.PLAIN, 11));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setForeground(Color.BLACK);
		button.setBorder(new EmptyBorder(1, 1, 3, 1));
		button.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	button.setContentAreaFilled(true);
		    	button.setBackground(Color.decode("0xb6fcd5"));
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	button.setBackground(UIManager.getColor("control"));
		    	button.setContentAreaFilled(false);
		    }
		});
		return button;
	}

	/**
	 * Use this method to create a screenshot of the JFrame object argFrame.
	 *
	 * Author(s): Dejan Lekic License: Public Domain
	 *
	 * @param argFrame
	 *            JFrame you want to make screenshot of.
	 */
	public final void makeScreenshot() {
		try {
			Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
			Point point = window.getLocationOnScreen();
			Robot robot = new Robot(window.getGraphicsConfiguration().getDevice());
			Rectangle rectangle = new Rectangle((int) point.getX(), (int) point.getY(), window.getWidth(),
					window.getHeight());
			BufferedImage img = robot.createScreenCapture(rectangle);
			Path path = Paths.get(Signlink.findcachedir(), "screenshots");
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			DateFormat format = new SimpleDateFormat("MM-dd-yyyy hh-mm-ss a");
			File file = new File(path.toFile(), format.format(new Date()) + ".png");
			ImageIO.write(img, "png", file);

		} catch (IOException | AWTException ioe) {
			System.out.println(ioe.toString());
		}
	}

	/**
	 * Opens a URL in your default web browser
	 * 
	 * @param url
	 *            The url of the website to open
	 */
	static void openURL(String url) {
		Desktop d = Desktop.getDesktop();
		try {
			d.browse(new URI(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Resizes the frame when switching between modes
	 * 
	 * @param screenMode
	 *            The {@link ScreenMode} your switching too
	 */
	public void resize(int size) {
		try {
			int width = 765;
			int height = 503;
			if (size == 0) {
				width = 765;
				height = 503;
			} else if (size == 1) {
				width = RESIZABLE_WIDTH;
				height = RESIZABLE_HEIGHT;
			} else if (size == 2) {
				width = getMaxWidth();
				height = getMaxHeight();
			}
			

			Dimension dimension = new Dimension(width, height);

			gamePanel.setPreferredSize(dimension);

			frame.setMinimumSize(dimension);

			Insets insets = frame.getInsets();
			int widthModifier = 0 + insets.left + insets.right;
			int heightModifier = menuPanel.getHeight(); 
			frame.setBounds(0, 0, width + widthModifier, height + heightModifier);

			super.myWidth = width;
			super.myHeight = height;

			frame.setResizable(Configuration.clientSize == 0);
			frame.setLocationRelativeTo(null);
			frame.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The action listener for the menu panel buttons
	 */
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		try {
			if (cmd != null) {
				switch (cmd) {
				case "Home":
					openURL("https://osps.com");
					break;
				case "Forum":
					openURL("http://forum.osps.com/");
					break;
				case "Store":
					openURL("https://osps.com/forums/index.php?/store/index.html");
					break;
				case "Hiscores":
					openURL("https://osps.com/forums/index.php?/highscores/index/");
					break;
				case "Vote":
					openURL("https://osps.com/forums/index.php?/vote/index");
					break;
				case "Screenshot":
					makeScreenshot();
					break;
				case "History":
					Desktop.getDesktop().open(new File(Signlink.findcachedir(), "screenshots"));
					break;
				}

			}
		} catch (Exception e) {
		}
	}

	private static void screenshot() {
		Graphics2D imageGraphics = null;
		try {
			Robot robot = new Robot();
			GraphicsDevice currentDevice = MouseInfo.getPointerInfo().getDevice();
			BufferedImage exportImage = robot.createScreenCapture(currentDevice.getDefaultConfiguration().getBounds());

			imageGraphics = (Graphics2D) exportImage.getGraphics();
			File directory = new File(Signlink.findcachedir(), "screenshots");
			if (!directory.exists()) {
				directory.mkdir();
			}
			// Create temp file.
			File screenshotFile = new File(directory, "screenshot.png");
			ImageIO.write(exportImage, "png", screenshotFile);
			System.out.println("Screenshot successfully captured to '" + screenshotFile.getCanonicalPath() + "'!");
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			imageGraphics.dispose();
		}
	}

	public void setCursor(byte[] data) {
		try {
			Image image = getGameComponent().getToolkit().createImage(data);
			getGameComponent()
					.setCursor(getGameComponent().getToolkit().createCustomCursor(image, new Point(0, 0), null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JPanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(JPanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public JPanel getMenuPanel() {
		return menuPanel;
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public JFrame getFrame() {
		return frame;
	}
}