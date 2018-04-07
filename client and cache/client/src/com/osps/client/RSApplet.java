package com.osps.client;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.osps.client.cache.graphics.RSInterface;
import com.osps.client.draw.RSImageProducer;

public class RSApplet extends Applet implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, FocusListener, WindowListener {

	private static final long serialVersionUID = 1L;
	
    private int anInt4;
    private int delayTime;
    int minDelay;
    private final long aLongArray7[] = new long[10];
    int fps;
    boolean shouldDebug;
    int myWidth;
    int myHeight;
    Graphics graphics;
    RSImageProducer fullGameScreen;
    RSFrame gameFrame;
    private boolean shouldClearScreen;
    boolean awtFocus;
    int idleTime;
    int clickMode2;
    public int mouseX;
    public int mouseY;
    int clickMode1;
    private int clickX;
    private int clickY;
    private long clickTime;
    public long aLong29;
    public int clickMode3;
    public int saveClickX;
    public int saveClickY;
    final int keyArray[] = new int[128];
    private final int charQueue[] = new int[128];
    public boolean isLoading;
    private int readIndex;
    private int writeIndex;
    public static int anInt34;
    public boolean isApplet;
    protected int rotationGliding;
    public boolean resized;
    
	public void rebuildFrame(boolean undecorated, int width, int height, boolean resizable, boolean full) {
		Component component = getGameComponent();
		component.setBackground(Color.black);
		component.removeMouseWheelListener(this);
		component.removeMouseListener(this);
		component.removeMouseMotionListener(this);
		component.removeKeyListener(this);
		component.removeFocusListener(this);
		if (gameFrame != null) {
			gameFrame.removeWindowListener(this);
			gameFrame.setVisible(false);
			gameFrame.dispose();
			gameFrame = null;
		}
		component = getGameComponent();
		component.setBackground(Color.black);
		component.addMouseWheelListener(this);
		component.addMouseListener(this);
		component.addMouseMotionListener(this);
		component.addKeyListener(this);
		component.addFocusListener(this);
		mouseX = mouseY = -1;
	}
    
	final void createClientFrame(int w, int h) {
		isApplet = false;
		myWidth = w;
		myHeight = h;
		gameFrame = new RSFrame(this, myWidth, myHeight, Configuration.clientSize == 2, Configuration.clientSize == 1);
		setFocusTraversalKeysEnabled(false);
		graphics = getGameComponent().getGraphics();
		fullGameScreen = new RSImageProducer(myWidth, myHeight, getGameComponent());
		startRunnable(this, 1);
	}

	final void initClientFrame(int w, int h) {
		isApplet = true;
		myWidth = w;
		setFocusTraversalKeysEnabled(false);
		myHeight = h;
		graphics = getGameComponent().getGraphics();
		fullGameScreen = new RSImageProducer(myWidth, myHeight, getGameComponent());
		startRunnable(this, 1);
	}

    @Override
	public void run()
    {
    	setFocusTraversalKeysEnabled(false);
        getGameComponent().addMouseListener(this);
        getGameComponent().addMouseMotionListener(this);
        getGameComponent().addKeyListener(this);
        getGameComponent().addFocusListener(this);
        getGameComponent().addMouseWheelListener(this);
        if(gameFrame != null)
        {
            gameFrame.addWindowListener(this);
        }
        drawLoadingText(3, "Checking for updates");
        startUp();
        int i = 0;
        int j = 256;
        int k = 1;
        int l = 0;
        int i1 = 0;
        for(int j1 = 0; j1 < 10; j1++)
        {
            aLongArray7[j1] = System.currentTimeMillis();
        }
        do
        {
            if(anInt4 < 0)
            {
                break;
            }
            if(anInt4 > 0)
            {
                anInt4--;
                if(anInt4 == 0)
                {
                    exit();
                    return;
                }
            }
            int k1 = j;
            int i2 = k;
            j = 300;
            k = 1;
            long l2 = System.currentTimeMillis();
            if(aLongArray7[i] == 0L)
            {
                j = k1;
                k = i2;
            } else
            if(l2 > aLongArray7[i])
            {
                j = (int)(2560 * delayTime / (l2 - aLongArray7[i]));
            }
            if(j < 25)
            {
                j = 25;
            }
            if(j > 256)
            {
                j = 256;
                k = (int)(delayTime - (l2 - aLongArray7[i]) / 10L);
            }
            if(k > delayTime)
            {
                k = delayTime;
            }
            aLongArray7[i] = l2;
            i = (i + 1) % 10;
            if(k > 1)
            {
                for(int j2 = 0; j2 < 10; j2++)
                {
                    if(aLongArray7[j2] != 0L)
                    {
                        aLongArray7[j2] += k;
                    }
                }

            }
            if(k < minDelay)
            {
                k = minDelay;
            }
            try
            {
                Thread.sleep(k);
            }
            catch(InterruptedException interruptedexception)
            {
                i1++;
            }
            for(; l < 256; l += j)
            {
                clickMode3 = clickMode1;
                saveClickX = clickX;
                saveClickY = clickY;
                clickMode1 = 0;
                processGameLoop();
                readIndex = writeIndex;
            }

            l &= 0xff;
            if(delayTime > 0)
            {
                fps = (1000 * j) / (delayTime * 256);
            }
            processDrawing();
            if(shouldDebug)
            {
                System.out.println((new StringBuilder()).append("ntime:").append(l2).toString());
                for(int k2 = 0; k2 < 10; k2++)
                {
                    int i3 = ((i - k2 - 1) + 20) % 10;
                    System.out.println((new StringBuilder()).append("otim").append(i3).append(":").append(aLongArray7[i3]).toString());
                }

                System.out.println((new StringBuilder()).append("fps:").append(fps).append(" ratio:").append(j).append(" count:").append(l).toString());
                System.out.println((new StringBuilder()).append("del:").append(k).append(" deltime:").append(delayTime).append(" mindel:").append(minDelay).toString());
                System.out.println((new StringBuilder()).append("intex:").append(i1).append(" opos:").append(i).toString());
                shouldDebug = false;
                i1 = 0;
            }
        } while(true);
        if(anInt4 == -1)
        {
            exit();
        }
    }
    
    void drawLoadingText(int percentage, String loadingText) {
		Client.getClient().checkSize();
		while (graphics == null) {
			graphics = (isApplet ? this : gameFrame).getGraphics();
			try {
				getGameComponent().repaint();
			} catch (Exception _ex) {
			}
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		}
		Font font = new Font("Helvetica", 1, 13);
		FontMetrics fontmetrics = getGameComponent().getFontMetrics(font);
		Font font1 = new Font("Helvetica", 0, 13);
		FontMetrics fontmetrics1 = getGameComponent().getFontMetrics(font1);
		if (shouldClearScreen) {
			graphics.setColor(Color.black);
			graphics.fillRect(0, 0, Configuration.clientWidth, Configuration.clientHeight);
			shouldClearScreen = false;
		}
		Color color = new Color(140, 17, 17);
		int y = Configuration.clientHeight / 2 - 18;
		graphics.setColor(color);
		graphics.drawRect(Configuration.clientWidth / 2 - 152, y, 304, 34);
		graphics.fillRect(Configuration.clientWidth / 2 - 150, y + 2, percentage * 3,
				30);
		graphics.setColor(Color.black);
		graphics.fillRect((Configuration.clientWidth / 2 - 150) + percentage * 3,
				y + 2, 300 - percentage * 3, 30);
		graphics.setFont(font);
		graphics.setColor(Color.white);
		graphics.drawString(
				loadingText,
				(Configuration.clientWidth - fontmetrics.stringWidth(loadingText)) / 2,
				y + 22);
		graphics.drawString("",
				(Configuration.clientWidth - fontmetrics1.stringWidth("")) / 2, y - 8);
	}

    private void exit()
    {
        anInt4 = -2;
        cleanUpForQuit();
        if(gameFrame != null)
        {
            try
            {
                Thread.sleep(1000L);
            }
            catch(Exception exception) { }
            try
            {
                System.exit(0);
            }
            catch(Throwable throwable) { }
        }
    }

    final void method4(int i)
    {
        delayTime = 1000 / i;
    }

    @Override
	public final void start()
    {
        if(anInt4 >= 0)
        {
            anInt4 = 0;
        }
    }

    @Override
	public final void stop()
    {
        if(anInt4 >= 0)
        {
            anInt4 = 4000 / delayTime;
        }
    }

    @Override
	public final void destroy()
    {
        anInt4 = -1;
        try
        {
            Thread.sleep(5000L);
        }
        catch(Exception exception) { }
        if(anInt4 == -1)
        {
            exit();
        }
    }

    @Override
	public final void update(Graphics g)
    {
        if(graphics == null)
        {
            graphics = g;
        }
        shouldClearScreen = true;
        raiseWelcomeScreen();
    }

    @Override
	public final void paint(Graphics g)
    {
        if(graphics == null)
        {
            graphics = g;
        }
        shouldClearScreen = true;
        raiseWelcomeScreen();
    }
    
	public boolean canZoom = true;

    @Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		int rotation = event.getWheelRotation();
		handleInterfaceScrolling(event);
		if(mouseX > 0 && mouseX < 512 && mouseY > Configuration.clientHeight - 165 && mouseY < Configuration.clientHeight - 25) {
			int scrollPos = Client.anInt1089;
			scrollPos -= rotation * 30;		
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > Client.anInt1211 - 110) {
				scrollPos = Client.anInt1211 - 110;
			}
			
			if(Client.anInt1089 != scrollPos) {
				Client.anInt1089 = scrollPos;
				Client.inputTaken = true;
			}
		}
		if (Configuration.clientSize != 0 && Client.openInterfaceID == -1) {
			if (canZoom) {
				if (rotation == -1) {
					if(mouseX > 0 && mouseX < 512 && mouseY > Configuration.clientHeight - 165 && mouseY < Configuration.clientHeight - 25) {
						return;
					}
					if (Client.cameraZoom > 200) {
						Client.cameraZoom -= 50;
					}
				} else {
					if (Client.cameraZoom < 900) {
						Client.cameraZoom += 50;
					}
				}
			}
		}
	}
	
	
    public void handleInterfaceScrolling(MouseWheelEvent event) {
		int rotation = event.getWheelRotation();
		int positionX = 0;
		int positionY = 0;
		int width = 0;
		int height = 0;
		int offsetX = 0;
		int offsetY = 0;
		int childID = 0;
		/* Tab interface scrolling */
		int tabInterfaceID = Client.tabInterfaceIDs[Client.tabID];
		if (tabInterfaceID != -1) {
			RSInterface tab = RSInterface.interfaceCache[tabInterfaceID];
			offsetX = Configuration.clientSize == 0 ? Configuration.clientWidth - 218 : (Configuration.clientSize == 0 ? 28 : Configuration.clientWidth - 197);
			offsetY = Configuration.clientSize == 0 ? Configuration.clientHeight - 298 : (Configuration.clientSize == 0 ? 37 : Configuration.clientHeight - (Configuration.clientWidth >= 900 ? 37 : 74) - 267);
			if (tab.children == null) {
				return;
			}
			for (int index = 0; index < tab.children.length; index++) {
				if (RSInterface.interfaceCache[tab.children[index]].scrollMax > 0) {
					childID = index;
					positionX = tab.childX[index];
					positionY = tab.childY[index];
					width = RSInterface.interfaceCache[tab.children[index]].width;
					height = RSInterface.interfaceCache[tab.children[index]].height;
					break;
				}
			}
			if (mouseX > offsetX + positionX && mouseY > offsetY + positionY && mouseX < offsetX + positionX + width && mouseY < offsetY + positionY + height) {
				canZoom = false;
			} else {
				canZoom = true;
			}
			if (mouseX > offsetX + positionX && mouseY > offsetY + positionY && mouseX < offsetX + positionX + width && mouseY < offsetY + positionY + height) {
				if (RSInterface.interfaceCache[tab.children[childID]].scrollPosition > 0) {
					RSInterface.interfaceCache[tab.children[childID]].scrollPosition += rotation * 30;
					return;
				} else {
					if (rotation > 0) {
						RSInterface.interfaceCache[tab.children[childID]].scrollPosition += rotation * 30;
						return;
					}
				}
			}
		}
		/* Main interface scrolling */
		if (Client.openInterfaceID != -1) {
			RSInterface rsi = RSInterface.interfaceCache[Client.openInterfaceID];
			offsetX = Configuration.clientSize == 0 ? 4 : (Configuration.clientWidth / 2) - 256;
			offsetY = Configuration.clientSize == 0 ? 4 : (Configuration.clientHeight / 2) - 167;
			for (int index = 0; index < rsi.children.length; index++) {
				if (RSInterface.interfaceCache[rsi.children[index]].scrollMax > 0) {
					childID = index;
					positionX = rsi.childX[index];
					positionY = rsi.childY[index];
					width = RSInterface.interfaceCache[rsi.children[index]].width;
					height = RSInterface.interfaceCache[rsi.children[index]].height;
					break;
				}
			}
			if (mouseX > offsetX + positionX && mouseY > offsetY + positionY && mouseX < offsetX + positionX + width && mouseY < offsetY + positionY + height) {
				canZoom = false;
			} else {
				canZoom = true;
			}
			if (mouseX > offsetX + positionX && mouseY > offsetY + positionY && mouseX < offsetX + positionX + width && mouseY < offsetY + positionY + height) {
				if (RSInterface.interfaceCache[rsi.children[childID]].scrollPosition > 0) {
					RSInterface.interfaceCache[rsi.children[childID]].scrollPosition += rotation * 30;
					return;
				} else {
					if (rotation > 0) {
						RSInterface.interfaceCache[rsi.children[childID]].scrollPosition += rotation * 30;
						return;
					}
				}
			}
		}
	}

	public int clickType;
	public final int LEFT = 0;
	public final int RIGHT = 1;
	public final int DRAG = 2;
	public final int RELEASED = 3;
	public final int MOVE = 4;
	public int releasedX;
	public int releasedY;
	public boolean mouseWheelDown;
	
	@Override
	public final void mousePressed(MouseEvent mouseevent) {
		int i = mouseevent.getX();
		int j = mouseevent.getY();
		if (gameFrame != null) {
			Insets insets = gameFrame.getInsets();
			i -= insets.left;// 4
			j -= insets.top;// 22
		}
		idleTime = 0;
		clickX = i;
		clickY = j;
		clickTime = System.currentTimeMillis();
		if(mouseevent.getButton() == 2) {
			mouseWheelDown = true;
			mouseWheelX = i;
			mouseWheelY = j;
			return;
		}
		if (mouseevent.isMetaDown()) {
			clickType = RIGHT;
			clickMode1 = 2;
			clickMode2 = 2;
		} else {
			clickType = LEFT;
			clickMode1 = 1;
			clickMode2 = 1;
		}
	}


	@Override
	public final void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(gameFrame != null) {
			Insets insets = gameFrame.getInsets();
			x -= insets.left;//4
			y -= insets.top;//22
		}
		releasedX = x;
		releasedY = y;
		idleTime = 0;
		clickMode2 = 0;
		clickType = RELEASED;
		mouseWheelDown = false;
	}

    @Override
	public final void mouseClicked(MouseEvent mouseevent)
    {
    }

    @Override
	public final void mouseEntered(MouseEvent mouseevent)
    {
    }

	@Override
	public final void mouseExited(MouseEvent mouseevent)
	{
		idleTime = 0;
		mouseX = -1;
		mouseY = -1;
	}

	public int mouseWheelX;
	public int mouseWheelY;
	@Override
	public final void mouseDragged(MouseEvent mouseevent) {
		 if (Client.instance.variousSettings[314] == 0) {
			int i = mouseevent.getX();
			int j = mouseevent.getY();
			if (gameFrame != null) {
				Insets insets = gameFrame.getInsets();
				i -= insets.left;
				j -= insets.top;
			}
			if (System.currentTimeMillis() - clickTime >= 250L
					|| Math.abs(saveClickX - i) > 5 || Math.abs(saveClickY - j) > 5) {
				idleTime = 3;
				mouseX = i;
				mouseY = j;
			}
			if (mouseWheelDown) {
	            j = mouseWheelX - mouseevent.getX();
	            int k = mouseWheelY - mouseevent.getY();
	            Client.instance.mouseWheelDragged(j, -k);
	            mouseWheelX = mouseevent.getX();
	            mouseWheelY = mouseevent.getY();
	            return;
			}
			clickType = DRAG; 
			}
		}
	
	void mouseWheelDragged(int param1, int param2) {

	}

	@Override
	public final void mouseMoved(MouseEvent mouseevent) {
		int x = mouseevent.getX();
		int y = mouseevent.getY();
		if(gameFrame != null) {
			Insets insets = gameFrame.getInsets();
			x -= insets.left;//4
			y -= insets.top;//22
		}
		idleTime = 0;
		mouseX = x;
		mouseY = y;
		clickType = MOVE;
	}

	@Override
	public final void keyPressed(KeyEvent keyevent) {
		idleTime = 0;
		int i = keyevent.getKeyCode();
		int j = keyevent.getKeyChar();
		if (i == KeyEvent.VK_F1) {
			Client.setTab(3);
		} else if (i == KeyEvent.VK_ESCAPE) {
			Client.setTab(10);
		} else if (i == KeyEvent.VK_F2) {
			Client.setTab(4);
		} else if (i == KeyEvent.VK_F3) {
			Client.setTab(5);
		} else if (i == KeyEvent.VK_F4) {
			Client.setTab(6);
		} else if (i == KeyEvent.VK_F5) {
			Client.setTab(0);
		}
		if(j < 30)
			j = 0;
		if(i == 37)
			j = 1;
		if(i == 39)
			j = 2;
		if(i == 38)
			j = 3;
		if(i == 40)
			j = 4;
		if(i == 17)
			j = 5;
		if(i == 8)
			j = 8;
		if(i == 127)
			j = 8;
		if(i == 9)
			j = 9;
		if(i == 10)
			j = 10;
		if(i >= 112 && i <= 123)
			j = (1008 + i) - 112;
		if(i == 36)
			j = 1000;
		if(i == 35)
			j = 1001;
		if(i == 33)
			j = 1002;
		if(i == 34)
			j = 1003;
		if(j > 0 && j < 128)
			keyArray[j] = 1;
		if(j > 4)
		{
			charQueue[writeIndex] = j;
			writeIndex = writeIndex + 1 & 0x7f;
		}
	}

	@Override
	public final void keyReleased(KeyEvent keyevent)
	{
		idleTime = 0;
		int i = keyevent.getKeyCode();
		char c = keyevent.getKeyChar();
		if(c < '\036')
			c = '\0';
		if(i == 37)
			c = '\001';
		if(i == 39)
			c = '\002';
		if(i == 38)
			c = '\003';
		if(i == 40)
			c = '\004';
		if(i == 17)
			c = '\005';
		if(i == 8)
			c = '\b';
		if(i == 127)
			c = '\b';
		if(i == 9)
			c = '\t';
		if(i == 10)
			c = '\n';
		if(c > 0 && c < '\200')
			keyArray[c] = 0;
	}

    @Override
	public final void keyTyped(KeyEvent keyevent)
    {
    }

	final int readChar(int dummy)
	{
		while(dummy >= 0)
		{
			for(int j = 1; j > 0; j++);
		}
		int k = -1;
		if(writeIndex != readIndex)
		{
			k = charQueue[readIndex];
			readIndex = readIndex + 1 & 0x7f;
		}
		return k;
	}

    @Override
	public final void focusGained(FocusEvent focusevent)
    {
        awtFocus = true;
        shouldClearScreen = true;
        raiseWelcomeScreen();
    }

    @Override
	public final void focusLost(FocusEvent focusevent)
    {
        awtFocus = false;
        for(int i = 0; i < 128; i++)
        {
            keyArray[i] = 0;
        }

    }

    @Override
	public final void windowActivated(WindowEvent windowevent)
    {
    }

    @Override
	public final void windowClosed(WindowEvent windowevent)
    {
    }

    @Override
	public final void windowClosing(WindowEvent windowevent)
    {
        destroy();

    }

    @Override
	public final void windowDeactivated(WindowEvent windowevent)
    {
    }

    @Override
	public final void windowDeiconified(WindowEvent windowevent)
    {
    }

    @Override
	public final void windowIconified(WindowEvent windowevent)
    {
    }

    @Override
	public final void windowOpened(WindowEvent windowevent)
    {
    }

    void startUp()
    {
    }

    void processGameLoop()
    {
    }

    void cleanUpForQuit()
    {
    }

    void processDrawing()
    {
    }

    void raiseWelcomeScreen()
    {
    }

	Component getGameComponent() {
		if(gameFrame != null && !isApplet) {
			return gameFrame;
		} else {
			return this;
		}
	}

    public void startRunnable(Runnable runnable, int i)
    {
        Thread thread = new Thread(runnable);
        thread.start();
        thread.setPriority(i);
    }


    RSApplet() {
        delayTime = 20;
        minDelay = 1;
        shouldDebug = false;
        shouldClearScreen = true;
        awtFocus = true;
    }

}
