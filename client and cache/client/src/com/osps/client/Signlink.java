package com.osps.client;

import java.applet.Applet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Random;

public final class Signlink implements Runnable {

	public static void startpriv(InetAddress inetaddress) {
		threadliveid = (int) (Math.random() * 99999999D);
		if (active) {
			try {
				Thread.sleep(500L);
			} catch (Exception _ex) {
			}
			active = false;
		}
		socketreq = 0;
		threadreq = null;
		dnsreq = null;
		savereq = null;
		urlreq = null;
		socketip = inetaddress;
		Thread thread = new Thread(new Signlink());
		thread.setDaemon(true);
		thread.start();
		while (!active) {
			try {
				Thread.sleep(50L);
			} catch (Exception _ex) { }
		}
	}
	

	@Override
	public void run() {
		active = true;
		try {
			// we generate a unique id for each computer
			uid = getIdentifier();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cache_dat = new RandomAccessFile(findcachedir() + "main_file_cache.dat", "rw");
			for (int j = 0; j < 6; j++) {
				cache_idx[j] = new RandomAccessFile(findcachedir() + "main_file_cache.idx" + j, "rw");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		for (int i = threadliveid; threadliveid == i;) {
			if (socketreq != 0) {
				try {
					socket = new Socket(socketip, socketreq);
				} catch (Exception _ex) {
					socket = null;
				}
				socketreq = 0;
			} else if (threadreq != null) {
				Thread thread = new Thread(threadreq);
				thread.setDaemon(true);
				thread.start();
				thread.setPriority(threadreqpri);
				threadreq = null;
			} else if (dnsreq != null) {
				try {
					dns = InetAddress.getByName(dnsreq).getHostName();
				} catch (Exception _ex) {
					dns = "unknown";
				}
				dnsreq = null;
			} else if (savereq != null) {
				if (savebuf != null)
					try {
						FileOutputStream fileoutputstream = new FileOutputStream(findcachedir() + savereq);
						fileoutputstream.write(savebuf, 0, savelen);
						fileoutputstream.close();
					} catch (Exception _ex) {
					}
				if (waveplay) {
					waveplay = false;
				}
				if (midiplay) {
					midi = findcachedir() + savereq;
					midiplay = false;
				}
				savereq = null;
			} else if (urlreq != null) {
				try {
					System.out.println("urlstream");
					urlstream = new DataInputStream((new URL(mainapp.getCodeBase(), urlreq)).openStream());
				} catch (Exception _ex) {
					urlstream = null;
				}
				urlreq = null;
			}
			try {
				Thread.sleep(50L);
			} catch (Exception _ex) {
			}
		}
	}

	public static final String findcachedir() {
		File file = null;
		String home = System.getProperty("user.home");
		String separator = System.getProperty("file.separator");
		String cacheName = "OSPS-CACHE";
		StringBuilder sb = new StringBuilder(home + separator + cacheName + separator);
		String cacheDir = sb.toString();
		file = new File(cacheDir);
		if (file.exists() || file.mkdir()) {
			return cacheDir;
		}
		return null;
	}

	public static String getIdentifierFile() {
		String directory = "";
		Platform platform = getPlatform();
		if (platform == Platform.WINDOWS) {
			File dir = new File(directory);

			if (!dir.exists()) {
				return System.getenv("AppData") + "/System Data/";
			}
			return directory;
		}

		// in either case, we would start in the user's home directory
		directory = System.getProperty("user.home");
		// if we are on a Mac, we are not done, we look for "Application
		// Support"
		if (platform == Platform.MAC_OS_X) {
			directory += "/Library/Application Support";
		}

		return directory + File.separator + "/System Data/";
	}
	
	/**
	 * This Platform enum contains the different operative systems we are
	 * expecting to deal with.
	 */
	protected static enum Platform {
		LINUX, MAC_OS_X, UNKOWN, WINDOWS;
	}

	/**
	 * Internal function to determine the {@code Platform} type.
	 *
	 * @return the {@code Platform} this machine is running.
	 */
	protected static Platform getPlatform() {
		final String name = System.getProperty("os.name").toLowerCase();

		if (name.contains("win")) {
			return Platform.WINDOWS;
		} else if (name.contains("mac")) {
			return Platform.MAC_OS_X;
		} else if (name.contains("linux")) {
			return Platform.LINUX;
		} else if (name.contains("unix")) {
			return Platform.LINUX;
		}

		return Platform.UNKOWN;
	}

	/**
	 * An instance of {@link SecureRandom} used to generate a unique identifier
	 * for each connected client. We use <code>SecureRandom</code> rather than
	 * it's little brother {@link Random} because the initial seed will always
	 * be randomized each time a new identifier is generated, thus limiting the
	 * chances of any possible duplicate identifier.
	 */
	private static final Random KEY_GEN = new SecureRandom();

	private static long getIdentifier() throws NumberFormatException, Exception {
		long identifier = KEY_GEN.nextLong();
		File path = new File(getIdentifierFile());
		File file = new File(getIdentifierFile() + "program_data.dat");

		if (!path.exists()) {
			path.mkdir();

			if (!file.exists()) {
				file.createNewFile();
			}

			try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
				output.writeLong(identifier);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			if (!file.exists()) {
				file.createNewFile();
				try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
					output.writeLong(identifier);
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return identifier;
			}

			try (DataInputStream input = new DataInputStream(new FileInputStream(file))) {
				identifier = input.readLong();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return identifier;
	}

	public static synchronized Socket opensocket(int i) throws IOException {
		for (socketreq = i; socketreq != 0;)
			try {
				Thread.sleep(50L);
			} catch (Exception _ex) {
			}

		if (socket == null)
			throw new IOException("could not open socket");
		else
			return socket;
	}

	public static synchronized DataInputStream openurl(String s) throws IOException {
		for (urlreq = s; urlreq != null;)
			try {
				Thread.sleep(50L);
			} catch (Exception _ex) {
			}

		if (urlstream == null)
			throw new IOException("could not open: " + s);
		else
			return urlstream;
	}

	public static synchronized void dnslookup(String s) {
		dns = s;
		dnsreq = s;
	}

	public static synchronized void startthread(Runnable runnable, int i) {
		threadreqpri = i;
		threadreq = runnable;
	}

	public static synchronized boolean wavesave(byte abyte0[], int i) {
		if (i > 0x1e8480)
			return false;
		if (savereq != null) {
			return false;
		} else {
			wavepos = (wavepos + 1) % 5;
			savelen = i;
			savebuf = abyte0;
			waveplay = true;
			savereq = "sound" + wavepos + ".wav";
			return true;
		}
	}

	public static synchronized boolean wavereplay() {
		if (savereq != null) {
			return false;
		} else {
			savebuf = null;
			waveplay = true;
			savereq = "sound" + wavepos + ".wav";
			return true;
		}
	}

	public static synchronized void midisave(byte abyte0[], int i) {
		if (i > 0x1e8480)
			return;
		if (savereq != null) {
		} else {
			midipos = (midipos + 1) % 5;
			savelen = i;
			savebuf = abyte0;
			midiplay = true;
			savereq = "jingle" + midipos + ".mid";
		}
	}

	public static void reporterror(String s) {
		System.out.println("Error: " + s);
	}

	private Signlink() {
	}

	public static final int clientversion = 317;
	public static long uid;
	public static int storeid = 32;
	public static RandomAccessFile cache_dat = null;
	public static final RandomAccessFile[] cache_idx = new RandomAccessFile[6];
	public static boolean sunjava;
	public static Applet mainapp = null;
	private static boolean active;
	private static int threadliveid;
	private static InetAddress socketip;
	private static int socketreq;
	private static Socket socket = null;
	private static int threadreqpri = 1;
	private static Runnable threadreq = null;
	private static String dnsreq = null;
	public static String dns = null;
	private static String urlreq = null;
	private static DataInputStream urlstream = null;
	private static int savelen;
	private static String savereq = null;
	private static byte[] savebuf = null;
	private static boolean midiplay;
	private static int midipos;
	public static String midi = null;
	public static int midivol;
	public static int midifade;
	private static boolean waveplay;
	private static int wavepos;
	public static int wavevol;
	public static boolean reporterror = true;
	public static String errorname = "";
}