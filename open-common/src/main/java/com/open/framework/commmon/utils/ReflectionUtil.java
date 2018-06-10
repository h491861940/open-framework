package com.open.framework.commmon.utils;

import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


/**
 * 
 * 根据字节码反射工具类<br/>
 *         <br/>
 *         功能说明:根据Class获取类的各种信息<br/>
 *         包括:全类名,实现的接口,接口的泛型等<br/>
 *         并提供了根据Class类型获取对应实例对象的方法,以及修改属性,调用对象的方法
 */
public class ReflectionUtil {
	///////// 获取方法对象 ////////

	/**
	 * 获取类的包名
	 * 
	 * @param classInstance
	 *            字节码
	 * @return 类的包名
	 */
	public static String getPackage(Class<?> classInstance) {
		Package pck = classInstance.getPackage();
		if (null != pck) {
			return pck.getName();
		} else {
			return null;
		}
	}

	/**
	 * 获取父类的全类名
	 * 
	 * @param classInstance
	 *            字节码
	 * @return 父类的全类名
	 */
	public static String getSuperClassName(Class<?> classInstance) {
		Class<?> superclass = classInstance.getSuperclass();
		if (null != superclass) {
			return superclass.getName();
		} else {
			return null;
		}
	}

	/**
	 * 获取类的所有接口名
	 * 
	 * @param classInstance
	 *            字节码
	 * @return 类的所有接口名,并保存在list中
	 */
	public static List<String> getInterfaces(Class<?> classInstance) {
		Class<?>[] interfaces = classInstance.getInterfaces();
		if (interfaces.length > 0) {
			List<String> list = new ArrayList<>();
			Stream.of(interfaces).forEach(i -> {
				list.add(i.getSimpleName());
			});
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 获取所有属性的信息
	 * 
	 * @param classInstance
	 *            字节码
	 * @return 获取类的所有属性,保存在List<StringBuilder>中
	 */
	public static List<StringBuilder> getFields(Class<?> classInstance) {
		Field[] fields = classInstance.getDeclaredFields();
		if (null != fields && fields.length > 0) {
			List<StringBuilder> list = new ArrayList<>();
			Stream.of(fields).forEach(f -> {
				StringBuilder sb = new StringBuilder();
				// 修饰符
				String modifier = Modifier.toString(f.getModifiers());
				sb.append(modifier);

				// 属性的类型
				String type = f.getType().getSimpleName();
				sb.append(type + " ");

				// 属性名
				String name = f.getName();
				sb.append(name + "");

				list.add(sb);
			});
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 获取类的所有方法的信息
	 * 
	 * @param classInstance
	 *            字节码
	 * @return 返回类中所有方法,保存到list中
	 */
	public static List<StringBuilder> getMethods(Class<?> classInstance) {
		Method[] methods = classInstance.getDeclaredMethods();
		if (null != methods && methods.length > 0) {
			List<StringBuilder> list = new ArrayList<>();

			Stream.of(methods).forEach(m -> {
				StringBuilder sb = new StringBuilder();

				// 修饰符
				String modifier = Modifier.toString(m.getModifiers());
				sb.append(modifier);
				// 返回值类型
				Class<?> returnType = m.getReturnType();
				sb.append(returnType.getSimpleName());
				// 方法名
				sb.append(m.getName());
				// 形参列表
				sb.append("(");
				Class<?>[] parameterTypes = m.getParameterTypes();
				int length = parameterTypes.length;

				for (int j = 0; j < length; j++) {
					Class<?> parameterType = parameterTypes[j];

					// 形参类型
					String parameterTypeName = parameterType.getSimpleName();

					if (j < length - 1) {
						sb.append(parameterTypeName + ", ");
					} else {
						sb.append(parameterTypeName);
					}

				}

				sb.append(") {}");

				list.add(sb);
			});

			return list;
		} else {
			return null;
		}
	}

	/**
	 * 获取类的所有注解的名字
	 * 
	 * @param classInstance
	 *            字节码
	 * @return 返回类的所有注解的名字
	 */
	public static List<String> getAnnotations(Class<?> classInstance) {
		Annotation[] annotations = classInstance.getAnnotations();
		if (null != annotations && annotations.length > 0) {
			List<String> list = new ArrayList<>();
			Stream.of(annotations).forEach(a -> {
				list.add(a.annotationType().getSimpleName());
			});
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 暴力反射获取字段值
	 * 
	 * @param object
	 *            实例对象
	 * @param fieldName
	 *            属性名
	 * @return 属性值
	 */
	public static Object getFieldValue(Object object, String fieldName) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		} catch (Exception ex) {
			throw new RuntimeException();
		}
	}

	/**
	 * 通过构造函数实例化对象
	 * 
	 * @param classInstance
	 *            字节码
	 * @param parameterTypes
	 *            参数类型
	 * @param initargs
	 *            参数值
	 * @return 实例化后的对象
	 */
	@SuppressWarnings("rawtypes")
	public static Object constructorNewInstance(Class<?> classInstance, Class[] parameterTypes, Object[] initargs) {
		Object object = null;
		Constructor<?> constructor;
		try {
			constructor = classInstance.getDeclaredConstructor(parameterTypes);
			constructor.setAccessible(true);
			object = constructor.newInstance(initargs);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * 根据传入的属性名,修改对应实例的属性值
	 * 
	 * @param classInstance
	 *            类
	 * @param object
	 *            被修改的对象
	 * @param fieldName
	 *            属性名
	 * @param value
	 *            修改后的值
	 */
	public static void setField(Class<?> classInstance, Object object, String fieldName, Object value) {
		Field field;
		try {
			field = classInstance.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据传入的字节码和方法名和方法的形参类型,获取对应的方法实例
	 * 
	 * @param classInstance
	 *            字节码
	 * @param methodName
	 *            方法名
	 * @param paramTypes
	 *            方法的形参的类型
	 * @return 返回该类中的此方法实例{@code Method}
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method getMethod(Class<?> classInstance, String methodName, Class<?>... paramTypes)
			throws NoSuchMethodException, SecurityException {
		Assert.notNull(classInstance, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		Class<?> searchType = classInstance;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (methodName.equals(method.getName())
						&& (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * 根据传入的方法对象和实例对象,调用实例对象的此方法,并返回执行的结果
	 * 
	 * @param method
	 *            需要执行方法
	 * @param object
	 *            执行方法的对象
	 * @param args
	 *            此方法的参数
	 * @return 执行方法后的返回值
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(Method method, Object object, Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		method.setAccessible(true);
		return method.invoke(object, args);
	}

	/**
	 * 获取一个实例对象的类上的泛型
	 * @param object
	 * @return
	 */
	public static Type[] getParameterizedTypes(Object object) {
		Type superclassType = object.getClass().getGenericSuperclass();
		if (!ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
			return null;
		}
		return ((ParameterizedType) superclassType).getActualTypeArguments();
	}

	/**
	 * 获取一个实例对象的类中,对应(List)成员属性上的泛型
	 * @param object
	 * @param FieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Type getParameterizedTypeForFieldList(Object object,String FieldName) throws NoSuchFieldException, SecurityException{
		Type genericType = object.getClass().getDeclaredField(FieldName).getGenericType();
		if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {  
			return ((ParameterizedType) genericType).getActualTypeArguments()[0]; 
        }
		return null;
	}
	/**
	 * 获取一个实例对象的类中,对应(Map)成员属性上的泛型
	 * @param object
	 * @param FieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Type[] getParameterizedTypeForFieldMap(Object object, String FieldName) throws NoSuchFieldException, SecurityException{
		Type genericType = object.getClass().getDeclaredField(FieldName).getGenericType();
		if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
			return ((ParameterizedType) genericType).getActualTypeArguments();
		}
		return null;
	}

	
}
