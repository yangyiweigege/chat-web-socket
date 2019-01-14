package com.chat.springboot.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

/**
 * modelt ---> dto
 * 
 * @author yangyiwei
 * @date 2018年12月24日
 * @time 下午1:56:51
 */
@Slf4j
public class ModelToDtoUtil {

	/**
	 * 将bean中的属性复制到dto中 说明：赋值过程中可能产生异常 但这些异常 均为java反射机制所产生 不会影响最终结果 如
	 * bean中存在的属性(包含父类) dto没有 反射会抛出无该字段异常 注意:1.dto和bean中 最好都填写 set get
	 * 
	 * @param soureObject
	 *            源对象
	 * @param targetObject
	 *            目标类型
	 * @return 目标
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertModelToDto(Object soureObject, Class<T> targetClass) {
		Class<?> soureClass = soureObject.getClass();
		Object resultObject;
		try {
			resultObject = targetClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("无法通过反射实例这个类" + targetClass.getName());
		}
		// 遍历源对象属性,优先复制自身属性
		for (Field sourcefield : soureClass.getDeclaredFields()) {
			// 判定要复制的对象属性 是否存在当前属性
			Field targetField;
			try {
				targetField = targetClass.getDeclaredField(sourcefield.getName());
				if (targetField != null) { // 说明要复制的对象中存在当前属性
					if (targetField.getType().getName().equals(sourcefield.getType().getName())) { // 类型必须一致
						sourcefield.setAccessible(true);
						targetField.setAccessible(true);
						if (sourcefield.get(soureObject) != null) {
							targetField.set(resultObject, sourcefield.get(soureObject));
						}

					}

				}
			} catch (Exception e) {
				log.warn("此处发生了字段不存在或者get方法不存在异常 以及类型不一致无法赋值...无需关注");
			}
		}
		// 此处开始, 若源对象属性 存在父类,则继续复制 父类属性到dto中
		Class<?> sourceParentClass = soureClass.getSuperclass();
		if (sourceParentClass != null) {
			for (Field field : sourceParentClass.getDeclaredFields()) {
				// 判定目标对象中 是否存在其父类属性
				try {
					Field targetField = targetClass.getDeclaredField(field.getName());
					if (targetField != null) { // 此处为了兼容泛型父类 不做处理
						// if
						// (targetField.getType().getName().equals(field.getType().getName()))
						// {
						// 通过子类方法,调用父类属性
						StringBuilder methodName = new StringBuilder();
						methodName.append("get").append(field.getName().substring(0, 1).toUpperCase()
								+ field.getName().substring(1, field.getName().length()));
						Method method = soureClass.getMethod(methodName.toString());
						/*log.info("父类methodName:{}", method);
						Method targetMethod = targetClass.getMethod(methodName.toString());//调用
						targetMethod.invoke(resultObject, method.invoke(soureObject));*/
						targetField.setAccessible(true);
						targetField.set(resultObject, method.invoke(soureObject)); // 将父类属性
																					// 赋值到dto对应属性中
						// }
					}
				} catch (Exception e) {
					log.warn("此处发生了字段不存在或者get方法不存在异常 以及类型不一致无法赋值...无需关注");
				}
			}
		}
		if (targetClass.getGenericInterfaces() != null) {
			log.info("dto存在父类,暂时不处理父类中属性....");
		}
		return (T) resultObject;
	}

}
