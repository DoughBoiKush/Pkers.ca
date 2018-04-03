package org.osps.util;

public class ArrayUtils {

	public static <T> boolean contains(T search, T[] array) {
		for (T value : array) {
			if (value == search) {
				return true;
			}
		}
		return false;
	}
	
}