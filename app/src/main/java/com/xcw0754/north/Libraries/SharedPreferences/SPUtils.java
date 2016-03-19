package com.xcw0754.north.Libraries.SharedPreferences;

/**
 * Created by Administrator on 2016/2/15.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;






/**
 * Created by Administrator on 2016/2/14.
 * 一个用于操作SharedPreferences的类，可以以map的形式的xml文件保存用户名及密码等信息。
 * 具体用法可参考：http://www.runoob.com/w3cnote/android-tutorial-sharedpreferences.html
 */

public class SPUtils {
    /**
     * 保存在手机里的SP文件名
     */
    public static final String FILE_NAME = "my_sp";

    /**
     * 增
     */
    public static void put(Context context, String key, Object obj) {
        // 文件默认私有，可以更改。
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (obj instanceof Boolean) {           //  布尔值
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {      //  浮点型
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Integer) {    //  整型
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Long) {       //  长整型
            editor.putLong(key, (Long) obj);
        } else {                                //  其他的全当字符串了
            editor.putString(key, (String) obj);
        }
        editor.commit();                        //  提交到文件中
    }


    /**
     * 查
     */
    public static Object get(Context context, String key, Object defaultObj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        if (defaultObj instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Float) {
            return sp.getFloat(key, (Float) defaultObj);
        } else if (defaultObj instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Long) {
            return sp.getLong(key, (Long) defaultObj);
        } else if (defaultObj instanceof String) {
            return sp.getString(key, (String) defaultObj);
        }
        return null;
    }

    /**
     * 删
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }


    /**
     * 返回所有键值对
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        Map<String, ?> map = sp.getAll();
        return map;
    }

    /**
     * 删除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 检查key对应的数据是否存在
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sp.contains(key);
    }

}