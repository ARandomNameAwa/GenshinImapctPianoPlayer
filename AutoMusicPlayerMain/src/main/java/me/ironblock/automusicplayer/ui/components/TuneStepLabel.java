package me.ironblock.automusicplayer.ui.components;

import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.text.WebTextField;
import me.ironblock.automusicplayer.music.TuneStep;
import me.ironblock.automusicplayer.ui.frames.MainFrame;
import me.ironblock.automusicplayer.ui.loader.UILoader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author :Iron__Block
 * @Date :2022/2/20 1:06
 */
public class TuneStepLabel extends WebLabel {


    private final WebToggleButton button = new WebToggleButton("Disable");
    private final WebLabel description = new WebLabel();
    private final WebLabel octaveLabel = new WebLabel("Octave:");
    private final WebLabel trackIndexLabel = new WebLabel();
    private final WebTextField octaveTF = new WebTextField("0");
    public final WebButton add = new WebButton();
    public final WebButton sub = new WebButton();

    public TuneStepLabel(int trackIndex,String trackDescription) {
        description.setText(trackDescription);
        trackIndexLabel.setText("Track "+trackIndex+":");
        this.setSize(480,40);
        button.setBounds(370,0,60,30);
        trackIndexLabel.setBounds(0,0,370,30);
        octaveLabel.setBounds(200,0,50,30);
        description.setBounds(0,30,480,10);
        octaveTF.setBounds(250,0,50,30);
        add.setBounds(295,0,20,17);
        sub.setBounds(295,13,20,17);
        UILoader.trySetIcon(add,"/images/add1.png");
        UILoader.trySetIcon(sub,"/images/sub1.png");
        octaveTF.setEditable(false);
        add.addActionListener(e -> {
            try {
                int currentOctave = Integer.parseInt(octaveTF.getText());
                octaveTF.setText(String.valueOf(currentOctave+1));
            } catch (NumberFormatException ex) {
                MainFrame.LOGGER.warn("Number Format Exception:",ex);
            }
        });
        sub.addActionListener(e -> {
            try {
                int currentOctave = Integer.parseInt(octaveTF.getText());
                octaveTF.setText(String.valueOf(currentOctave-1));
            } catch (NumberFormatException ex) {
                MainFrame.LOGGER.warn("Number Format Exception:",ex);
            }
        });
        button.addActionListener(e->{
            if ("Disable".equals(button.getText())){
                button.setText("Enable");
            }else{
                button.setText("Disable");
            }
        });
        button.setSelected(true);

        this.add(octaveLabel);
        this.add(button);
        this.add(description);
        this.add(trackIndexLabel);
        this.add(octaveTF);
        this.add(add);
        this.add(sub);
    }

    public int getTuneStep(){
        return Integer.parseInt(octaveTF.getText());
    }



}
