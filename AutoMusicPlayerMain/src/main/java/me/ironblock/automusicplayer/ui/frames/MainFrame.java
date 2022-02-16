package me.ironblock.automusicplayer.ui.frames;

import me.ironblock.automusicplayer.ui.annotations.WindowFrame;
import sun.applet.Main;

import javax.swing.*;

/**
 * @author :Iron__Block
 * @Date :2022/2/17 2:17
 */
@WindowFrame(name = "mainFrame", title = (MainFrame.PROGRAM_NAME + " " + MainFrame.PROGRAM_VERSION), width = MainFrame.FRAME_WIDTH, height = MainFrame.FRAME_HEIGHT, autoCenter = true)
public class MainFrame extends JFrame {
    public static final String PROGRAM_NAME = "Game Music Player";
    public static final String PROGRAM_VERSION = "v1.3.0";
    public static final int FRAME_WIDTH = 600;
    public static final int FRAME_HEIGHT = 320;


}
