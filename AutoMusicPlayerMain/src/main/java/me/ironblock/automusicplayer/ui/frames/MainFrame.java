package me.ironblock.automusicplayer.ui.frames;

import com.alee.extended.list.WebFileList;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.slider.WebSlider;
import com.alee.laf.text.WebTextField;
import com.sun.jna.WString;
import me.ironblock.automusicplayer.nativeInvoker.WindowsMessage;
import me.ironblock.automusicplayer.ui.annotations.Initializer;
import me.ironblock.automusicplayer.ui.annotations.WindowComponent;
import me.ironblock.automusicplayer.ui.annotations.WindowFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

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
    public static final Logger LOGGER = LogManager.getLogger(MainFrame.class);
    public static final String PROGRAM_NAME = "Game Music Player";
    public static final String PROGRAM_VERSION = "v1.3.0";
    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 800;

    @WindowComponent(name = "labelFileList",x = 50,y = 0,width = 140,height = 45,initPara = "PlayLists")
    public WebLabel label1;
    @WindowComponent(name = "labelPlaySettings",x = 400, y = 0,width = 140,height = 45,initPara = "Play Settings")
    public WebLabel label2;
    @WindowComponent(name = "fileList",x = 50,y = 50,width = 330,height = 560)
    public WebFileList list1;
    @WindowComponent(name = "slider",x = 50,y = 650,width = 880,height = 35,initializer = "initSlider")
    public WebSlider slider;
    @WindowComponent(name = "addFile",x= 50,y = 610,width = 30,height = 30,background = "/images/addFile.png")
    public WebButton button1;
    @WindowComponent(name = "deleteFile",x = 80,y = 610,width = 30,height = 30,background = "/images/deleteFile.png")
    public WebButton button2;
    @WindowComponent(name = "labelSpeed",x = 440,y = 50,width = 120,height = 40,initPara = "Speed:")
    public WebLabel label5;
    @WindowComponent(name = "speed",x = 590,y = 50,width = 120,height = 40,initPara = "1.0")
    public WebTextField textField1;
    @WindowComponent(name = "addSpeed",x = 705,y = 50,width = 20,height = 23)
    public WebButton button6;
    @WindowComponent(name = "subSpeed",x = 705,y = 67,width = 20,height = 23)
    public WebButton button7;
    @WindowComponent(name = "labelFileType",x = 440,y = 100,width = 120,height = 40,initPara = "File Type:")
    public WebLabel label6;
    @WindowComponent(name = "ComboBoxFileType",x = 590,y = 100,width = 240,height = 40)
    public WebComboBox comboBox1;
    @WindowComponent(name = "playMode",x = 440,y = 150,width = 120,height = 40,initPara = "PlayMode:")
    public WebLabel label3;
    @WindowComponent(name = "postMessage",x = 730,y = 150,width = 120,height = 40,initPara = "PostMessage",initializer = "initPostMessage")
    public WebRadioButton button4;
    @WindowComponent(name = "awt",x = 590,y = 150,width = 120,height = 40,initPara = "AWT Robot",initializer = "initRadioButton")
    public WebRadioButton button3;
    @WindowComponent(name = "labelSelectWindow",x = 440,y = 200,width = 120,height = 40,initPara = "Select window:")
    public WebLabel label4;
    @WindowComponent(name = "ComboBoxSelectWindow",x = 590,y = 200,width = 240,height = 40,initializer = "initComboBoxWindowTitle")
    public WebComboBox comboBox2;
    @WindowComponent(name = "labelTuneSettings",x = 400, y = 250,width = 140,height = 45,initPara = "Tune Settings")
    public WebLabel label7;
    @WindowComponent(name = "tuneList",x = 440,y = 300,width = 480,height = 310)
    public WebList tuneList;
    @WindowComponent(name = "lastSong",x = 400,y = 690,width = 50,height = 50,background = "/images/lastSong.png")
    public WebButton button5;
    @WindowComponent(name = "pause",x = 450,y = 690,width = 50,height = 50,background = "/images/resume.png")
    public WebButton button8;
    @WindowComponent(name = "stop",x = 500,y = 690,width = 50,height = 50,background = "/images/stop.png")
    public WebButton button9;
    @WindowComponent(name = "nextSong",x = 550,y = 690,width = 50,height = 50,background = "/images/nextSong.png")
    public WebButton button10;




    @Initializer(name = "initFrame")
    public static void initFrame(JFrame frame){
        frame.setResizable(false);
    }



    private final static ButtonGroup group = new ButtonGroup();
    @Initializer(name = "initRadioButton")
    public static void initRadioButton(JRadioButton button){
        group.add(button);
        button.setSelected(true);
    }

    private static boolean dllLoadSuccessful = false;
    @Initializer(name = "initPostMessage")
    public static void initPostMessage(JRadioButton button){
        try {
            WindowsMessage.INSTANCE.getClass();
            dllLoadSuccessful = true;
        } catch (Throwable e) {
            LOGGER.error("Failed to load postMessage dll,so postMessage player can't be used.",e);
        }
        if (dllLoadSuccessful){
            group.add(button);
        }else{
            button.setEnabled(false);
        }
    }
    @Initializer(name = "initComboBoxWindowTitle")
    public static void initComboBoxWindowTitle(JComboBox<String> comboBox){
        if (dllLoadSuccessful){
            WString string = WindowsMessage.INSTANCE.listWindows();
            String[] tmp = string.toString().split(";");
            comboBox.removeAllItems();
            for (String s : tmp) {
                comboBox.addItem(s);
            }
        }else{
            comboBox.setEnabled(false);
        }

    }
    @Initializer(name = "initSlider")
    public static void initSlider(JSlider slider){
        slider.setValue(0);
    }

    @Initializer(name = "makeInvisible")
    public static void makeInvisible(Component component){
        component.setVisible(false);
    }

}
