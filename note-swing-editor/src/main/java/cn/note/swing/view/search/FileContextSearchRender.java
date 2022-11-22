package cn.note.swing.view.search;

import cn.note.swing.core.view.base.BorderBuilder;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import java.awt.*;

/**
 * @author jee
 * @version 1.0
 */
public class FileContextSearchRender extends JPanel implements ListCellRenderer<FileContext> {

    private final JLabel relativePath = new JLabel();
    private final JLabel modifiedDate = new JLabel();
    private final JXLabel matchResult = new JXLabel();

    public FileContextSearchRender() {
        setLayout(new MigLayout("h 30::", "[grow]", "[grow]"));
        relativePath.setForeground(ThemeColor.fontColor);
        modifiedDate.setForeground(ThemeColor.fontColor);
        matchResult.setForeground(ThemeColor.fontColor);
        matchResult.setLineWrap(true);
        relativePath.setIcon(SvgIconFactory.icon(SvgIconFactory.Editor.note, ThemeColor.primaryColor));
        // 文件信息区域
        this.add(matchResult, "grow,wrap");
        this.add(relativePath, "gapleft 5");
        this.add(modifiedDate, "align right,gapright 5");
        this.setBorder(BorderBuilder.bottomBorder(1, ThemeColor.grayColor));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends FileContext> list, FileContext value, int index, boolean isSelected, boolean cellHasFocus) {
        relativePath.setText(value.getRelativePath());
        modifiedDate.setText(value.getModifiedDate());
        matchResult.setText(value.getHighMatchText());
        if (isSelected) {
            setBackground(ThemeColor.hoverBgColor);
        } else {
            setBackground(ThemeColor.noColor);
        }
        // 内容可能影响panel大小
        this.revalidate();
        return this;
    }
}
