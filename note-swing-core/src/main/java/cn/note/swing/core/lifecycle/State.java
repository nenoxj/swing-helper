package cn.note.swing.core.lifecycle;

/**
 * 描述组件状态
 *
 * @author jee
 */
public enum State {
    /**
     * 未实例化
     */
    UNINITIALIZED,
    /**
     * 正在实例化
     */
    INITIALIZING,
    /**
     * 实例化完成
     */
    INITIALIZED,
    /**
     * 运行
     */
    RUNNING,
    /**
     * 停止
     */
    STOPPED,
    /**
     * 失败
     */
    FAILED
}
