package ddd.base.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ddd.base.Config;

public class MD5Util {
	private static MessageDigest digest;

	public static MessageDigest getDigest() {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return digest;
	}

	public static void setDigest(MessageDigest digest) {
		MD5Util.digest = digest;
	}

	/**
	 * 通过指定的文件名获取md5值
	 * 
	 * @param fileName 文件名
	 * @return md5值
	 */
	public static String getMD5ThrowFileName(String fileName) {
		return getMd5ByFile(new File(fileName));
	}

	/**
	 * 获取指定文件的md5值
	 * 
	 * @param file
	 *            文件
	 * @return md5值
	 */
	public static String getMd5ByFile(File file) {
		String value = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(
					FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	public static String getMD5(String str) {
		byte[] bytes = getDigest().digest(str.getBytes());
		return new BigInteger(1, bytes).toString(16);
	}
	 
	/** 获取一个文件的md5值 */
	public static String getMD5(File file) throws Exception {
		if (!file.exists()) {
			throw new Exception("文件不存在");
		}
		MessageDigest digest = MessageDigest.getInstance("MD5");
		;
		try {
			BufferedInputStream inputStream = new BufferedInputStream(
					new FileInputStream(file));
			byte[] bytes = new byte[1024 * 8];
			int len = 0;
			while ((len = inputStream.read(bytes)) != -1) {
				digest.update(bytes, 0, len);
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new BigInteger(1, digest.digest()).toString(16);
	}

	public static void main(String[] args) throws Exception {
		String str1 = "admin";
		System.out.println(getMD5(str1));
		String appFileName = Config.applicationPath
				+ "\\WebContent\\ddd\\simple\\js\\app.js";
		long start = System.currentTimeMillis();
		System.out.println(getMD5(new File(appFileName)));
		System.out.println("用时：" + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();
		System.out.println(getMD5(new File("D:/响应式web设计浅析.ppt")));
		System.out.println("用时：" + (System.currentTimeMillis() - start));
	}

}
