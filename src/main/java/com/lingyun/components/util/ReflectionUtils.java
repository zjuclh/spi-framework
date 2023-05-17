package com.lingyun.components.util;

import com.lingyun.components.spi.SpiBase;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author caolianghong
 * @date 2022/8/8 5:13 下午
 */
public class ReflectionUtils {
    /**
     * 获取SPI接口类型（实现或者继承SpiBase的最具体的接口）
     * @param clazz spi实现类
     * @return
     */
    public static Class<?> getSpiInterfaceClass(Class<?> clazz) {
        if (!SpiBase.class.isAssignableFrom(clazz)) {
            return null;
        }

        Class<?>[] superInterfaces = clazz.getInterfaces();
        for (Class<?> superClazz : superInterfaces) {
            if (SpiBase.class.isAssignableFrom(superClazz)) {
                return superClazz;
            }
        }

        //实际上走不到这里
        return null;
    }

    public static Type getGenericInterfaceType(Class<?> subClass, Class<?> target) {
        Type[] superInterfaces = subClass.getGenericInterfaces();
        for (Type type : superInterfaces) {
            if (isTarget(type, target)) {
                return type;
            }

            if (type instanceof Class<?>) {
                Type result = getGenericInterfaceType((Class<?>)type, target);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    private static boolean isTarget(Type type, Class<?> target) {
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getRawType().equals(target);
        } else {
            return type.equals(SpiBase.class);
        }
    }
}
