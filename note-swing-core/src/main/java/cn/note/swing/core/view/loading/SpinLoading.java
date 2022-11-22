package cn.note.swing.core.view.loading;

import lombok.extern.slf4j.Slf4j;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.triggers.TimingTrigger;
import org.jdesktop.animation.timing.triggers.TimingTriggerEvent;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;


/**
 * 带动画加载的loading
 *
 * @author jee
 */
@Slf4j
public class SpinLoading<T extends Component> extends JXPanel {

    static final Insets margin = new Insets(8, 10, 8, 8);

    private T component;

    public SpinLoading(T component) {
        // 默认持续0.4s
        this(component, 400);
    }

    public SpinLoading(T component, int duration) {
        this.component = component;
        setLayout(new BorderLayout());
        LoadAnimationPanel loadAnimationPanel = new LoadAnimationPanel();

        add(loadAnimationPanel);
        loadAnimationPanel.setAnimating(true);

        Animator fadeOutAnimator = null;
        Animator fadeInAnimator = null;
        try {
            loadAnimationPanel.setAnimating(false);
            fadeOutAnimator = new Animator(duration, new FadeOut(SpinLoading.this, loadAnimationPanel, createItemComponent()));
            fadeOutAnimator.setAcceleration(.2f);
            fadeOutAnimator.setDeceleration(.3f);
            fadeInAnimator = new Animator(duration, new PropertySetter(SpinLoading.this, "alpha", 0.3f, 1.0f));
            TimingTrigger.addTrigger(fadeOutAnimator, fadeInAnimator, TimingTriggerEvent.STOP);
            fadeOutAnimator.start();
        } catch (Exception e) {
            if (fadeOutAnimator != null) {
                fadeOutAnimator.stop();
            }
            if (fadeInAnimator != null) {
                fadeInAnimator.stop();
            }
            loadAnimationPanel.setAnimating(false);
            log.info("渲染loading异常:{}", e);
        }
    }

    public T getComponent() {
        return component;
    }

    private JPanel createItemComponent() {
        JPanel loadingPanel = new JPanel();
        loadingPanel.setLayout(new BorderLayout());
        loadingPanel.add(component);
        return loadingPanel;
    }

}
