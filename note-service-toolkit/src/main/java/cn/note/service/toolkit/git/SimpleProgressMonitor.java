package cn.note.service.toolkit.git;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.ProgressMonitor;

/**
 * new TextProgressMonitor(new PrintWriter(System.out));
 *
 * @author jee
 * @version 1.0
 */
@Slf4j
public class SimpleProgressMonitor implements ProgressMonitor {
    @Override
    public void start(int totalTasks) {
    }

    @Override
    public void beginTask(String title, int totalWork) {
        log.debug("git opt taskName:{} ", title);
    }

    @Override
    public void update(int completed) {
    }

    @Override
    public void endTask() {
        log.debug("git done...");
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
