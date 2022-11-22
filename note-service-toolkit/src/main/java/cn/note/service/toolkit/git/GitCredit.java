package cn.note.service.toolkit.git;

import lombok.Getter;
import lombok.Setter;

/**
 * git信用信息
 *
 * @author jee
 * @version 1.0
 */
@Setter
@Getter
public class GitCredit {
    private String gitUrl;
    private String username;
    private String password;

    public GitCredit() {
    }

    public GitCredit(String gitUrl, String username, String password) {
        this.gitUrl = gitUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "GitCredit{" +
                "gitUrl='" + gitUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
