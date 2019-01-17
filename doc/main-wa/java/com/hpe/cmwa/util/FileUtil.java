package com.hpe.cmwa.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static File createNewFile(String path, String fileName) throws IOException {

		String fullFileName = buildFullFilePath(path, fileName);
		File file = new File(fullFileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	/**
	 * <pre>
	 * Desc 构建路径 
	 * @param path
	 * @param fileName
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   May 28, 2014 9:47:01 PM
	 * </pre>
	 */
	public static String buildFullFilePath(String path, String fileName) {

		logger.error("buildFullFilePath>>>" + path + "<" + fileName);
		if ((path.endsWith("/") || path.endsWith(File.separator)) && (fileName.startsWith("/") || fileName.startsWith(File.separator))) {
			return path + fileName.substring(1);
		}
		if ((path.endsWith("/") || path.endsWith(File.separator)) || (fileName.startsWith("/") || fileName.startsWith(File.separator))) {
			return path + fileName;
		}

		return path + "/" + fileName;

	}

	/**
	 * <pre>
	 * Desc 创建目录 
	 * @param dirPath
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   May 28, 2014 9:46:45 PM
	 * </pre>
	 */
	public static boolean mkdirs(String dirPath) {

		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			return dirFile.mkdirs();
		}
		return false;
	}

	/**
	 * <pre>
	 * Desc  移动文件
	 * @param fromPath
	 * @param fromFileName
	 * @param toPath
	 * @param toFileName
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   May 28, 2014 9:46:54 PM
	 * </pre>
	 */
	public static boolean renameFile(String fromPath, String fromFileName, String toPath, String toFileName) {

		String fullFilePath = FileUtil.buildFullFilePath(fromPath, fromFileName);
		File currFile = new File(fullFilePath);

		String fullBakPathFilePath = FileUtil.buildFullFilePath(toPath, toFileName);
		File file = new File(fullBakPathFilePath);
		removeFile(file);
		return currFile.renameTo(new File(fullBakPathFilePath));
	}

	public static void writeContentToFile(String basePath, String fileName, String content) throws IOException {
		FileInputStream input = null;
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(basePath + fileName));
			pw.print(content);
			pw.close();
		} catch (Exception ex) {
			logger.error("Error:", ex);
		} finally {
			if (input != null)
				input.close();
		}
	}

	/**
	 * <pre>
	 * Desc 构建路径 
	 * @param path
	 * @param fileName
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   May 28, 2014 9:47:10 PM
	 * </pre>
	 */
	public static String buildFullFilePathForUnix(String path, String fileName) {

		int flag = path.indexOf(":");
		if (flag >= 0) {
			path = path.substring(flag + 1);
		}
		if (path.endsWith("/") || path.endsWith(File.separator)) {
			return path + fileName;
		}
		return path + "/" + fileName;

	}

	/**
	 * <pre>
	 * Desc  关闭reader
	 * @param fileReader
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   May 28, 2014 9:47:22 PM
	 * </pre>
	 */
	public static void closeReader(Reader fileReader) {

		if (fileReader != null) {
			try {
				fileReader.close();
				fileReader = null;
			} catch (IOException e) {
				logger.error("close reader error.", e);
			}
		}

	}

	/**
	 * <pre>
	 * Desc  关闭writer
	 * @param printWriter
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   May 28, 2014 9:47:29 PM
	 * </pre>
	 */
	public static void closeWriter(Writer printWriter) {

		if (printWriter != null) {
			try {
				printWriter.close();
			} catch (Exception e) {
				logger.error("close writer error.", e);
			}
		}
	}

	/**
	 * <pre>
	 * Desc  将指定目录及目录下的子目录和文件均删除
	 * @param path
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   Aug 9, 2014 8:49:24 PM
	 * </pre>
	 */
	public static void deleteDirectory(String path) {

		if (StringUtils.isBlank(path)) {
			return;
		}
		try {
			FileUtils.deleteDirectory(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeDirFiles(String root, String fileExpr) {

		List<File> list = getSubDirFiles(root, fileExpr);
		for (File file : list) {
			file.delete();
		}
	}

	public static void removeFile(String path, String fileName) {

		File file = new File(FileUtil.buildFullFilePath(path, fileName));
		removeFile(file);
	}

	public static void removeFile(String fillAllPath) {

		File file = new File(fillAllPath);
		removeFile(file);
	}

	public static void removeFile(File file) {

		if (file == null) {
			return;
		}
		if (file.exists()) {
			file.delete();
		}
	}

	public static List<File> getSubDirFiles(String root, String fileExpr) {

		List<File> list = new ArrayList<File>();
		getFileLists(root, list, fileExpr);
		return list;
	}

	private static void getFileLists(String root, List<File> list, String fileExpr) {

		File file = new File(root);
		File[] subFile = file.listFiles(filter(fileExpr));
		for (int i = 0; i < subFile.length; i++) {
			if (subFile[i].isFile()) {
				list.add(subFile[i]);
			}
		}

		File[] subDir = file.listFiles();
		for (int i = 0; i < subDir.length; i++) {
			if (subDir[i].isDirectory()) {
				getFileLists(subDir[i].getPath(), list, fileExpr);
			}
		}
	}

	/**
	 * <pre>
	 * Desc  过滤器 
	 * @param fileExpr
	 * @return
	 * @author peter.fu
	 * @refactor peter.fu
	 * @date   May 28, 2014 9:47:38 PM
	 * </pre>
	 */
	private static FilenameFilter filter(final String fileExpr) {

		return new FilenameFilter() {

			public boolean accept(File file, String path) {
				String filename = new File(path).getName();
				return filename.matches(fileExpr);
			}
		};
	}

	public static boolean zipFile(String srcDir, String dsctDir, final String destFileName, List<String> fileNamesList) {
		String zipFile = FileUtil.buildFullFilePath(dsctDir, destFileName);
		try {
			zip(new File(srcDir), new File(zipFile), fileNamesList);
		} catch (Exception e) {
			logger.error("Error:", e);
			return false;
		}
		return true;
	}

	public static File zipOneFile(File file) throws IOException {

		ZipOutputStream output = null;

		try {
			String zipFile = file.getPath().replace(".csv", ".zip").replace(".CSV", ".zip");

			System.out.println(zipFile);

			output = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));

			// 压缩文件

			output.putNextEntry(new ZipEntry(file.getName()));

			FileInputStream input = new FileInputStream(file);

			int readLen = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1)
				output.write(buffer, 0, readLen);

			output.flush();
			// 关闭流
			if (input != null)
				input.close();

			logger.error("zipOneFile>>" + zipFile);

			return new File(zipFile);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 关闭流
			if (output != null) {
				output.flush();
				output.close();
			}
		}
		return null;

	}

	/**
	 * 批量压缩文件
	 * @param fs
	 * @param zipFile
	 * @return
	 * @throws IOException
	 */
	public static File zipFile(File[] fs, String zipFile) throws IOException {
		ZipOutputStream output = null;

		try {

			output = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));

			for (File file : fs) {

				output.putNextEntry(new ZipEntry(file.getName()));

				FileInputStream input = new FileInputStream(file);

				int readLen = 0;
				byte[] buffer = new byte[1024 * 8];
				while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1)
					output.write(buffer, 0, readLen);

				output.flush();
				// 关闭流
				if (input != null)
					input.close();

			}

			return new File(zipFile);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 关闭流
			if (output != null) {
				output.flush();
				output.close();
			}
		}
		return null;

	}

	/**
	 * 压缩docfile和csvfile，生成zipfile tianyue
	 * 
	 * @param docFile
	 * @param csvFile
	 * @return zipfile
	 * @throws IOException
	 */
	public static File zipFile(File docFile, File csvFile) throws IOException {

		ZipOutputStream output = null;

		try {
			String zipFile = docFile.getPath().replace(".doc", ".zip");

			System.out.println(zipFile);

			output = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));

			// 压缩文件

			output.putNextEntry(new ZipEntry(docFile.getName()));

			FileInputStream input = new FileInputStream(docFile);

			int readLen = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1)
				output.write(buffer, 0, readLen);

			output.flush();
			// 关闭流
			if (input != null)
				input.close();

			output.putNextEntry(new ZipEntry(csvFile.getName()));

			input = new FileInputStream(csvFile);
			readLen = 0;
			buffer = new byte[1024 * 8];
			while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1)
				output.write(buffer, 0, readLen);

			// 关闭流
			if (input != null)
				input.close();

			return new File(zipFile);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 关闭流
			if (output != null) {
				output.flush();
				output.close();
			}
		}
		return null;

	}

	/**
	 * <pre>
	 * Desc  压缩文件file成zip文件zipFile
	 * @param file 要压缩的文件
	 * @param zipFile 压缩文件存放地方
	 * @throws Exception
	 * @author wilson
	 * @refactor wilson
	 * @date   2013-3-29 下午04:58:08
	 * </pre>
	 */
	public static void zip(File file, File zipFile, final List<String> fileNamesList) throws Exception {
		ZipOutputStream output = null;
		try {
			output = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
			// 顶层目录开始
			zipFile(output, file, "", fileNamesList);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 关闭流
			if (output != null) {
				output.flush();
				output.close();
			}
		}
	}

	/**
	 * <pre>
	 * Desc  压缩文件为zip格式
	 * @param output ZipOutputStream对象
	 * @param file 要压缩的文件或文件夹
	 * @param basePath 条目根目录
	 * @throws IOException
	 * @author wilson
	 * @refactor wilson
	 * @date   2013-3-29 下午04:58:31
	 * </pre>
	 */
	private static void zipFile(ZipOutputStream output, File file, String basePath, final List<String> fileNamesList) throws IOException {
		FileInputStream input = null;
		try {
			// 文件为目录
			if (file.isDirectory()) {
				// 得到当前目录里面的文件列表
				// File list[] = file.listFiles();
				File list[] = file.listFiles(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						if (fileNamesList == null)
							return true;
						for (String fileName : fileNamesList) {
							if (name.equals(fileName))
								return true;
						}
						return false;
					}
				});
				if (list == null || list.length == 0)
					return;
				// 循环递归压缩每个文件
				for (File f : list)
					zipFile(output, f, basePath, fileNamesList);
			} else {
				// 压缩文件
				basePath = (basePath.length() == 0 ? "" : basePath + "/") + file.getName();
				// System.out.println(basePath);
				output.putNextEntry(new ZipEntry(basePath));
				input = new FileInputStream(file);
				int readLen = 0;
				byte[] buffer = new byte[1024 * 8];
				while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1)
					output.write(buffer, 0, readLen);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 关闭流
			if (input != null)
				input.close();
		}
	}

	public static void main(String[] args) {
		// buildFullFilePath("/test/aaa", "a.txt");
		// mkdirs("c:/1/2/2/2/2");
		// mkdirs("c:/a1/a2/a/a2/a2");
		// List<String> list = new ArrayList<String>();
		// list.add("10201.txt");
		//
		// zipFile("D:/1", "D:/2", "2.zip", list);

		System.out.println(buildFullFilePath("/test/aaa/", "/a.txt"));
		System.out.println(buildFullFilePath("/test/aaa/", "a.txt"));
		System.out.println(buildFullFilePath("/test/aaa", "/a.txt"));
		System.out.println(buildFullFilePath("/test/aaa", "a.txt"));
	}

	/**
	 * Desc 把一个File类型的文件转换成byte形式，以供下载使用
	 * 
	 * @param exportFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author fuwei
	 * @refactor fuwei
	 * @date 2012-12-26 上午01:41:08
	 */
	public static byte[] getBytesFile(File exportFile) throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(exportFile);
		int length = fis.available();
		byte[] bytes = new byte[length];
		fis.read(bytes);
		return bytes;
	}

	/**
	 * Desc 把一段文件流信息保存进一个文件里
	 * 
	 * @param fileContent
	 * @param filePathAndName
	 * @throws IOException
	 * @author fuwei
	 * @date 2012-12-14 上午10:42:05
	 */
	public static void writeFile(String fileContent, String filePathAndName) throws IOException {

		File file = new File(filePathAndName);
		file.createNewFile();
		FileWriter resultFile = new FileWriter(file);
		PrintWriter myNewFile = new PrintWriter(resultFile);
		myNewFile.println(fileContent);
		resultFile.close();
	}

}
