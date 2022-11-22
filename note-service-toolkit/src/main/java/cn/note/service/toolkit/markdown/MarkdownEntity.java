package cn.note.service.toolkit.markdown;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MarkdownEntity {

    // resources 样式
    @Setter
    @Getter
    private String css = "";

    // 最外层的div标签， 可以用来设置样式，宽高，字体等
    private Map<String, String> divStyle = new ConcurrentHashMap<>();

    // 转换后的html文档
    private String html;


    public MarkdownEntity() {
    }

    public MarkdownEntity(String html) {
        this.html = html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getHtml() {
        return "<div " + parseDiv() + ">\n" + html + "\n</div>";
    }

    @Override
    public String toString() {

        return css + "\n" + getHtml();
    }


    private String parseDiv() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : divStyle.entrySet()) {
            builder.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
        }
        return builder.toString();
    }


    public void addDivStyle(String attrKey, String value) {
        if (divStyle.containsKey(attrKey)) {
            divStyle.put(attrKey, divStyle.get(attrKey) + " " + value);
        } else {
            divStyle.put(attrKey, value);
        }
    }


}

