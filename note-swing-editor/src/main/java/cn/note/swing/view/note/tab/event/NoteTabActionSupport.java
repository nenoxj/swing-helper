package cn.note.swing.view.note.tab.event;

import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyAction;
import cn.note.swing.core.event.key.KeyActionStatus;
import cn.note.swing.core.event.key.RegisterKeyAction;
import cn.note.swing.store.NoteContext;
import cn.note.swing.view.note.tab.NoteTabView;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

/**
 * @description: 扫描@ActionKey实现注册
 * @author: jee
 */
@Slf4j
public class NoteTabActionSupport implements RegisterKeyAction {

    /**
     * 分类主组件 tree
     */
    private NoteTabView tabView;

    public NoteTabActionSupport(NoteContext noteContext) {
        this.tabView = noteContext.getNoteTabView();
    }

    @Override
    public JComponent getComponent() {
        return tabView;
    }


    /**
     * 分类菜单删除
     */
    @KeyAction
    public void closeEvent() {
        registerKeyAction(NoteTabActions.CLOSE_NOW, new ConsumerAction((e) -> {
            log.info("-----------close now---------");
            tabView.closeNow();
        }), KeyActionStatus.WHEN_IN_FOCUSED_WINDOW);

        registerKeyAction(NoteTabActions.CLOSE_OTHER, new ConsumerAction((e) -> {
            tabView.closeOther();
        }), KeyActionStatus.WHEN_IN_FOCUSED_WINDOW);

        registerKeyAction(NoteTabActions.CLOSE_LEFT, new ConsumerAction((e) -> {
            tabView.closeLeft();
        }), KeyActionStatus.WHEN_IN_FOCUSED_WINDOW);

        registerKeyAction(NoteTabActions.CLOSE_RIGHT, new ConsumerAction((e) -> {
            tabView.closeRight();
        }), KeyActionStatus.WHEN_IN_FOCUSED_WINDOW);

        registerKeyAction(NoteTabActions.CLOSE_ALL, new ConsumerAction((e) -> {
            tabView.closeAll();
        }), KeyActionStatus.WHEN_IN_FOCUSED_WINDOW);
    }

}
