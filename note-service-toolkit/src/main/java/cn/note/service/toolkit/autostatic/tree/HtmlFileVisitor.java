package cn.note.service.toolkit.autostatic.tree;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.note.service.toolkit.file.AbstractTreeFileVisitor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: html文件扫描器
 * 生成json数据结构
 */
@Setter
@Getter
public class HtmlFileVisitor extends AbstractTreeFileVisitor {
    public static final String SUFFIX_HTML = ".html";
    public static final String UTF_8 = "UTF-8";
    public static final String TITLE_TAG = "title";
    public static final String TITLE_SHOW_SUFFIX = "示例";
    public static final String DEFAULT_FOLDER = "default";
    public static final String DEFAULT_FLODER_NAME = "默认";
    private List<DataNode> results = new ArrayList<>();

    /**
     * 根目录
     */
    private Path rootPath;


    public HtmlFileVisitor(String dirPath) {
        this(Paths.get(dirPath));
    }

    public HtmlFileVisitor(Path dirPath) {
        this.rootPath = dirPath;
        if (!rootPath.toFile().isDirectory()) {
            throw new RuntimeException(dirPath + "not directory!");
        }
        // 仅仅获取html文件
        super.setFileFilter(FileFilterUtils.suffixFileFilter("html"));
    }

    @Override
    public void addNode(Path path, boolean isDir) {
        DataNode dataNode = new DataNode();
        File file = path.toFile();
        dataNode.setId(getTid());
        dataNode.setPid(getPid());
        String fileName = file.getName();
        dataNode.setFileName(fileName);
        dataNode.setFileDate(DateUtil.format(FileUtil.lastModifiedTime(file), DatePattern.NORM_DATETIME_PATTERN));
        if (isDir) {
            if (StrUtil.equals(fileName, DEFAULT_FOLDER)) {
                fileName = DEFAULT_FLODER_NAME;
                dataNode.setOpen(true);
            }
            dataNode.setTitle(fileName + TITLE_SHOW_SUFFIX);
        } else {
            dataNode.setTitle(getHtmlTitle(file));
            dataNode.setUrl(createUrl(path, rootPath));
        }
        Console.log(dataNode);
        results.add(dataNode);
    }


    /**
     * 解析html文件的title标签,获取不到则为文件名
     *
     * @param htmlFile html文件
     * @return 返回html文件标题
     */
    private String getHtmlTitle(File htmlFile) {
        String title = htmlFile.getName().replace(SUFFIX_HTML, "");
        try {
            Document document = Jsoup.parse(htmlFile, UTF_8, "");
            Elements tagElement = document.getElementsByTag(TITLE_TAG);
            String htmlTitle = tagElement.text();
            if (StrUtil.isNotBlank(htmlTitle)) {
                title = htmlTitle;
            }
        } catch (IOException e) {
            Console.error("获取标题异常:{}", e.getMessage());
        }
        return title;
    }


    /**
     * 得到相对路径
     *
     * @param filePath 文件路径
     * @param rootPath 根路径
     * @return 相对路径
     */
    private String createUrl(Path filePath, Path rootPath) {
        String fileRelative = filePath.toString().replace(rootPath.toString(), "");
        return "single" + fileRelative.replace("\\", "/");
    }

    public static List<DataNode> scanHtmls(Path dir) throws IOException {
        HtmlFileVisitor htmlFileVisitor = new HtmlFileVisitor(dir);
        Files.walkFileTree(htmlFileVisitor.getRootPath(), htmlFileVisitor);
        return htmlFileVisitor.getResults();
    }
}
