package cn.note.swing.view.search;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import cn.note.service.toolkit.file.AbstractIndexManager;
import cn.note.service.toolkit.filestore.FileStore;
import cn.note.service.toolkit.filestore.RelativeFileStore;
import cn.note.swing.core.view.wrapper.HtmlBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 目录索引管理器
 * @author: jee
 */
public class FileContextIndexManager extends AbstractIndexManager<FileContext> {

    private SearchType searchType;

    public FileContextIndexManager(String homeDir) {

        this(new RelativeFileStore(homeDir));
    }

    public FileContextIndexManager(FileStore fileStore) {
        super(fileStore);
        searchType = SearchType.FileName;
    }

    public void setSearchType(SearchType searchType) {
        Console.log("toggle mode:{}", searchType.name().toString());
        this.searchType = searchType;
    }


    @Override
    public List<FileContext> searchIndex(String matchText) {
        if (!isInitCompleted()) {
            throw new IllegalStateException("调用是否isInitCompleted 检查初始化是否完成!!!");
        }

        if (StrUtil.isBlank(matchText)) {
            return Collections.emptyList();
        }

        return getIndexes().values().stream()
                .filter(fileContext -> {
                    switch (searchType) {
                        case FileName:
                            return searchFileName(fileContext, matchText);
                        case FileContext:
                            return searchPTag(fileContext, matchText);
                        case CodeBlock:
                            return searchPreTag(fileContext, matchText);
                        default:
                            return false;
                    }


                })
                .collect(Collectors.toList());
    }

    @Override
    protected FileContext fileToIndexObject(File file) {
        String fileName = file.getName();
        String relativePath = getFileStore().getRelativePath(file);
        String modifiedDate = getFileStore().getModifiedDate(file);
        FileContext fileContext = new FileContext();
        fileContext.setFileName(fileName);
        fileContext.setRelativePath(relativePath);
        fileContext.setModifiedDate(modifiedDate);

        // 解析文件 标签内容
        try {
            Document document = Jsoup.parse(file, CharsetUtil.UTF_8);
            StaticLog.info("-----create document-------" + file.getName());
            fileContext.setDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContext;
    }

    @Override
    public File indexToFileObject(FileContext index) {
        return getFileStore().getFile(index.getRelativePath());
    }


    /**
     * 搜索文件名称
     */
    private boolean searchFileName(FileContext fileContext, String matchText) {
        fileContext.setHighMatchText("");
        fileContext.setMatchPath("");
        String lowerMatchText = matchText.toLowerCase();
        String fileName = fileContext.getFileName();
        String lowerFileName = fileName.toLowerCase();

        int index = lowerFileName.indexOf(lowerMatchText);
        if (index >= 0) {
            String matchChar = fileName.substring(index, index + matchText.length());
            fileName = fileName.replace(matchChar, HtmlBuilder.danger(matchChar));
            fileContext.setHighMatchText(HtmlBuilder.html(fileName));
            // 文件名+ 路径
            String matchPath = StrUtil.format("{}({})", fileContext.getHighMatchText(), fileContext.getRelativePath());
            fileContext.setMatchPath(matchPath);
            return true;
        }
        return false;
    }


    /**
     * 搜索文本标签
     */
    private boolean searchPTag(FileContext fileContext, String matchText) {
        fileContext.setHighMatchText("");
        Elements pTags = fileContext.getDocument().getElementsByTag("p");
        String matchContext = "";
        for (int i = 0; i < pTags.size(); i++) {
            Element element = pTags.get(i);
            int prev = i - 1;
            int next = i + 1;
            String text = element.text();
            if (text.contains(matchText)) {
                if (prev >= 0) {
                    matchContext = matchContext.concat(pTags.get(prev).text()).concat("<br/>");
                }
                matchContext = matchContext.concat(text).concat("<br/>");
                if (next < pTags.size()) {
                    matchContext = matchContext.concat(pTags.get(next).text());
                }
                matchContext = matchContext.replace(matchText, HtmlBuilder.danger(matchText));
                fileContext.setHighMatchText(HtmlBuilder.html(matchContext));
                fileContext.setMatchPath(fileContext.getRelativePath());
                return true;
            }
        }

        return false;
    }


    /**
     * 搜索代码块标签
     * <p>
     * 默认文件匹配第一个 则结束
     */
    private boolean searchPreTag(FileContext fileContext, String matchText) {
        fileContext.setHighMatchText("");
        Elements preTags = fileContext.getDocument().getElementsByTag("pre");
        for (Element pre : preTags) {
            String code = pre.text();
            if (pre.text().contains(matchText)) {
                code = code.replace(matchText, HtmlBuilder.danger(matchText));
                fileContext.setHighMatchText(HtmlBuilder.html(code));
                fileContext.setMatchPath(fileContext.getRelativePath());
                return true;
            }
        }

        return false;
    }
}
