package cn.note.swing.view.note;

import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.view.note.category.CategoryView;
import cn.note.swing.view.remotegit.RemoteGitView;
import net.miginfocom.swing.MigLayout;

/**
 * 资源面板
 */
public class ResourcesView extends AbstractMigView {

    private CategoryView categoryView;

    private RemoteGitView remoteGitView;

    public ResourcesView(CategoryView categoryView, RemoteGitView remoteGitView) {
        super(true);
        this.categoryView = categoryView;
        this.remoteGitView = remoteGitView;
        super.display();
    }

    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("gap 0,insets 0", "[grow]", "[grow][fill]");
    }

    @Override
    protected void render() {
        view.add(categoryView, "cell 0 0,grow");
        view.add(remoteGitView, "cell 0 1,grow");
    }
}
