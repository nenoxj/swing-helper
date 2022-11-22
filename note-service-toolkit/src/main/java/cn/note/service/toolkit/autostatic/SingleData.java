package cn.note.service.toolkit.autostatic;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @description: 写入json文件到目录
 * @author: jee
 * @time: 2022/3/3 12:40
 */

public class SingleData {

    /**
     * 目录路径
     */
    private String dir;

    /**
     * json数组
     */
    private JSONArray data;


    public SingleData(String dir, JSONArray data) {
        this.dir = dir;
        this.data = data;
    }

    /**
     * 文件路径转换
     * F:\Person_workspace\note-single-html
     * to
     * F:/Person_workspace/note-single-html
     *
     * @return
     */
    public String getIndexHtml() {
//        String index = "chooser:///" + dir + File.separator + "index.html";
        String index =  dir + File.separator + "index.html";
        return index.replaceAll("\\\\", "/");
    }

    /**
     * 更新数据
     *
     * @param data
     * @return
     */
    public void updateData(JSONArray data) {
        this.data = data;
    }


    /**
     * 获取格式化数据
     *
     * @return
     */
    public String getFormatData() {
        return JSONUtil.toJsonPrettyStr(data);
    }

    /**
     * data.js==备份==>data_bak.js
     * 将模板内容写入到data.js
     *
     * @throws IOException
     */
    public void restore() throws IOException {
        String template = ResourceUtil.readUtf8Str("autostatic/_data");
        String newData = StrUtil.replace(template, "{$data}", getFormatData());
        File dataFile = FileUtils.getFile(dir, "data.js");
        if (dataFile.exists()) {
            StaticLog.info("\n data.js 备份至data_bak.js");
            File dataBakFile = FileUtils.getFile(dir, "data_bak.js");
            FileUtils.copyFile(dataFile, dataBakFile);
        }
        StaticLog.info("\n 新数据写入至data.js");
        FileUtils.writeStringToFile(dataFile, newData, CharsetUtil.CHARSET_UTF_8);
    }

    @Override
    public String toString() {
        return "SingleData{" +
                "dir=" + dir + '\n' +
                ", data='" + data + '\'' +
                '}';
    }
}


