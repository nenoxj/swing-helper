package cn.note.swing.view.qacard;

import cn.note.service.toolkit.filestore.SystemFileManager;
import cn.note.swing.core.util.FrameUtil;
import cn.note.swing.core.view.AbstractMigCard;
import cn.note.swing.core.view.panel.LRCard;
import cn.note.swing.core.view.theme.ThemeFlatLaf;

/**
 * 问答视图
 */
public class QACardView extends AbstractMigCard {

    private QACategoryBoxList qaCategoryBoxList;

    public QACardView() {
    }

    public QACardView(boolean card) {
        super(card);
    }

    @Override
    public LRCard getCardView() {
        return new LRCard(this.getClass(), qaCategoryBoxList, qaCategoryBoxList.getQaCardPanel());

    }

    @Override
    protected void init() {
        qaCategoryBoxList = new QACategoryBoxList();
    }

    @Override
    protected void render() {
        view.add(qaCategoryBoxList, "w 20%,grow");
        view.add(qaCategoryBoxList.getQaCardPanel(), "w 80%,grow");
    }


    public static void main(String[] args) {
        SystemFileManager.updateSystemDir2Default();
        ThemeFlatLaf.install();
        FrameUtil.launchTime(QACardView.class);
    }


}
