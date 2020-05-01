package develop.toolkit.base.utils;

import develop.toolkit.base.struct.KeyValuePairs;
import lombok.NonNull;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 实例对象处理增强工具
 *
 * @author qiushui on 2019-02-20.
 */
public final class ObjectAdvice {

    /**
     * 是否基本类型
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isPrimitiveType(Class<?> clazz) {
        return valueIn(clazz,
                int.class, Integer.class,
                long.class, Long.class,
                float.class, Float.class,
                double.class, Double.class,
                boolean.class, Boolean.class,
                char.class, Character.class,
                short.class, Short.class,
                byte.class, Byte.class
        );
    }

    /**
     * 是否是字节
     *
     * @param obj
     * @return
     */
    public static boolean isByte(Object obj) {
        var clazz = obj.getClass();
        return clazz == byte.class || clazz == Byte.class;
    }

    /**
     * 是否是短整型
     *
     * @param obj
     * @return
     */
    public static boolean isShort(Object obj) {
        var clazz = obj.getClass();
        return clazz == short.class || clazz == Short.class;
    }

    /**
     * 是否是整型
     *
     * @param obj
     * @return
     */
    public static boolean isInt(Object obj) {
        var clazz = obj.getClass();
        return clazz == int.class || clazz == Integer.class;
    }

    /**
     * 是否是长整型
     *
     * @param obj
     * @return
     */
    public static boolean isLong(Object obj) {
        var clazz = obj.getClass();
        return clazz == long.class || clazz == Long.class;
    }

    /**
     * 是否是单精度浮点型
     *
     * @param obj
     * @return
     */
    public static boolean isFloat(Object obj) {
        var clazz = obj.getClass();
        return clazz == float.class || clazz == Float.class;
    }

    /**
     * 是否是双精度浮点型
     *
     * @param obj
     * @return
     */
    public static boolean isDouble(Object obj) {
        var clazz = obj.getClass();
        return clazz == double.class || clazz == Double.class;
    }

    /**
     * 是否是字符型
     *
     * @param obj
     * @return
     */
    public static boolean isChar(Object obj) {
        var clazz = obj.getClass();
        return clazz == char.class || clazz == Character.class;
    }

    /**
     * 是否是布尔型
     *
     * @param obj
     * @return
     */
    public static boolean isBoolean(Object obj) {
        var clazz = obj.getClass();
        return clazz == boolean.class || clazz == Boolean.class;
    }

    /**
     * 值是否在数组里
     *
     * @param obj
     * @param targets
     * @param <T>
     * @return
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
     *
     * @param obj
     * @param targets
     * @param <T>
     * @return
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
     * @param instance
     * @param field
     * @param value
     * @param firstUseSetterMethod 优先使用setter方法
     */
    public static void set(Object instance, String field, Object value, boolean firstUseSetterMethod) {
        try {
            if (firstUseSetterMethod) {
                try {
                    final String setterMethodName = JavaBeanUtils.getSetterMethodName(field);
                    MethodUtils.invokeMethod(instance, true, setterMethodName);
                } catch (NoSuchMethodException e) {
                    FieldUtils.writeDeclaredField(instance, field, value, true);
                }
            } else {
                FieldUtils.writeDeclaredField(instance, field, value, true);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反射获取值
     *
     * @param instance
     * @param field
     * @param fieldType
     * @param firstUseGetterMethod 优先使用getter方法
     * @return
     */
    public static Object get(Object instance, String field, Class<?> fieldType, boolean firstUseGetterMethod) {
        try {
            if (firstUseGetterMethod) {
                try {
                    final String getterMethodName = JavaBeanUtils.getGetterMethodName(field, fieldType);
                    return MethodUtils.invokeMethod(instance, true, getterMethodName);
                } catch (NoSuchMethodException e) {
                    return FieldUtils.readDeclaredField(instance, field, true);
                }
            } else {
                return FieldUtils.readDeclaredField(instance, field, true);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取全部字段值
     *
     * @param instance
     * @return
     */
    public static KeyValuePairs<String, Object> readAllFieldValue(Object instance) {
        Class<?> instanceClass = instance.getClass();
        KeyValuePairs<String, Object> keyValuePairs = new KeyValuePairs<>();

        Field[] fields = instanceClass.getDeclaredFields();
        for (Field field : fields) {
            keyValuePairs.addKeyValue(field.getName(), get(instance, field.getName(), field.getType(), true));
        }
        return keyValuePairs;
    }

    /**
     * 安静地使用无参构造方法new对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T newInstanceQuietly(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串值转化成基本类型值
     *
     * @param value
     * @param clazz
     * @return
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
