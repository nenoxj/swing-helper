package cn.note.service.toolkit.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jee
 * @version 1.0
 */
@Slf4j
public class ConvertUtil {

    public static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 转换对象为集合类型
     */
    public static <T> List<T> obj2List(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }


    /**
     * json转List对象
     */
    public static <T> List<T> json2List(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, collectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("转换异常:{}", e);
            return null;
        }
    }


    /**
     * json 转map对象
     */
    public static <T> Map<String, T> json2Map(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, collectionType(Map.class, String.class, clazz));
        } catch (IOException e) {
            log.error("转换异常:{}", e);
            return null;
        }
    }


    private static JavaType collectionType(Class<?> collectionClz, Class<?>... elementClz) {
        return mapper.getTypeFactory().constructParametricType(collectionClz, elementClz);
    }
}
