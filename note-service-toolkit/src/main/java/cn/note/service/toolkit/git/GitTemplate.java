package cn.note.service.toolkit.git;

import ch.qos.logback.classic.Level;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.note.swing.core.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RemoteSetUrlCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * https://www.ktanx.com/blog/p/2398
 * git简介操作模板
 */
@Slf4j
public class GitTemplate {
    static {
        /* 用户密码权限验证*/
        LoggerUtil.setLogLevel("org.eclipse.jgit", Level.WARN);
    }

    private String remoteUrl;


    public Git getGit() {
        return git;
    }

    private Git git;


    /**
     * 验证代码, 默认使用username/password
     */
    private CredentialsProvider credentialsProvider;

    /**
     * git操作输出流
     */
    private static ProgressMonitor monitor = new SimpleProgressMonitor();


    public GitTemplate(Git git) {
        this.git = git;
    }


    /**
     * 根据用户名和密码设置凭证
     *
     * @param username 用户名
     * @param password 密码
     */
    public void setCredentialsProvider(String username, String password) {
        setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
    }


    /**
     * @param credentialsProvider 凭证信息
     */
    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    /**
     * 获取git的远程地址
     */
    public String getRemoteUrl() {
        return this.git.getRepository().getConfig().getString("remote", "origin", "url");
    }


    /**
     * 添加至暂存区
     */
    public GitTemplate add() throws GitAPIException {
        // 获取删除和修改
        git.add().setUpdate(true).addFilepattern(".").call();
        // 获取新增
        git.add().addFilepattern(".").call();
        return this;
    }


    /**
     * 提交,提交内容自动填充
     */
    public GitTemplate autoCommit() throws GitAPIException {
        add().commit(statusToMessage());
        return this;
    }


    /**
     * 提交
     *
     * @param message 提交内容
     */
    public GitTemplate commit(String message) throws GitAPIException {
        if (StrUtil.isBlank(message)) {
            throw new IllegalArgumentException("提交记录不能为空!");
        }
        log.info("git commit ...");
        this.git.commit().setAll(true).setMessage(message).call();
        return this;
    }


    /**
     * 拉取
     */
    public GitTemplate pull() throws GitAPIException {
        log.info("git pull ...");
        this.git.pull().setRemote("origin").setRemoteBranchName("master").setProgressMonitor(monitor).setCredentialsProvider(credentialsProvider).call();
        return this;
    }


    /**
     * 推送至远程
     */
    public GitTemplate push() throws GitAPIException {
        log.info("git push ...");
        this.git.push().setProgressMonitor(monitor).setCredentialsProvider(credentialsProvider).call();
        return this;
    }


    /**
     * 关闭
     */
    public void close() {
        if (this.git != null) {
            this.git.getRepository().close();
        }
    }


    /**
     * 移除暂存 与add 成反向操作, 但是删除得文件不能恢复
     * 如果恢复删除得文件 ,需要使用resetHard
     */
    public GitTemplate reset() throws GitAPIException {
        this.git.reset().call();
        return this;
    }


    /**
     * 还原上一个版本, 恢复丢失得文件
     * 也可以用来重置
     */
    public GitTemplate resetHard() throws GitAPIException {
        ResetCommand resetCommand = this.git.reset();
        resetCommand.setMode(ResetCommand.ResetType.HARD);
        resetCommand.call();
        return this;
    }


    /**
     * 本地文件状态
     */
    public JSONObject status() throws GitAPIException {
        Status status = git.status().call();
        JSONObject json = new JSONObject();
        json.putOpt("Added", status.getAdded().toString());
        json.putOpt("Changed", status.getChanged().toString());
        json.putOpt("Conflicting", status.getConflicting().toString());
        json.putOpt("ConflictingStageState", status.getConflictingStageState().toString());
        json.putOpt("IgnoredNotInIndex", status.getIgnoredNotInIndex().toString());
        json.putOpt("Missing", status.getMissing().toString());
        json.putOpt("Modified", status.getModified().toString());
        json.putOpt("Removed", status.getRemoved().toString());
        json.putOpt("UntrackedFiles", status.getUntracked().toString());
        json.putOpt("UntrackedFolders", status.getUntrackedFolders().toString());
        log.debug("status changed :\n{}", json.toStringPretty());
        return json;
    }

    /**
     * 状态记录为消息
     */
    public String statusToMessage() throws GitAPIException {
        Status status = git.status().call();
        return "状态信息:" + JSONUtil.toJsonStr(status.getUncommittedChanges());
    }


    /**
     * 获取本地最新提交版本
     */
    public ObjectId localLastCommitId() throws IOException, GitAPIException {
        return git.getRepository().resolve(getBranchName());
    }

    /**
     * 获取远端 最新提交版本
     */
    public ObjectId remoteLastCommitId() throws GitAPIException, IOException {
        Map<String, Ref> refs = git.lsRemote().setHeads(true).setCredentialsProvider(credentialsProvider).callAsMap();
        return refs.get(getBranchName()).getObjectId();
    }


    /**
     * 获取当前分支名称
     * 如: master
     */
    public String getBranchName() throws IOException {
        return git.getRepository().getFullBranch();
    }


    /**
     * 查询最近5条的提交记录
     */
    public List<GitLogInfo> queryLocalLog(int size) throws IOException, GitAPIException {
        Iterable<RevCommit> iterable = git.log().all().setMaxCount(size).call();
        List<GitLogInfo> gitLogList = new ArrayList<>(size);
        for (RevCommit locate : iterable) {
            GitLogInfo logInfo = new GitLogInfo();
            String locateCommitId = locate.getId().getName();
            logInfo.setCommitId(locateCommitId);
            logInfo.setDescription(locate.getFullMessage());
            logInfo.setAuthor(locate.getAuthorIdent().getName());
            logInfo.setCommitDate(DateUtil.formatDateTime(locate.getCommitterIdent().getWhen()));
            gitLogList.add(logInfo);
        }
        return gitLogList;
    }

    /**
     * isLocalContainsObjectId( remoteLastCommitId())
     * 可以用来查询本地是否已与远程同步
     */
    public boolean isLocalContainsObjectId(ObjectId objectId) {

        Iterable<RevCommit> iterable = null;
        try {
            iterable = git.log().add(objectId).call();
        } catch (GitAPIException | IOException e) {
            log.warn("本地不包含远程提交记录:" + e.getMessage());
        }
        if (iterable != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 本地最新分支是否与远程分支一致
     */
    public boolean isLocalEqRemote(ObjectId localId, ObjectId remoteId) {
        return StrUtil.equals(localId.getName(), remoteId.getName());
    }


    /**
     * 同步远程代码
     */
    public void fetchRemote() throws GitAPIException, IOException {
        this.git.fetch().setCredentialsProvider(credentialsProvider).setForceUpdate(true).call();
        this.pull();
//        this.git.fetch().setRemote("origin").setCredentialsProvider(credentialsProvider).call();
    }


    /**
     * 获取本地最新提交记录
     */
    public RevCommit localLastCommit() throws GitAPIException {
        Repository repo = git.getRepository();
        RevCommit lastCommit = null;
        Iterable<RevCommit> iterable = git.log().setMaxCount(1).call();
        for (RevCommit revCommit : iterable) {
            lastCommit = revCommit;
        }
        Console.log("<========Local Last Commit========>");
        printCommit(lastCommit);
        return lastCommit;
    }


    /**
     * 获取最新提交记录
     */
    public RevCommit remoteLastCommit() throws IOException, GitAPIException {
        File temp = FileUtils.getFile(FileUtils.getTempDirectory(), IdUtil.fastSimpleUUID());
        RevCommit lastCommit = null;

        try {
            // 因为同步问题, 不能实时远程ID
//            RevWalk revWalk = new RevWalk(repo);
//            RevCommit revCommit = revWalk.parseCommit(repo.resolve(repo.getFullBranch()));
            Git remoteGit = Git.cloneRepository()
                    .setURI(getRemoteUrl())
                    .setCredentialsProvider(credentialsProvider)
                    .setDirectory(temp).call();

            Repository repo = remoteGit.getRepository();
            ObjectId lastCommitId = repo.resolve(repo.getFullBranch());
            Console.log("last commit id: {}", lastCommitId);
            Iterable<RevCommit> iterable = remoteGit.log().setMaxCount(1).call();
            for (RevCommit revCommit : iterable) {
                lastCommit = revCommit;
            }
            repo.close();
            remoteGit.close();
        } finally {
            FileUtils.deleteDirectory(temp);
        }
        Console.log("<========Remote Last Commit========>");
        printCommit(lastCommit);
        return lastCommit;
    }


    /**
     * 打印commit信息
     */
    public void printCommit(RevCommit revCommit) {
        String commitId = revCommit.getId().getName();
        String commitMessage = revCommit.getFullMessage();
        String commitTime = DateUtil.formatDateTime(revCommit.getCommitterIdent().getWhen());
        Console.log(" id:{},\n message:{},\n time:{}",
                commitId, commitMessage, commitTime);
    }


    public void setRemote(String url) throws URISyntaxException, GitAPIException {
        RemoteSetUrlCommand command = git.remoteSetUrl();
        command.setRemoteUri(new URIish(url));
        command.setRemoteName("origin");
//        command.setUriType(RemoteSetUrlCommand.UriType.FETCH);
        command.call();
    }


    /**
     * 是否当前目录是否为提交状态 ,否则应该先commit
     *
     * @return 返回目录校验结果
     * @throws GitAPIException
     */
    public boolean notCleanStatus() throws GitAPIException {
        Status status = this.git.status().call();
        return !status.isClean();
    }


}
