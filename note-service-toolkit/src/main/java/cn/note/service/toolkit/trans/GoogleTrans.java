package cn.note.service.toolkit.trans;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.note.service.toolkit.common.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

/**
 * 使用谷歌免费api翻译
 *
 * @author jee
 * 谷歌已停止服务, 只能在VPN下使用
 */
public class GoogleTrans {
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
        return query(words, LANG_ES, LANG_ZH);
    }

    /**
     * // url 路径
     * "https://translate.googleapis.com/translate_a/single?" +
     * "client=gtx&" +
     * "sl=ES" +
     * "&tl=zh-CN" +
     * "&dt=t&q=" + URLEncoder.encode("hello world", "UTF-8")
     * // 翻译结果 [0]==>[0]==>[0]
     * [[["trans result",null,null,3,null,null,[[],[]],[[["",""]],[["",""]]]]],null,"es",null,null,null,null,[]]
     *
     * @param words    查询语句
     * @param langFrom 来源语言
     * @param langTo   目标语言
     * @return
     * @throws Exception
     */
    public static ResultData<String> query(String words, String langFrom, String langTo) {
        String result = "";
        try {
            String url = "https://translate.googleapis.com/translate_a/single?";
            String templateParam = "client=gtx&sl={}&tl={}&dt=t&q={}";
            templateParam = StrUtil.format(templateParam, langFrom, langTo, URLEncoder.encode(words, "UTF-8"));
            result = HttpUtil.get(url.concat(templateParam));
            if (StrUtil.isBlank(result)) {
                return ResultData.fail("未获取到翻译结果!");
            }
            result = mapper.readTree(result).get(0).get(0).get(0).toString();
            return ResultData.ok(result);
        } catch (Exception e) {
            return ResultData.fail("翻译异常:" + e.getMessage());
        }
    }

}
