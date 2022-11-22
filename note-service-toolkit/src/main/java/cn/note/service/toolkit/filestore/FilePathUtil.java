package cn.note.service.toolkit.filestore;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 解析url 工具类
 *
 * @author jee
 * @version 1.0
 */
public class FilePathUtil {

    public static void main(String[] args) {
        String pathStr = "aaa/app**,/doc.html";
        String[] paths = StrUtil.split(pathStr, ",");
        String[] testUrls = {
                "aaa/appLogin",
                "/aaa/a",
                "/doc.html",
                "doc.html",
        };
        for (String testUrl : testUrls) {
            Console.log("url:{} ,valid:{}", testUrl, FilePathUtil.contains(paths, testUrl));
        }
    }

    /**
     * 转换文件路径
     * \a\b ->/a/b
     *
     * @param path 路径
     * @return 转换后路径
     */
    public static String parseFilePath(String path) {
        return path.replaceAll("\\\\", "/");
    }


    public static String getRelativePath(File file, File home) {
        String path = file.getAbsolutePath().replace(home.getAbsolutePath(), "");
        return parseFilePath(path);
    }


    /**
     * @param path 路径
     * @return 强制路径必须以/开头
     */
    public static String strictPath(String path) {
        return path.startsWith("/") ? path : "/".concat(path);
    }


    /**
     * @param paths 路径集合
     * @param path  对比路径
     * @return 是否包含
     */
    public static boolean contains(List<String> paths, String path) {
        return paths.stream().anyMatch(url -> containsPath(url, path));
    }


    public static boolean contains(String[] paths, String path) {
        for (String origin : paths) {
            if (containsPath(origin, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通配符模式
     *
     * @param origin  - 原始路径 /a/**模式
     * @param compare - 对比路径
     * @return 是否通过
     */

    public static boolean containsPath(String origin, String compare) {
        String regPath = getRegPath(origin);
        return Pattern.compile(regPath).matcher(compare).matches();

    }

    /**
     * 将通配符表达式转化为正则表达式
     *
     * @param path 路径地址
     * @return 转换后路径
     */

    private static String getRegPath(String path) {
        char[] chars = path.toCharArray();
        int len = chars.length;
        StringBuilder sb = new StringBuilder();
        boolean preX = false;
        for (int i = 0; i < len; i++) {
            if (chars[i] == '*') {//遇到*字符
                if (preX) {//如果是第二次遇到*，则将**替换成.*
                    sb.append(".*");
                    preX = false;
                } else if (i + 1 == len) {//如果是遇到单星，且单星是最后一个字符，则直接将*转成[^/]*
                    sb.append("[^/]*");

                } else {//否则单星后面还有字符，则不做任何动作，下一把再做动作
                    preX = true;
                }
            } else {//遇到非*字符
                if (preX) {//如果上一把是*，则先把上一把的*对应的[^/]*添进来
                    sb.append("[^/]*");
                    preX = false;
                }
                if (chars[i] == '?') {//接着判断当前字符是不是?，是的话替换成.
                    sb.append('.');
                } else {//不是?的话，则就是普通字符，直接添进来
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();

    }

}