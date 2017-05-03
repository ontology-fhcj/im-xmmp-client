/** 
 * 四川数码物联网络科技有限责任公司  (c)2013-2014  All right reserved. 
 */
package pe.fu.im.client.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 反射工具类
 * 
 * @author <a href='mailto:475961393@qq.com'>Fhcj</a><br/>
 *         2015年4月22日
 * @since
 * @version
 */
public class ReflectUtils {

	public static final class Modifier_ {
		public static final String ABSTRACT = "abstract";
		public static final String DEFAULT = "default";
		public static final String FINAL = "final";
		public static final String NATIVE = "native";
		public static final String PRIVATE = "private";
		public static final String PROTECTED = "protected";
		public static final String PUBLIC = "public";
		public static final String STATIC = "static";
		public static final String TRANSIENT = "transient";
		public static final String VOLATILE = "volatile";
		public static final String SYNCHRONIZED = "synchronized";
	}

	/**
	 * 判断方法时候没有返回值
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isEmptyReturn(Method method) {
		boolean is = true;
		Class<?> returnType = method.getReturnType();
		if (!"void".equals(returnType.getName())) {
			is = false;
		}
		return is;
	}

	/**
	 * 通过类的字节码获取实例
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> Object getInstance(Class<?> clazz) throws Exception {
		Object instance = null;
		if (clazz.isEnum()) {
			T[] enumConstants = (T[]) clazz.getEnumConstants();
			for (T t : enumConstants) {
				instance = t;
				break;
			}
		} else {
			Constructor<?>[] constructors = clazz.getDeclaredConstructors();
			for (Constructor<?> constructor : constructors) {
				// 构造器私有
				if ((constructor.getModifiers() & 2) != 0) {
					instance = clazz;
					break;
				} else
					instance = clazz.newInstance();
				break;

			}
		}
		return instance;
	}

	/**
	 * 获取字段值
	 * 
	 * @param propertyName
	 *            属性名
	 * @param object
	 *            实例对象
	 * @return 字段值
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getFieldValue(String propertyName, Object object) throws Exception {
		PropertyDescriptor pd = new PropertyDescriptor(propertyName, object.getClass());
		Method method = pd.getReadMethod();
		method.setAccessible(true);
		return method.invoke(object);
	}

	/**
	 * 获取字段值
	 * 
	 * @param name
	 * @param obj
	 * @param classes
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(String name, Object obj, Class<T> classes) throws Exception {
		Field declaredField = ReflectUtils.getDeclaredField(obj, name);
		declaredField.setAccessible(true);
		return (T) declaredField.get(obj);
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredField
	 * 
	 * @param object
	 *            : 子类对象
	 * @param fieldName
	 *            : 父类中的属性名
	 * @return 父类中的属性对象
	 */

	public static Field getDeclaredField(Object object, String fieldName) {
		Field field = null;

		Class<?> clazz = object.getClass();
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e1) {
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				return field;
			} catch (Exception e) {
				// 如果这里的异常打印或者往外抛，则就不会执行clazz =
				// clazz.getSuperclass(),最后就不会进入到父类中了
			}
		}

		return null;
	}

	public static Field getDeclaredField(Class<?> clazz, String fieldName) {
		Field field = null;

		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e1) {
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				return field;
			} catch (Exception e) {
				// 如果这里的异常打印或者往外抛，则就不会执行clazz =
				// clazz.getSuperclass(),最后就不会进入到父类中了
			}
		}

		return null;
	}

	public static List<Field> getDeclaredFields(Class<?> clazz) {
		List<Field> allfields = new ArrayList<Field>();

		try {
			Field[] declaredFields = clazz.getDeclaredFields();
			allfields.addAll(Arrays.asList(declaredFields));
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {

				Field[] declaredFields = clazz.getDeclaredFields();
				for (Field field : declaredFields) {
					if (!allfields.contains(field)) {
						allfields.add(field);
					}
				}
			} catch (Exception e) {
				// 如果这里的异常打印或者往外抛，则就不会执行clazz =
				// clazz.getSuperclass(),最后就不会进入到父类中了
			}
		}

		return allfields;

	}

	/**
	 * 设置字段值(会向上找父类)
	 * 
	 * @param obj
	 * @param propertyName
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setSuperClassFieldValue(Object obj, String propertyName, Object value)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = getDeclaredField(obj, propertyName);
		if (field != null) {
			field.setAccessible(true);
			field.set(obj, value);
		}
	}

	/**
	 * 设置字段值
	 * 
	 * @param propertyName
	 *            字段名
	 * @param obj
	 *            实例对象
	 * @param value
	 *            新的字段值
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void setDeclaredFieldValue(Object obj, String propertyName, Object value)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(propertyName);
		if (field != null) {
			field.setAccessible(true);
			field.set(obj, value);
		}
	}

	/**
	 * 验证字段是否存在
	 * 
	 * @param obj
	 * @param propertyName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static boolean existsField(Class<?> clazz, String propertyName) throws SecurityException {
		boolean is = false;
		Field field = null;
		try {
			field = clazz.getField(propertyName);
		} catch (NoSuchFieldException e) {
			// 未找到该字段
			return is;
		}
		if (field != null) {
			is = true;
		}
		return is;
	}

	/**
	 * 验证字段的类型是不是某一类型
	 * 
	 * @param obj
	 * @param propertyName
	 * @param clazz
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static boolean validateDeclaredField(Object obj, String propertyName, Class<?> clazz) throws NoSuchFieldException, SecurityException {
		boolean is = true;
		Field field = obj.getClass().getDeclaredField(propertyName);
		Class<?> type = field.getType();
		if (type != clazz) {
			is = false;
		}
		return is;
	}

	/**
	 * 获取修饰符
	 * 
	 * @param <T>
	 * @param method
	 * @return
	 */
	public static <T> String getModifiers(T t) {
		Member member = null;
		if (t instanceof Member) {
			member = (Member) t;
		} else {
			throw new ClassCastException(t + "不能转换为 java.lang.reflect.Member");
		}
		return Modifier.toString(member.getModifiers());
	}

	/**
	 * 设置字段值
	 * 
	 * @param className
	 *            类的全路径名称
	 * @param methodName
	 *            调用方法名
	 * @param parameterTypes
	 *            参数类型
	 * @param values
	 *            参数值
	 * @param object
	 *            实例对象
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object methodInvoke(String className, String methodName, Class[] parameterTypes, Object[] values, Object object) {
		try {
			Method method = Class.forName(className).getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method.invoke(object, values);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 获得某个接口下所有实现这个接口的类
	 * 
	 * @param c
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> getAllClassByInterface(Class<?> clazz) throws Exception {
		List<Class<?>> returnClassList = null;
		if (clazz.isInterface()) {
			String packageName = clazz.getPackage().getName();
			// 获取当前包下以及子包下所以的类
			List<Class<?>> allClass = getClasses(packageName);
			if (allClass != null) {
				returnClassList = new ArrayList<Class<?>>();
				for (Class<?> classes : allClass) {
					// 判断是否是同一个接口
					if (clazz.isAssignableFrom(classes)) {
						if (!clazz.equals(classes)) {
							returnClassList.add(classes);
						}
					}
				}
			}
		}

		return returnClassList;
	}

	/**
	 * 取得某一类所在包的所有类名
	 * 
	 * @param classLocation
	 * @param packageName
	 * @return
	 */
	public static String[] getPackageAllClassName(String classLocation, String packageName) {
		// 将packageName分解
		String[] packagePathSplit = packageName.split("[.]");
		String realClassLocation = classLocation;
		int packageLength = packagePathSplit.length;
		for (int i = 0; i < packageLength; i++) {
			realClassLocation = realClassLocation + File.separator + packagePathSplit[i];
		}
		File packeageDir = new File(realClassLocation);
		if (packeageDir.isDirectory()) {
			String[] allClassName = packeageDir.list();
			return allClassName;
		}
		return null;
	}

	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param pack
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> getClasses(String packageName) throws Exception {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
		while (dirs.hasMoreElements()) {
			URL url = dirs.nextElement();
			// 得到协议的名称
			String protocol = url.getProtocol();
			// 如果是以文件的形式保存在服务器上
			if ("file".equals(protocol)) {
				// 获取包的物理路径
				String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
				// 以文件的方式扫描整个包下的文件 并添加到集合中
				findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
			} else if ("jar".equals(protocol)) {
				// 如果是jar包文件
				// 定义一个JarFile
				JarFile jar;
				// 获取jar
				jar = ((JarURLConnection) url.openConnection()).getJarFile();
				// 从此jar包 得到一个枚举类
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					// 如果是以/开头的
					if (name.charAt(0) == '/') {
						// 获取后面的字符串
						name = name.substring(1);
					}
					// 如果前半部分和定义的包名相同
					if (name.startsWith(packageDirName)) {
						int idx = name.lastIndexOf('/');
						// 如果以"/"结尾 是一个包
						if (idx != -1) {
							// 获取包名 把"/"替换成"."
							packageName = name.substring(0, idx).replace('/', '.');
						}
						// 如果可以迭代下去 并且是一个包
						if ((idx != -1) || recursive) {
							// 如果是一个.class文件 而且不是目录
							if (name.endsWith(".class") && !entry.isDirectory()) {
								// 去掉后面的".class" 获取真正的类名
								String className = name.substring(packageName.length() + 1, name.length() - 6);
								// 添加到classes
								classes.add(Class.forName(packageName + '.' + className));
							}
						}
					}
				}
			}
		}

		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 * @throws ClassNotFoundException
	 */
	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) throws ClassNotFoundException {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				classes.add(Class.forName(packageName + '.' + className));
			}
		}
	}

	/**
	 * 无返回值类型
	 * 
	 * @author Administrator
	 *
	 */
	public static class Void_ {

		public Void_() {
		}

	}

}