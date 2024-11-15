package develop.toolkit.base.utils;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 实例对象处理增强工具
 *
 * @author qiushui on 2019-02-20.
 */
@SuppressWarnings("unused")
public final class ObjectAdvice {

    /**
     * 值是否在数组里
     */
    @SafeVarargs
    public static <T> boolean valueIn(@NonNull T obj, T... targets) {
        for (T target : targets) {
            if (obj.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 值是否不在数组里
     */
    @SafeVarargs
    public static <T> boolean valueNotIn(@NonNull T obj, T... targets) {
        for (T target : targets) {
            if (obj.equals(target)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 反射设置值
     *
     * @param instance             实例
     * @param field                字段
     * @param value                值
     * @param firstUseSetterMethod 优先使用setter方法
     */
    @SneakyThrows
    public static void set(Object instance, Field field, Object value, boolean firstUseSetterMethod) {
        if (firstUseSetterMethod) {
            try {
                final String setterMethodName = JavaBeanUtils.getSetterMethodName(field.getName());
                MethodUtils.invokeMethod(instance, true, setterMethodName);
            } catch (NoSuchMethodException e) {
                FieldUtils.writeField(field, instance, value, true);
            }
        } else {
            FieldUtils.writeField(field, instance, value, true);
        }
    }

    /**
     * 反射设置值
     *
     * @param instance             实例
     * @param fieldName            字段
     * @param value                值
     * @param firstUseSetterMethod 优先使用setter方法
     */
    @SneakyThrows
    public static void set(Object instance, String fieldName, Object value, boolean firstUseSetterMethod) {
        if (firstUseSetterMethod) {
            try {
                final String setterMethodName = JavaBeanUtils.getSetterMethodName(fieldName);
                MethodUtils.invokeMethod(instance, true, setterMethodName);
            } catch (NoSuchMethodException e) {
                FieldUtils.writeField(instance, fieldName, value, true);
            }
        } else {
            FieldUtils.writeField(instance, fieldName, value, true);
        }
    }

    /**
     * 反射获取值
     *
     * @param instance             实例
     * @param field                字段
     * @param firstUseGetterMethod 优先使用getter方法
     * @return 反射值
     */
    @SneakyThrows
    public static Object get(Object instance, Field field, boolean firstUseGetterMethod) {
        if (firstUseGetterMethod) {
            try {
                final String getterMethodName = JavaBeanUtils.getGetterMethodName(field.getName(), field.getType());
                return MethodUtils.invokeMethod(instance, true, getterMethodName);
            } catch (NoSuchMethodException e) {
                return FieldUtils.readField(instance, field.getName(), true);
            }
        } else {
            return FieldUtils.readField(instance, field.getName(), true);
        }
    }

    /**
     * 反射获取值
     *
     * @param instance             实例
     * @param fieldName            字段
     * @param firstUseGetterMethod 优先使用getter方法
     * @return 反射值
     */
    @SneakyThrows
    public static Object get(Object instance, String fieldName, boolean firstUseGetterMethod) {
        if (firstUseGetterMethod) {
            try {
                Field field = instance.getClass().getField(fieldName);
                final String getterMethodName = JavaBeanUtils.getGetterMethodName(fieldName, field.getType());
                return MethodUtils.invokeMethod(instance, true, getterMethodName);
            } catch (NoSuchMethodException e) {
                return FieldUtils.readField(instance, fieldName, true);
            }
        } else {
            return FieldUtils.readField(instance, fieldName, true);
        }
    }

    /**
     * 读取全部字段值
     *
     * @param instance 实例
     * @return 所有字段值
     */
    public static Map<Field, Object> readAllFieldValue(Object instance) {
        Map<Field, Object> map = new HashMap<>();
        Field[] fields = FieldUtils.getAllFields(instance.getClass());
        for (Field field : fields) {
            map.put(field, get(instance, field, true));
        }
        return map;
    }

    /**
     * 安静地使用无参构造方法new对象
     */
    @SneakyThrows
    public static <T> T newInstanceQuietly(Class<T> clazz) {
        return clazz.getConstructor().newInstance();
    }

    @SneakyThrows
    public static <T> T newInstanceQuietly(Class<T> clazz, Class<?>[] parameterClasses, Object... values) {
        return clazz.getConstructor(parameterClasses).newInstance(values);
    }

    /**
     * 字符串值转化成基本类型值
     */
    public static Object primitiveTypeCast(String value, Class<?> clazz) {
        if (value == null) {
            return null;
        } else if (clazz == String.class) {
            return value;
        } else if (clazz == int.class || clazz == Integer.class) {
            return Integer.parseInt(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return Long.parseLong(value);
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (clazz == float.class || clazz == Float.class) {
            return Float.parseFloat(value);
        } else if (clazz == double.class || clazz == Double.class) {
            return Double.parseDouble(value);
        } else if (clazz == short.class || clazz == Short.class) {
            return Short.parseShort(value);
        } else if (clazz == char.class || clazz == Character.class) {
            return value.charAt(0);
        } else if (clazz == byte.class || clazz == Byte.class) {
            return Byte.parseByte(value);
        } else {
            throw new ClassCastException();
        }
    }
}
