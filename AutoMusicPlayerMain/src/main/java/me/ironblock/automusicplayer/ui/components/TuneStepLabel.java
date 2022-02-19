package me.ironblock.automusicplayer.ui.components;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

/**
 * @author :Iron__Block
 * @Date :2022/2/20 1:06
 */
public class TuneStepLabel extends WebLabel {
    private final WebButton button = new WebButton("Hello");
    public TuneStepLabel() {
        button.setBounds(2,2,100,100);
        this.add(button);
    }
}
