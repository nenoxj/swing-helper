package cn.note.swing.view.search;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;

/**
 * 文件基本信息类
 */
@Setter
@Getter
public class FileContext {

    /* 文件名称*/
    private String fileName;

    /* 文件路径*/
    private String relativePath;

    /* 修改时间 */
    private String modifiedDate;

    /* 文本结构 */
    private Document document;

    /* 高亮匹配内容*/
    private String highMatchText;

    /* 显示路径*/
    private String matchPath;
}
