package com.atguigu.gmall.common.util;

import com.google.common.base.CaseFormat;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.Collection;

public class LoyUtils {

    public static <T, E> E copyProperties(T t, E e) {
        BeanUtils.copyProperties(t, e);
        return e;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * 根据指定对象和该对象的属性名取值
     *
     * @param obj       需要反射获取属性值的对象
     * @param fieldName 对象属性名
     * @return 对应属性值
     * @throws Exception 根据给定参数fileName没有在obj对象中找到对应的属性时抛出，即：不存在该属性
     */
    public static Integer getIntegerValueByAttrName(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(underlineToHump(fieldName));
        field.setAccessible(true);
        return (Integer) field.get(obj);
    }

    /**
     * 带下划线的字符串转为驼峰格式（eg: abc_def -> abcDef）
     *
     * @param fieldName 需要转换的字段
     * @return
     */
    public static String underlineToHump(String fieldName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName);
    }

}
