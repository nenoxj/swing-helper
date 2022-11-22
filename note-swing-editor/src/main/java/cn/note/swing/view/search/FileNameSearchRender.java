package cn.note.swing.view.search;

import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * 文件名称搜索
 *
 * @author jee
 * @version 1.0
 */
public class FileNameSearchRender extends JPanel implements ListCellRenderer<FileContext> {

    private final JLabel matchPath = new JLabel();
    private final JLabel modifiedDate = new JLabel();

    public FileNameSearchRender() {
        setLayout(new MigLayout("h 25!", "[grow]", "[grow]"));
        matchPath.setIcon(SvgIconFactory.icon(SvgIconFactory.Editor.note, ThemeColor.primaryColor));
        this.add(matchPath, "west,gapleft 5");
        this.add(modifiedDate, "east,gapright 5");
        matchPath.setForeground(ThemeColor.fontColor);
        modifiedDate.setForeground(ThemeColor.fontColor);

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends FileContext> list, FileContext value, int index, boolean isSelected, boolean cellHasFocus) {
        matchPath.setText(value.getMatchPath());
        modifiedDate.setText(value.getModifiedDate());
        if (isSelected) {
            setBackground(ThemeColor.hoverBgColor);
        } else {
            setBackground(ThemeColor.noColor);
        }
        return this;
    }
}
