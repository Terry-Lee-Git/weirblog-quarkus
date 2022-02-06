package com.weirblog.util;
 
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
 
/**
 * @author xfcyzq
 * @version 1.0
 */
public class DeleteUselessRepository {
 
	private static String MAVEN_PATH = "/Users/weir/.m2/repository";
 
	/**
	 * 判断是否存在jar
	 * 
	 * @author xfcyzq
	 * @version 1.0
	 * @param file
	 * @return
	 */
	private static boolean judge(File file) {
		boolean isHaveJar = false;
		File[] files = file.listFiles();
		if (files != null && files.length > 0) {
			for (File _file : files) {
				if (_file.getName().endsWith(".jar")) {
					isHaveJar = true;
				}
				if (_file.isDirectory()) {
					boolean isNextHaveJar = judge(_file);
					if (isNextHaveJar) {
						isHaveJar = true;
					}
				}
			}
		}
		if (!isHaveJar) {
			delete(file);
		}
		return isHaveJar;
	}
 
	/**
	 * 删除操作
	 * 
	 * @author xfcyzq
	 * @version 1.0
	 * @param file
	 */
	public static void delete(File file) {
		File[] files = file.listFiles();
		if (files != null && files.length > 0) {
			for (File f : files) {
				if (f.isDirectory()) {
					delete(f);
				}
				f.delete();
				System.out.println("已删除：" + f.getAbsolutePath());
			}
		} else {
			file.delete();
			System.out.println("已删除：" + file.getAbsolutePath());
		}
	}
	
	public static void main(String[] args) {
		File mavenRoot = new File(MAVEN_PATH);
//		if (mavenRoot.exists()) {
//			File[] files = mavenRoot.listFiles();
//			if (files != null && files.length > 0) {
//				for (File file : files) {
////					judge(file);
//					if (file.isDirectory()) { // 文件夹
//						File[] listFiles = file.listFiles();
//						
//					}
//				}
//			}
//		}
		checkFile(mavenRoot);
	}
	
	public static File checkFile(File file) {
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			if (listFiles != null && listFiles.length > 0) {
				for (File file2 : listFiles) {
					checkFile(file2);
				}
			}
		}else {
			File parentFile = file.getParentFile().getParentFile();
			System.out.println(parentFile);
			File[] listFiles = parentFile.listFiles();
			if (listFiles != null && listFiles.length > 1) {
				File maxFile = List.of(parentFile.listFiles()).stream().max(Comparator.comparingLong(File::lastModified)).get();
				for (File file2 : listFiles) {
					if (!maxFile.getName().equals(file2.getName())) {
						deleteDir(file2);
					}
				}
			}
			
		}
		return null;
	}
	
	   /**
		 * 迭代删除文件夹
		 * @param dirPath 文件夹路径
		 */
		public static void deleteDir(File file)
		{
//			File file = new File(dirPath);
			if(file.isFile())
			{
				file.delete();
			}else
			{
				File[] files = file.listFiles();
				if(files == null)
				{
					file.delete();
				}else
				{
					for (int i = 0; i < files.length; i++) 
					{
						deleteDir(files[i]);
					}
					file.delete();
				}
			}
		}

}