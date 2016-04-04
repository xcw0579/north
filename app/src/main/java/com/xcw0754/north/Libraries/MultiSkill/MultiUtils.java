package com.xcw0754.north.Libraries.MultiSkill;

import java.lang.reflect.Field;

/**
 * Created by xiao on 16-4-4.
 * 这是个多功能的函数集合，通常是一些可以帮助操作的工具。
 */
public class MultiUtils {


    /**
     * 凭类名和资源名称获取资源id，而不需要后缀（比如图片）。
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
