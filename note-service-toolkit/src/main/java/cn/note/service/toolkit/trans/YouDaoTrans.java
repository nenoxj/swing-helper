package cn.note.service.toolkit.trans;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.note.service.toolkit.common.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

/**
 * @description: 谷歌翻译
 * @author: jee
 * @time: 2022/2/28 14:47
 */
public class YouDaoTrans {
    private static final String url = "https://translate.google.cn/translate_a/single";

    private static final String LANG_ES = "ES";
    private static final String LANG_ZH = "zh-CN";

    private static ObjectMapper mapper = new ObjectMapper();


    /**
     * 默认翻译中文为英文
     *
     * @param words 翻译内容
     * @return
     */
    public static ResultData<String> query(String words) {
        String result = "";
        try {
            String url = "https://fanyi.youdao.com/translate?&i={}&doctype=json";
            url = StrUtil.format(url, URLEncoder.encode(words, "UTF-8"));
            result = HttpUtil.get(url);
            if (StrUtil.isBlank(result)) {
                return ResultData.fail("未获取到翻译结果!");
            }
            result = mapper.readTree(result).get("translateResult").get(0).get(0).get("tgt").textValue();
            return ResultData.ok(result);
        } catch (Exception e) {
            return ResultData.fail("翻译异常:" + e.getMessage());
        }
    }

}
