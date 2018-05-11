package ksd.binews.utils;

import java.io.ByteArrayOutputStream;

public class Base64 {

	private static final char[] encodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	private static byte[] decodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1,
			-1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29,
			30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };
	
	public static String encode(byte[] data) {
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		int b1, b2, b3;

		while (i < len) {
			b1 = data[i++] & 0xff;
			if (i == len) {
				sb.append(encodeChars[b1 >>> 2]);
				sb.append(encodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len) {
				sb.append(encodeChars[b1 >>> 2]);
				sb.append(encodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
				sb.append(encodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(encodeChars[b1 >>> 2]);
			sb.append(encodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
			sb.append(encodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
			sb.append(encodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}

	public static byte[] decode(String str) throws Exception {
		byte[] data = str.getBytes("GBK");
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		int i = 0;
		int b1, b2, b3, b4;

		while (i < len) {

			/* b1 */
			do {
				b1 = decodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}

			/* b2 */
			do {
				b2 = decodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			buf.write((b1 << 2) | ((b2 & 0x30) >>> 4));

			/* b3 */
			do {
				b3 = data[i++];
				if (b3 == 61) {
					return buf.toByteArray();
				}
				b3 = decodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			buf.write(((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2));

			/* b4 */
			do {
				b4 = data[i++];
				if (b4 == 61) {
					return buf.toByteArray();
				}
				b4 = decodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1) {
				break;
			}
			buf.write(((b3 & 0x03) << 6) | b4);
		}
		return buf.toByteArray();
	}
}
