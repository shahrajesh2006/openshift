package com.loyalty.web.utils;

public class Conversion {
	/**
	 * Turns array of bytes into a hex string.
	 *
	 * @param buf
	 *            Array of bytes to convert to hex string
	 * @return Generated hex string
	 */
	public static String byteToHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10) {
				strbuf.append("0");
			}
			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	/**
	 * Converts hex string to a byte array
	 */
	public static byte[] hexStringToByteArray(String hex) {
		int len = hex.length();
		int index = 0;
		int i = 0;
		byte[] byteArray = new byte[len / 2];
		for (i = 0; i < len - 1; i++) {
			byteArray[index] = hex2byte(hex.charAt(i), hex.charAt(i + 1));
			i = i + 1;
			index++;
		}
		return byteArray;
	}

	/**
	 * Converts hex digit to a byte
	 */
	private static byte hex2byte(char high, char low) {
		int hi = 0;
		int lo = 0;
		int sum = 0;
		hi = Character.digit(high, 16);
		lo = Character.digit(low, 16);
		sum = hi * 16 + lo;

		if (sum > 127) {
			sum = sum - 256;
			return (byte) sum;
		}

		return (byte) sum;

	}
}
