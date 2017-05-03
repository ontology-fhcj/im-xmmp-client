/**
 * Copyright (c) 2010, 2017, 四川数码物联网络科技有限责任公司. All rights reserved.
 */
package pe.fu.im.client.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 
 * zip工具
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2014年12月1日
 * @since
 * @version
 */
public abstract class ZipUtils {

	private static final int SIZE = 1024 * 8;
	private static final String zipFileExtention = ".zip";
	private static final String fileSeparator = "/";

	/**
	 * 文件压缩
	 * 
	 * @param sourceFile
	 * @param targetZipFile
	 * @param charsetName
	 * @throws IOException
	 */
	public static void compress(File sourceFile, File targetZipFile, String charsetName) throws IOException {
		if (!sourceFile.exists()) {
			throw new IOException("资源文件不存在： " + sourceFile.getAbsolutePath());
		}
		if (targetZipFile == null) {
			targetZipFile = getDefaultZipFilename(sourceFile);
		}
		targetZipFile = new File(FileUtils.getValidFilename(targetZipFile.getAbsolutePath(), zipFileExtention));
		FileUtils.ensureParentPath(targetZipFile);
		OutputStream os = new FileOutputStream(targetZipFile);
		compressToStream(sourceFile, os, charsetName);
	}

	private static File getDefaultZipFilename(File sourceFile) {
		String parentPath = FileUtils.getParentPath(sourceFile);
		if (StringUtils.isEmpty(parentPath)) {
			parentPath = System.getProperty("user.home");
		}
		String filename;
		if (sourceFile.isDirectory()) {
			filename = sourceFile.getName() + zipFileExtention;
		} else {
			filename = FileUtils.getFilenameWithoutfileExtension(sourceFile) + zipFileExtention;
			if (sourceFile.getName().equalsIgnoreCase(filename)) {
				filename = filename + zipFileExtention;
			}
		}
		return new File(parentPath + fileSeparator + filename);
	}

	private static void compressToStream(File sourceFile, OutputStream os, String charsetName) throws IOException {
		if (!sourceFile.exists()) {
			throw new IOException("资源文件不存在");
		}
		if (os == null) {
			throw new IOException("输出流不能为空");
		}
		try (ZipOutputStream zos = new ZipOutputStream(os, Charset.forName(charsetName));) {
			handle(sourceFile, zos, sourceFile.getName());
			zos.flush();
		} finally {
		}
	}

	private static void handle(File srcFile, ZipOutputStream zos, String entryName) throws IOException {
		if (srcFile.isDirectory()) {
			handleDir(srcFile, zos, entryName);
		} else {
			handleFile(srcFile, zos, entryName);
		}
	}

	private static void handleDir(File file, ZipOutputStream zos, String entryName) throws IOException {
		System.out.println("正在压缩(D):" + entryName + fileSeparator);
		ZipEntry entry = new ZipEntry(entryName + fileSeparator);
		zos.putNextEntry(entry);
		zos.closeEntry();
		File[] files = file.listFiles();
		for (File f : files) {
			handle(f, zos, entryName + fileSeparator + f.getName());
		}
	}

	private static void handleFile(File file, ZipOutputStream zos, String entryName) throws IOException {
		System.out.println("正在压缩(F):" + entryName);
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			ZipEntry entry = new ZipEntry(entryName);
			zos.putNextEntry(entry);
			int r = -1;
			byte data[] = new byte[SIZE];
			while ((r = bis.read(data, 0, SIZE)) != -1) {
				zos.write(data, 0, r);
			}
		} finally {
		}
	}

	/**
	 * 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下
	 * 
	 * @param sourceZipFile
	 * @param targetDirFile
	 * @param charsetName
	 * @throws IOException
	 */
	public static void uncompress(File sourceZipFile, File targetDirFile, String charsetName) throws IOException {

		if (!sourceZipFile.exists()) {
			throw new IOException("压缩文件不存在: " + sourceZipFile.getAbsolutePath());
		}
		if (targetDirFile == null) {
			targetDirFile = sourceZipFile.getParentFile();
		}
		String uncompressDir = targetDirFile.getAbsolutePath() + File.separator;

		ZipFile zipFile = null;
		Enumeration<? extends ZipEntry> entries;
		ZipEntry entry = null;
		String entryName = null;

		byte[] buffer = new byte[SIZE];
		int bytes_read;
		FileOutputStream os = null;
		InputStream is = null;

		try {
			zipFile = new ZipFile(sourceZipFile, Charset.forName(charsetName));
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				entry = entries.nextElement();
				entryName = entry.getName();

				if (entry.isDirectory()) {
					System.out.println("正在解压(D):" + entryName);
					new File(uncompressDir + entryName).mkdirs();
				} else {
					System.out.println("正在解压(F):" + entryName);
					File file = new File(uncompressDir + entryName);
					FileUtils.ensureParentPath(file);
					try {
						// 打开文件输出流
						os = new FileOutputStream(file);
						// 从ZipFile对象中打开entry的输入流
						is = zipFile.getInputStream(entry);
						while ((bytes_read = is.read(buffer)) != -1) {
							os.write(buffer, 0, bytes_read);
						}
					} finally {
						if (os != null) {
							try {
								os.close();
							} catch (IOException e) {
							}
						}

						if (is != null) {
							try {
								is.close();
							} catch (IOException e) {
							}
						}
					}
				}
			}
		} finally {
			if (zipFile != null) {
				zipFile.close();
			}
		}
	}

}
