package org.apollo.util;

import java.nio.ByteBuffer;


/**
 * A utility class which contains {@link ByteBuffer}-related methods.
 * @author Graham
 */
public final class ByteBufferUtil {
	
	/**
	 * Reads a 'smart' (either a {@code byte} or {@code short} depending on the value) from the specified buffer.
	 * 
	 * @param buffer The buffer.
	 * @return The 'smart'.
	 */
	public static int readSmart(ByteBuffer buffer) {
		int peek = buffer.get(buffer.position()) & 0xFF;
		if (peek < 128) {
			return buffer.get() & 0xFF;
		}
		return (buffer.getShort() & 0xFFFF) - 32768;
	}
	
	public static int readSmart2(ByteBuffer buffer) {
		int baseVal = 0;
		int lastVal = 0;
		while ((lastVal = readSmart(buffer)) == 32767) {
			baseVal += 32767;
		}
		return baseVal + lastVal;
	}

	/**
	 * Reads an unsigned tri byte from the specified buffer.
	 * @param buffer The buffer.
	 * @return The tri byte.
	 */
	public static int readUnsignedTriByte(ByteBuffer buffer) {
		return ((buffer.get() & 0xFF) << 16) | ((buffer.get() & 0xFF) << 8) | (buffer.get() & 0xFF);
	}

	/**
	 * Reads a string from the specified buffer.
	 * @param buffer The buffer.
	 * @return The string.
	 */
	public static String readString(ByteBuffer buffer) {
		StringBuilder bldr = new StringBuilder();
		char c;
		while ((c = (char) buffer.get()) != 10) {
			bldr.append(c);
		}
		return bldr.toString();
	}
	
	/**
	 * Default private constructor to prevent instantiation.
	 */
	private ByteBufferUtil() {

	}

}
