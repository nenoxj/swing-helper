package cn.note.service.toolkit.file;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件基本信息类
 */
@Setter
@Getter
public class FileIndex {

    /* 文件名称*/
    private String fileName;

    /* 文件路径*/
    private String relativePath;

    /* 修改时间 */
    private String modifiedDate;

    /*是否目录*/
    private boolean dir;
    /**
     * 对fileName 进行忽略大小写
     */
    private String searchName;

    @Override
    public String toString() {
        String fmt = StrUtil.format("{}( {})", fileName, relativePath);
        return fmt;
    }
}
