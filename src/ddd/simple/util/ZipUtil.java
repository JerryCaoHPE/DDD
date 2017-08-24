package ddd.simple.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * ZIP压缩解压缩工具
 */
public class ZipUtil {

	public static final String EXT = ".zip";
	private static final String BASE_DIR = "";

	// 符号"/"用来作为目录标识判断符
	private static final String PATH = "/";
	private static final int BUFFER = 1024;

	/**
	 * 压缩
	 * 
	 * @param srcFile
	 *            源路径
	 * @param destPath
	 *            目标路径
	 * @throws Exception
	 */
	public static void compress(List<String> srcPaths, String destPath)
			throws Exception {

		File srcFile = null;
		File destFile = new File(destPath);
		if (destFile.getParentFile().exists() == false) {
			destFile.getParentFile().mkdirs();
		}
		// 对输出文件做CRC32校验
		CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(
				destFile), new CRC32());
		ZipOutputStream zos = new ZipOutputStream(cos);
		zos.setEncoding("gbk");
		for (String srcPath : srcPaths) {
			srcFile = new File(srcPath);
			compress(srcFile, zos, BASE_DIR);
		}
		zos.flush();
		zos.close();
	}

	/**
	 * 压缩
	 * 
	 * @param srcFile
	 *            源路径
	 * @param zos
	 *            ZipOutputStream
	 * @param basePath
	 *            压缩包内相对路径
	 * @throws Exception
	 */
	private static void compress(File srcFile, ZipOutputStream zos,
			String basePath) throws Exception {
		if (srcFile.isDirectory()) {
			compressDir(srcFile, zos, basePath);
		} else {
			compressFile(srcFile, zos, basePath);
		}
	}

	/**
	 * 压缩目录
	 * 
	 * @param dir
	 * @param zos
	 * @param basePath
	 * @throws Exception
	 */
	private static void compressDir(File dir, ZipOutputStream zos,
			String basePath) throws Exception {

		File[] files = dir.listFiles();

		// 构建空目录
		if (files.length < 1) {
			ZipEntry entry = new ZipEntry(basePath + dir.getName() + PATH);

			zos.putNextEntry(entry);
			zos.closeEntry();
		}

		// 递归压缩
		for (File file : files) {
			compress(file, zos, basePath + dir.getName() + PATH);
		}
	}

	/**
	 * 文件压缩
	 * 
	 * @param file
	 *            待压缩文件
	 * @param zos
	 *            ZipOutputStream
	 * @param dir
	 *            压缩文件中的当前路径
	 * @throws Exception
	 */
	private static void compressFile(File file, ZipOutputStream zos, String dir)
			throws Exception {

		/**
		 * 压缩包内文件名定义
		 * 
		 * <pre>
		 * 如果有多级目录，那么这里就需要给出包含目录的文件名
		 * </pre>
		 */
		ZipEntry entry = new ZipEntry(dir + file.getName());

		zos.putNextEntry(entry);

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file));

		int count;
		byte data[] = new byte[BUFFER];
		while ((count = bis.read(data, 0, BUFFER)) != -1) {
			zos.write(data, 0, count);
		}
		bis.close();

		zos.closeEntry();
	}

	/**
	 * 解压缩
	 * 
	 * @param 需解压的文件路径
	 *            <pre>
	 * 如果有多级目录，那么这里就需要给出包含目录的文件名
	 * 如果用WinRAR打开压缩包，中文名将显示为乱码
	 * </pre>
	 */
	public static void decompression(String filePath, String extractionPath) {
		int count = -1;
		int index = -1;

		File file = null;
		InputStream is = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			ZipFile zipFile = new ZipFile(filePath);

			Enumeration<?> entries = zipFile.getEntries();

			while (entries.hasMoreElements()) {
				byte buf[] = new byte[BUFFER];

				ZipEntry entry = (ZipEntry) entries.nextElement();

				String filename = entry.getName();
				index = filename.lastIndexOf("/");
				if (index > -1)
					filename = filename.substring(index + 1);

				filename = extractionPath + filename;

				file = new File(filename);
				file.createNewFile();

				is = zipFile.getInputStream(entry);

				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, BUFFER);

				while ((count = is.read(buf)) > -1) {
					bos.write(buf, 0, count);
				}

				fos.close();
				is.close();
			}
			zipFile.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
