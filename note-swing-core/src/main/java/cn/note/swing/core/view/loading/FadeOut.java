package cn.note.swing.core.view.loading;

import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;

/**
 * @author jee
 * @version 1.0
 */
class FadeOut extends PropertySetter {
    private JXPanel parent;
    private JXPanel out;
    private JComponent in;

    public FadeOut(JXPanel parent, JXPanel out, JComponent in) {
        super(out, "alpha", 1.0f, 0.3f);
        this.parent = parent;
        this.out = out;
        this.in = in;
    }

    @Override
    public void end() {
        parent.setAlpha(0.3f);
        parent.remove(out);
        parent.add(in);
        parent.revalidate();
    }
}