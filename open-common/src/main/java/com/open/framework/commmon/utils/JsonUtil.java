package com.open.framework.commmon.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.util.List;
import java.util.Map;


/**
 * 类JsonUtil.java的实现描述：
 *
 */

public class JsonUtil extends JSON {

    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
    }

    private static final SerializerFeature[] features = { SerializerFeature.WriteMapNullValue, // 输出空置字段
                                                          SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
                                                          SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
                                                          SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
                                                          SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };

    /**
     * Object转String
     * 
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, config, features);
    }

    /**
     *  字符串转map
     * @param str
     * @return
     */
    public static Map strToMap(String str){
        return parseObject(str,Map.class);
    }

    /**
     *
     * @param 字符串转list
     * @return
     */
    public static List strToList(String str){
        return parseObject(str,List.class);
    }

    /**
     * 字符串转对象
     * @param str
     * @param clazz
     * @return
     */
    public static <T> T strToBean(String str,Class<T> clazz){
        return parseObject(str,clazz);
    }
    public static <T> T  mapToBean(Map map,Class<T> clazz){
       String jsonStr= JSONObject.toJSONString(map);
        return parseObject(jsonStr,clazz);
    }
}
