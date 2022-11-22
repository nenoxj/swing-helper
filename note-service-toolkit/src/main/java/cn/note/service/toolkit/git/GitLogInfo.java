package cn.note.service.toolkit.git;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jee
 * @version 1.0
 */
@Setter
@Getter
public class GitLogInfo {
    private String commitId;
    private String commitDate;
    private String description;
    private String author;

    @Override
    public String toString() {
        return "GitLogInfo{" +
                "commitId='" + commitId + '\'' +
                ", commitDate='" + commitDate + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
