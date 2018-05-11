package ksd.binews.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * 
 * @author Administrator
 *
 */
public class MD5SecurityUtil {

	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * bytes 转 String
	 * 
	 * @param bytes
	 * @return
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		int t;
		for (int i = 0; i < 16; i++) {
			t = bytes[i];
			if (t < 0)
				t += 256;
			sb.append(hexDigits[(t >>> 4)]);
			sb.append(hexDigits[(t % 16)]);
		}
		return sb.toString();
	}

	/**
	 * Md5加密
	 * 
	 * @param input
	 * @param bit
	 * @return
	 */
	public static String MD5(String input, int bit) {
		try {
			return code(input, bit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 加密
	 * 
	 * @param input
	 * @param bit
	 * @return
	 * @throws Exception
	 */
	public static String code(String input, int bit) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
			if (bit == 16)
				return bytesToHex(md.digest(input.getBytes("utf-8"))).substring(8, 24);
			return bytesToHex(md.digest(input.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("Could not found MD5 algorithm.", e);
		}
	}

	/**
	 * Md5加密
	 * 
	 * @param b
	 * @return
	 * @throws Exception
	 */
	public static String MD5_3(String b) throws Exception {
		MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
		byte[] a = md.digest(b.getBytes());
		a = md.digest(a);
		a = md.digest(a);

		return bytesToHex(a);
	}

	/**
	 * 27 md5加密产生，产生128位（bit）的mac 28 将128bit Mac转换成16进制代码 29
	 * 
	 * @param strSrc
	 *            30
	 * @param key
	 *            31
	 * @return 32
	 */
	public static String MD5Encode(String strSrc) {
		return MD5Encode(strSrc, "");
	}

	/**
	 * 27 md5加密产生，产生128位（bit）的mac 28 将128bit Mac转换成16进制代码 29
	 * 
	 * @param strSrc
	 *            30
	 * @param key
	 *            31
	 * @return 32
	 */
	public static String MD5Encode(String strSrc, String key) {

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(strSrc.getBytes("UTF8"));
			StringBuilder result = new StringBuilder(32);
			byte[] temp;
			temp = md5.digest(key.getBytes("UTF8"));
			for (int i = 0; i < temp.length; i++) {
				result.append(Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6));
			}
			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

}
