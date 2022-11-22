package cn.note.service.toolkit.autostatic.tree;

import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: jee
 * @time: 2022/1/17 10:43
 */
@Setter
@Getter
public class DataNode {
    private int id;
    private int pid;
    private String fileName;
    private String fileDate;
    /**
     * 展示标题
     */
    private String title;
    /**
     * 打开路径
     */
    private String url;
    /**
     * 是否展开
     */
    private boolean open;

    @Override
    public String toString() {
        return "DataNode{" +
                "id=" + id +
                ", pid=" + pid +
                ", fileName='" + fileName + '\'' +
                ", fileDate='" + fileDate + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", open=" + open +
                '}';
    }
}
