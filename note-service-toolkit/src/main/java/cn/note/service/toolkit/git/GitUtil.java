package cn.note.service.toolkit.git;

import ch.qos.logback.classic.Level;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.note.service.toolkit.regedit.RegEditFactory;
import cn.note.service.toolkit.regedit.RegEditTemplate;
import cn.note.service.toolkit.regedit.RegException;
import cn.note.swing.core.util.LoggerUtil;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * 提供gui页面得克隆
 */
public class GitUtil {

    static {
        /* 用户密码权限验证*/
        LoggerUtil.setLogLevel("org.eclipse.jgit", Level.WARN);
        CustomGitAuthenticator.install();
    }

    private static final Logger log = LoggerFactory.getLogger(GitUtil.class);


    public static GitTemplate clone(String url, @Nonnull File gitDir) throws IOException {
        return clone(url, gitDir, null);
    }


    /**
     * clone url到目录
     * https://gitee.com/liveBetter/note-resources-store.git
     */
    public static GitTemplate clone(String url, @Nonnull File gitDir, CredentialsProvider credentialsProvider) throws IOException {

        if (gitDir.isFile()) {
            throw new IOException(gitDir.getAbsolutePath() + "not directory!");
        }

        if (gitDir.exists()) {
            String[] files = gitDir.list();
            if (files != null && files.length > 0) {
                throw new IOException(gitDir.getAbsolutePath() + " not an empty directory");
            }
        } else {
            gitDir.mkdirs();
        }

        IOException error = null;
        try (
                Git git = Git.cloneRepository()
                        .setCredentialsProvider(credentialsProvider)
//                        .setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
                        .setProgressMonitor(new SimpleProgressMonitor())
                        .setURI(url)
                        .setDirectory(gitDir).call();
        ) {

            return new GitTemplate(git);

        } catch (InvalidRemoteException e) {
            throw new IOException("远程连接异常!", e);
        } catch (TransportException e) {
            removeCredit(url);
            throw new IOException("权限验证异常!", e);
        } catch (GitAPIException e) {
            throw new IOException("JGit调用异常", e);
        }
    }


    /**
     * 判断文件夹是否位文件目录
     *
     * @param dir 文件夹
     */
    public static boolean isGitDir(File dir) {
        File gitDir = FileUtils.getFile(dir, ".git");
        return gitDir.exists();
    }


    /**
     * 通过文件夹创建gitTemplate
     *
     * @param dir 普通文件夹夹目录
     */
    public static GitTemplate loadGitDir(File dir) {
        Repository repository = null;
        try {
            File gitDir = FileUtils.getFile(dir, ".git");
            if (!gitDir.exists()) {
                throw new IllegalArgumentException("不是一个git目录!");
            }
            repository = new FileRepositoryBuilder()
                    .setGitDir(gitDir)
                    .readEnvironment()
                    .build();
            Git git = new Git(repository);
            return new GitTemplate(git);
        } catch (IOException e) {
            throw new IllegalArgumentException("创建git异常!", e);
        }
    }

    /*------------------------------------------自定义信息-----------------------------------------------------------------------*/


    public static GitTemplate cloneCheckCredit(String url, File dir) throws IOException {
        return clone(url, dir, readCreditAsProvider(url));
    }

    /**
     * 加载目录为gittemplate 时,同时检查giturl是否在注册表存在
     * 存在则获取注册表信息
     *
     * @param dir git目录
     */
    public static GitTemplate loadGitDirCheckCredit(File dir) {
        GitTemplate gitTemplate = loadGitDir(dir);
        String gitUrl = gitTemplate.getRemoteUrl();
        gitTemplate.setCredentialsProvider(readCreditAsProvider(gitUrl));
        return gitTemplate;
    }

    /**
     * 存储git信用信息
     *
     * @param gitCredit git信用信息
     */
    public static void storeCredit(GitCredit gitCredit) {
        RegEditFactory.defaultRegEdit().putCrypt(gitCredit.getGitUrl(), JSONUtil.toJsonStr(gitCredit));
    }

    public static void removeCredit(String gitUrl) {
        RegEditFactory.defaultRegEdit().remove(gitUrl);
    }


    public static boolean containsCredit(String gitUrl) {
        try {
            return RegEditFactory.defaultRegEdit().contains(gitUrl);
        } catch (RegException e) {
            log.error("读取注册表信息异常", e);
            return false;
        }
    }

    /**
     * 读取git信息信用
     *
     * @param gitUrl git地址
     */
    public static GitCredit readCredit(String gitUrl) {
        try {
            RegEditTemplate regEditTemplate = RegEditFactory.defaultRegEdit();
            if (!regEditTemplate.contains(gitUrl)) {
                return null;
            }
            String cryptGitInfo = regEditTemplate.getCrypt(gitUrl);
            if (StrUtil.isBlank(cryptGitInfo)) {
                return null;
            }
            return JSONUtil.toBean(cryptGitInfo, GitCredit.class);
        } catch (RegException e) {
            log.error("读取git注册表信息异常!", e);
            return null;
        }

    }


    /**
     * 读取giturl的信用信息
     *
     * @param gitUrl giturl
     */
    public static CredentialsProvider readCreditAsProvider(String gitUrl) {
        CredentialsProvider credentialsProvider = null;
        GitCredit gitCredit = readCredit(gitUrl);
        if (gitCredit != null) {
//            log.info("gitcredit ==>{}", gitCredit);
            credentialsProvider = new UsernamePasswordCredentialsProvider(gitCredit.getUsername(), gitCredit.getPassword());
        }
        return credentialsProvider;
    }
}
