package cn.note.swing.view.note.tab.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 选项卡对象
 */
@Setter
@Getter
public class NoteTabBean implements Serializable {

    public NoteTabBean() {
        this.tabFiles = new ArrayList<>(20);
    }


    /* 正在活动的选项卡*/
    private Integer activeIndex;


    /**
     * 打开的文件信息
     */
    private List<String> tabFiles;

}
