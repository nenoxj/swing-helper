package cn.note.service.toolkit.formatter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Set;

/**
 * // TODO
 *
 * @description:
 * @author: jee
 * @time: 2022/2/28 16:55
 */
@Slf4j
public class JsonFormatter implements CodeFormatter {
    private static final String SPECIAL_SYMBOL = "at [Source:";

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 返回格式化内容
     * 格式化失败时 必须返回 FormatterException 格式化失败得行数和列数
     *
     * @param originCode 原始code
     * @return
     */
    @Override
    public String format(String originCode) throws FormatterException {
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = null;
        try {
            parser = factory.createParser(originCode);
            JsonNode jsonObj = mapper.readTree(parser);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
        } catch (IOException e) {
            String jsonError = e.getMessage();
            log.error("json format error! ", e.getMessage());
            parseError(jsonError);
        }
        return null;
    }


    private static void parseError(String error) throws FormatterException {
        String prefixError = StrUtil.subBefore(error, SPECIAL_SYMBOL, true);
        String line = StrUtil.subBetween(error, "line: ", ",");
        String column = StrUtil.subBetween(error, "column: ", "]");
        FormatterException.throwError(prefixError, line, column);
    }

    /**
     * 获取json结构
     *
     * @param data 数据json
     * @return json结构
     */
    public static JSONObject getJsonStructure(JSONObject data, int deep) {
        JSONObject structure = new JSONObject();
        loop(structure, data, deep);
        return structure;
    }

    /**
     * 遍历json结构
     *
     * @param structure 结构json
     * @param data      数据
     * @param deep      遍历深度
     */
    private static void loop(JSONObject structure, JSONObject data, int deep) {
        if (deep == 0) {
            return;
        }
        deep--;
        Set<String> keys = data.keySet();
        for (String key : keys) {
            Object obj = data.getObj(key);
            // 获取{}结构
            if (obj instanceof JSONObject) {
                structure.putOpt(key, getJsonStructure((JSONObject) obj, deep));
                // 获取[]结构
            } else if (obj instanceof JSONArray) {
                JSONArray datas = (JSONArray) obj;
                if (datas.size() > 0) {
                    JSONObject arrData = datas.getJSONObject(0);
                    JSONObject arrResult = getJsonStructure(arrData, deep);
                    JSONArray arr = new JSONArray();
                    arr.put(arrResult);
                    structure.putOpt(key, arr);
                } else {
                    JSONArray arr = new JSONArray();
                    structure.putOpt(key, arr);
                }
            } else {
                structure.putOpt(key, "");
            }

        }
    }


}
