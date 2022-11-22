package cn.note.service.toolkit.git;

/**
 * git函数式
 */
@FunctionalInterface
public interface GitConsumer {
    void accept(GitTemplate gitTemplate) throws Exception;
}