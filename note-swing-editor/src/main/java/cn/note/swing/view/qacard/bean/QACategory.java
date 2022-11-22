package cn.note.swing.view.qacard.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

/**
 * QACard 分类目录
 */
@Setter
@Getter
public class QACategory {

    /**
     * 分类信息
     */
    private String categoryName;

    /**
     * 问答列表
     */
    private List<QACard> qaCardList;


    /**
     * 绑定文件信息
     */
    private File categoryFile;
}
