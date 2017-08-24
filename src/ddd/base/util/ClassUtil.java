package ddd.base.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;

import ddd.base.Config;
import ddd.base.persistence.baseEntity.Entity;

public class ClassUtil {

	/** 获取类加载器 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/** 获取类路径 */
	public static String getClassPath() {
		String classpath = "";
		URL resource = getClassLoader().getResource("");
		if (resource != null) {
			classpath = resource.getPath();
		}
		return classpath;
	}

	private static File[] getClassFiles(String packagePath) {
		return new File(packagePath).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(".class"))
						|| file.isDirectory();
			}
		});
	}

	private static String getClassName(String packageName, String fileName) {
		String className = fileName.substring(0, fileName.lastIndexOf("."));
		if (StringUtils.isNotEmpty(packageName)) {
			className = packageName + "." + className;
		}
		return className;
	}

	/** 加载类 */
	public static Class<? extends Entity> loadClass(String className,
			boolean isInitialized) {
		Class<? extends Entity> cls;
		try {
			cls = (Class<? extends Entity>) Class.forName(className,
					isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			// logger.error("加载类出错！", e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return cls;
	}

	private static String getSubPackagePath(String packagePath, String filePath) {
		String subPackagePath = filePath;
		if (StringUtils.isNotEmpty(packagePath)) {
			subPackagePath = packagePath + "/" + subPackagePath;
		}
		return subPackagePath;
	}

	private static String getSubPackageName(String packageName, String filePath) {
		String subPackageName = filePath;
		if (StringUtils.isNotEmpty(packageName)) {
			subPackageName = packageName + "." + subPackageName;
		}
		return subPackageName;
	}

	/**
	 * 获取所有的带有指定的注解的Class列表
	 * @param packageNames 包名
	 * @param annotationClass 注解
	 * @return
	 */
	public static List<Class<? extends Entity>> getClassListByAnnotation(
			String packageNames, Class<? extends Annotation> annotationClass) {
		List<Class<? extends Entity>> classList = new ArrayList<Class<? extends Entity>>();
		try {
			String[] names = packageNames.trim().split(",");
			for (String packageName : names) {
				System.out.println(packageName);
				Enumeration<URL> urls = getClassLoader().getResources(
						packageName.replace(".", "/"));

				while (urls.hasMoreElements()) {
					URL url = urls.nextElement();
					System.out.println(url);
					// System.out.println("File:"+url.getFile());
					if (url != null) {
						String protocol = url.getProtocol();
						if (protocol.equals("file")) {
							// System.out.println("this is file");
							String packagePath = url.getPath();
							packagePath = URLDecoder.decode(packagePath,
									Config.CHARSET_UTF8);
							// System.out.println(packagePath);
							addClassByAnnotation(classList, packagePath,
									packageName, annotationClass);
						} else if (protocol.equals("jar")) {
							JarURLConnection jarURLConnection = (JarURLConnection) url
									.openConnection();
							JarFile jarFile = jarURLConnection.getJarFile();
							Enumeration<JarEntry> jarEntries = jarFile
									.entries();
							while (jarEntries.hasMoreElements()) {
								JarEntry jarEntry = jarEntries.nextElement();
								String jarEntryName = jarEntry.getName();
								if (jarEntryName.endsWith(".class")) {
									String className = jarEntryName.substring(
											0, jarEntryName.lastIndexOf("."))
											.replaceAll("/", ".");
									Class<? extends Entity> cls = loadClass(className, false);
									if (cls.isAnnotationPresent(annotationClass)) {
										classList.add(cls);
									}
								}
							}
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		// 打印所有已经扫描到的 class
		// System.err.println(Arrays.deepToString(classList.toArray()));
		//
		return classList;
	}

	private static void addClassByAnnotation(
			List<Class<? extends Entity>> classList, String packagePath,
			String packageName, Class<? extends Annotation> annotationClass) {
		try {
			File[] files = getClassFiles(packagePath);
			if (files != null) {
				for (File file : files) {
					String fileName = file.getName();
					// System.out.println("fileName:"+fileName);
					if (file.isFile()) {
						String className = getClassName(packageName, fileName);
						Class<? extends Entity> cls = loadClass(className,
								false);
						if (cls.isAnnotationPresent(annotationClass)) {
							classList.add(cls);
							// System.out.println("adding");
						}
					} else {
						String subPackagePath = getSubPackagePath(packagePath,
								fileName);
						String subPackageName = getSubPackageName(packageName,
								fileName);
						addClassByAnnotation(classList, subPackagePath,
								subPackageName, annotationClass);
					}
				}
			}
		} catch (Exception e) {
			// logger.error("添加类出错！", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取嵌套的字段
	 * @param clazz
	 * @return 获取所有的字段
	 */
	public static Field[] getClassNestedFields(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		//获取到Entity之前的所有父类的字段
		while (clazz != Entity.class) {
			clazz = clazz.getSuperclass();
			Field[] superFields = clazz.getDeclaredFields();
			superFields = Arrays.copyOf(superFields, superFields.length
					+ fields.length);
			System.arraycopy(fields, 0, superFields, superFields.length
					- fields.length, fields.length);// 将第二个数组与第一个数组合并
			fields = superFields;
		}
		return fields;
	}

	/**获取给定类的所有方法
	 * @param clazz
	 * @return 获取到的所有方法
	 */
	public static Method[] getClassNestedMethods(Class<?> clazz) {
		Method[] fields = clazz.getDeclaredMethods();
		//获取到Entity之前的所有父类的方法
		while (clazz != Entity.class) {
			clazz = clazz.getSuperclass();
			Method[] superMethods = clazz.getDeclaredMethods();
			superMethods = Arrays.copyOf(superMethods, superMethods.length
					+ fields.length);
			System.arraycopy(fields, 0, superMethods, superMethods.length
					- fields.length, fields.length);// 将第二个数组与第一个数组合并
			fields = superMethods;
		}
		return fields;
	}

	public static boolean isBaseType(Class<?> clazz) {
		if (clazz.isPrimitive() || java.lang.Boolean.class.equals(clazz)
				|| java.lang.Character.class.equals(clazz)
				|| java.lang.Byte.class.equals(clazz)
				|| java.lang.Short.class.equals(clazz)
				|| java.lang.Integer.class.equals(clazz)
				|| java.lang.Long.class.equals(clazz)
				|| java.lang.Float.class.equals(clazz)
				|| java.lang.Double.class.equals(clazz)
				|| String.class.equals(clazz)
				|| java.util.Date.class.equals(clazz)
				|| java.sql.Date.class.equals(clazz)
				|| java.sql.Timestamp.class.equals(clazz)) {
			return true;
		}
		return false;
	}

}
