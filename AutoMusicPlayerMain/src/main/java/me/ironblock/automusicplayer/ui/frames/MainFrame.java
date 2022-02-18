package me.ironblock.automusicplayer.ui.frames;

import me.ironblock.automusicplayer.ui.annotations.Initializer;
import me.ironblock.automusicplayer.ui.annotations.Listener;
import me.ironblock.automusicplayer.ui.annotations.WindowComponent;
import me.ironblock.automusicplayer.ui.annotations.WindowFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * @author :Iron__Block
 * @Date :2022/2/17 2:17
 */
@WindowFrame(name = "mainFrame",
        title = (MainFrame.PROGRAM_NAME + " " + MainFrame.PROGRAM_VERSION),
        width = MainFrame.FRAME_WIDTH, height = MainFrame.FRAME_HEIGHT,
        initializer = "initFrame",
        autoCenter = true)
public class MainFrame extends JFrame {
    public static final String PROGRAM_NAME = "Game Music Player";
    public static final String PROGRAM_VERSION = "v1.3.0";
    public static final int FRAME_WIDTH = 600;
    public static final int FRAME_HEIGHT = 320;

    @Initializer(name = "initFrame")
    public static void initFrame(JFrame frame){
        frame.setAlwaysOnTop(true);
    }

}
