package me.ironblock.automusicplayer.ui.frames;

import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.list.WebListModel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.slider.WebSlider;
import com.alee.laf.text.WebTextField;
import com.sun.jna.WString;
import me.ironblock.automusicplayer.config.PlayListConfig;
import me.ironblock.automusicplayer.config.TuneStepConfig;
import me.ironblock.automusicplayer.keymap.KeyMap;
import me.ironblock.automusicplayer.keymap.KeyMapLoader;
import me.ironblock.automusicplayer.music.TuneStep;
import me.ironblock.automusicplayer.music.parser.AbstractMusicParser;
import me.ironblock.automusicplayer.nativeInvoker.WindowsMessage;
import me.ironblock.automusicplayer.note.NoteInfo;
import me.ironblock.automusicplayer.playcontroller.MusicParserRegistry;
import me.ironblock.automusicplayer.playcontroller.PlayController;
import me.ironblock.automusicplayer.ui.annotations.*;
import me.ironblock.automusicplayer.ui.components.TuneStepLabel;
import me.ironblock.automusicplayer.ui.loader.UIContext;
import me.ironblock.automusicplayer.ui.loader.UILoader;
import me.ironblock.automusicplayer.utils.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

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

    public static final PlayListConfig PLAY_LIST_CONFIG = new PlayListConfig();
    public static final TuneStepConfig TUNE_STEP_CONFIG  = new TuneStepConfig();

    @WindowComponent(name = "labelFileList", x = 50, y = 0, width = 140, height = 45, initPara = "PlayLists")
    public WebLabel label1;
    @WindowComponent(name = "labelPlaySettings", x = 400, y = 0, width = 140, height = 45, initPara = "Play Settings")
    public WebLabel label2;
    @WindowComponent(name = "fileList", x = 50, y = 50, width = 330, height = 560, initializer = "addScroll", listeners = {"jListMouseListener"})
    public WebList list1;
    @WindowComponent(name = "slider", x = 50, y = 650, width = 880, height = 35, initializer = "initSlider", listeners = "sliderChanged")
    public WebSlider slider;
    @WindowComponent(name = "addFile", x = 50, y = 610, width = 30, height = 30, background = "/images/addFile.png", listeners = {"addFileActionListener"})
    public WebButton button1;
    @WindowComponent(name = "deleteFile", x = 80, y = 610, width = 30, height = 30, background = "/images/deleteFile.png", listeners = {"deleteFileListener"})
    public WebButton button2;
    @WindowComponent(name = "labelSpeed", x = 440, y = 50, width = 120, height = 40, initPara = "Speed:")
    public WebLabel label5;
    @WindowComponent(name = "speed", x = 590, y = 50, width = 120, height = 40, initPara = "1.0")
    public WebTextField textField1;
    @WindowComponent(name = "addSpeed", x = 705, y = 50, width = 20, height = 23, background = "/images/add1.png", listeners = {"addSpeedListener"})
    public WebButton button6;
    @WindowComponent(name = "subSpeed", x = 705, y = 67, width = 20, height = 23, background = "/images/sub1.png", listeners = {"" + "subSpeedListener"})
    public WebButton button7;
    @WindowComponent(name = "labelFileType", x = 440, y = 100, width = 120, height = 40, initPara = "File Type:")
    public WebLabel label6;
    @WindowComponent(name = "ComboBoxFileType", x = 590, y = 100, width = 240, height = 40)
    public WebComboBox comboBox1;
    @WindowComponent(name = "playMode", x = 440, y = 150, width = 120, height = 40, initPara = "PlayMode:")
    public WebLabel label3;
    @WindowComponent(name = "postMessage", x = 730, y = 150, width = 120, height = 40, initPara = "PostMessage", initializer = "initPostMessage", listeners = "selectPostMessage")
    public WebRadioButton button4;
    @WindowComponent(name = "awt", x = 590, y = 150, width = 120, height = 40, initPara = "AWT Robot", initializer = "initRadioButton", listeners = "selectAwt")
    public WebRadioButton button3;
    @WindowComponent(name = "labelKeyMap", x = 440, y = 200, width = 120, height = 40, initPara = "Key Map")
    public WebLabel label11;
    @WindowComponent(name = "ComboBoxKeyMap", x = 590, y = 200, width = 240, height = 40, initializer = "initKeyMap", listeners = "keyMapListener")
    public WebComboBox comboBox3;
    @WindowComponent(name = "labelSelectWindow", x = 440, y = 250, width = 120, height = 40, initPara = "Select window:")
    public WebLabel label4;
    @WindowComponent(name = "ComboBoxSelectWindow", x = 590, y = 250, width = 240, height = 40, initializer = "initComboBoxWindowTitle", listeners = {"WindowTitleRefresher", "windowTitle"})
    public WebComboBox comboBox2;
    @WindowComponent(name = "labelTuneSettings", x = 400, y = 300, width = 140, height = 45, initPara = "Tune Settings")
    public WebLabel label7;
    @WindowComponent(name = "tuneList", x = 440, y = 390, width = 480, height = 180, initializer = "addScroll")
    public WebList tuneList;
    @WindowComponent(name = "lastSong", x = 400, y = 690, width = 50, height = 50, background = "/images/lastSong.png",listeners = {"lastMusic"})
    public WebButton button5;
    @WindowComponent(name = "pause", x = 450, y = 690, width = 50, height = 50, background = "/images/resume.png", listeners = "startPlay")
    public WebButton button8;
    @WindowComponent(name = "stop", x = 500, y = 690, width = 50, height = 50, background = "/images/stop.png", listeners = "stopPlay")
    public WebButton button9;
    @WindowComponent(name = "nextSong", x = 550, y = 690, width = 50, height = 50, background = "/images/nextSong.png",listeners = "nextMusic")
    public WebButton button10;
    @WindowComponent(name = "currentMusic", x = 430, y = 620, width = 200, height = 50, initPara = "Please select a music to play.")
    public WebLabel label8;
    @WindowComponent(name = "currentTime", x = 50, y = 660, width = 200, height = 50, initPara = "00:00")
    public WebLabel label9;
    @WindowComponent(name = "totalTime", x = 900, y = 660, width = 200, height = 50, initPara = "00:00")
    public WebLabel label10;
    @WindowComponent(name = "EveryTrackOctaveSame", x = 755, y = 340, width = 150, height = 30, initPara = "EveryTrackOctaveSame", listeners = {"OctaveSame"})
    public WebToggleButton button11;
    @WindowComponent(name = "OctaveLabel", x = 590, y = 330, width = 50, height = 50, initPara = "Octave:")
    public WebLabel label12;
    @WindowComponent(name = "octave", x = 640, y = 335, width = 50, height = 40, initPara = "0")
    public WebTextField textField2;
    @WindowComponent(name = "addOctave", x = 687, y = 335, width = 23, height = 23, background = "/images/add1.png", listeners = {"addOctave"})
    public WebButton button13;
    @WindowComponent(name = "subOctave", x = 687, y = 352, width = 23, height = 23, background = "/images/sub1.png", listeners = {"subOctave"})
    public WebButton button14;
    @WindowComponent(name = "tuneLabel", x = 440, y = 330, width = 50, height = 50, initPara = "Tune:")
    public WebLabel label13;
    @WindowComponent(name = "tune", x = 490, y = 335, width = 50, height = 40, initPara = "0")
    public WebTextField textField3;
    @WindowComponent(name = "addTune", x = 537, y = 335, width = 23, height = 23, background = "/images/add1.png", listeners = {"addTune"})
    public WebButton button15;
    @WindowComponent(name = "subTune", x = 537, y = 352, width = 23, height = 23, background = "/images/sub1.png", listeners = {"subTune"})
    public WebButton button16;
    @WindowComponent(name = "autoTune", x = 775, y = 580, width = 150, height = 30, initPara = "AutoTune", listeners = {"autoTune"})
    public WebButton button12;
    private static String selectedMusic = "";
    private static final PlayController playController = new PlayController();

    @Initializer(name = "initFrame")
    public static void initFrame(JFrame frame) {
        frame.setResizable(false);
        drag(frame);
        try {
            PLAY_LIST_CONFIG.readFromConfig();
        } catch (IOException e) {
            LOGGER.error("Failed to read from config.", e);
        }
    }

    private final static ButtonGroup group = new ButtonGroup();

    @Initializer(name = "initRadioButton")
    public static void initRadioButton(JRadioButton button) {
        group.add(button);
        button.setSelected(true);
    }

    private static boolean dllLoadSuccessful = false;


    @Initializer(name = "initPostMessage")
    public static void initPostMessage(JRadioButton button) {
        try {
            WindowsMessage.INSTANCE.getClass();
            dllLoadSuccessful = true;
        } catch (Throwable e) {
            LOGGER.error("Failed to load postMessage dll,so postMessage player can't be used.", e);
        }
        if (dllLoadSuccessful) {
            group.add(button);
        } else {
            button.setEnabled(false);
        }
    }

    @Initializer(name = "initComboBoxWindowTitle")
    public static void initComboBoxWindowTitle(JComboBox<String> comboBox) {
        if (dllLoadSuccessful) {
            WString string = WindowsMessage.INSTANCE.listWindows();
            String[] tmp = string.toString().split(";");
            comboBox.removeAllItems();
            for (String s : tmp) {
                comboBox.addItem(s);
            }
            playController.setPostMessageWindow(comboBox.getSelectedItem().toString());
        } else {
            comboBox.setEnabled(false);
        }

    }

    @Initializer(name = "initSlider")
    public static void initSlider(JSlider slider) {
        slider.setValue(0);
    }

    @Initializer(name = "makeInvisible")
    public static void makeInvisible(Component component) {
        component.setVisible(false);
    }

    @Initializer(name = "addScroll")
    public static boolean addScroll(JList<String> list, Frame frame) {
        JScrollPane s = new JScrollPane(list);
        s.setBounds(list.getBounds());
        s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(s);
        return false;
    }

    @Initializer(name = "initKeyMap")
    public static void initKeyMap(JComboBox<String> comboBox) {
        for (String s : KeyMapLoader.getInstance().getAllLoadedMapName()) {
            comboBox.addItem(s);
        }
        playController.setActiveKeyMap(KeyMapLoader.getInstance().getLoadedKeyMap(comboBox.getSelectedItem().toString()));
    }

    @Listener(name = "addFileActionListener", parent = ActionListener.class)
    public static class addFileActionListener implements ActionListener {
        private final JFileChooser fileChooser = new JFileChooser(new File("."));

        @Override
        public void actionPerformed(ActionEvent e) {
            UIContext ui = UILoader.UI;

            int status = fileChooser.showOpenDialog(ui.getFrameFromName("mainFrame"));
            if (status == JFileChooser.FILES_ONLY) {
                addToFileList(fileChooser.getSelectedFile().getName(), fileChooser.getSelectedFile().getAbsolutePath());

            }
        }
    }

    @Listener(name = "deleteFileListener", parent = ActionListener.class)
    public static class deleteFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            UIContext ui = UILoader.UI;
            JList<String> list = ((JList<String>) ui.getComponentFromName("fileList"));
            try {
                for (String s : list.getSelectedValuesList()) {
                    removeFromList(list, s);
                    PLAY_LIST_CONFIG.getFileNameAndPathMap().remove(s);
                }
                try {
                    PLAY_LIST_CONFIG.writeToConfig();
                } catch (IOException e1) {
                    LOGGER.warn("Failed to write to config.", e1);
                }
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                LOGGER.warn("Failed to add element for" + e.getSource());
                ex.printStackTrace();
            }
        }
    }

    @Listener(name = "jListMouseListener", parent = MouseListener.class)
    public static class jListMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            updateUI();
            if (e.getClickCount() == 2) {
                UIContext ui = UILoader.UI;
                JList<String> list = ((JList<String>) ui.getComponentFromName("fileList"));
                if (list.getSelectedValue() != null) {
                    try {
                        TUNE_STEP_CONFIG.writeTuneStepToConfig(selectedMusic,getCurrentTune());
                    } catch (IOException ex) {
                        LOGGER.error("Failed to save tune step.",e);
                    }
                    selectedMusic = list.getSelectedValue();
                    JLabel label = (JLabel) ui.getComponentFromName("currentMusic");
                    label.setText(selectedMusic);
                    onSongSelected(PLAY_LIST_CONFIG.getFileNameAndPathMap().get(selectedMusic));
                    loadSongWithParser(PLAY_LIST_CONFIG.getFileNameAndPathMap().get(selectedMusic), getSelectedParser());
                    JButton button = (JButton) ui.getComponentFromName("pause");
                    UILoader.trySetIcon(button, "/images/pause.png");
                    playController.stopPlay();
                    setSpeed();
                    updateTuneInfo();
                    playController.startPlay();

                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    @Listener(name = "lastMusic", parent = ActionListener.class)
    public static class lastMusic implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            UIContext ui = UILoader.UI;
            try {
                TUNE_STEP_CONFIG.writeTuneStepToConfig(selectedMusic,getCurrentTune());
            } catch (IOException ex) {
                LOGGER.error("Failed to save tune step.",e);
            }
            List<String> list1 = new ArrayList<>(PLAY_LIST_CONFIG.getFileNameAndPathMap().keySet());
            list1 = list1.stream().sorted(Comparator.comparingInt(s -> s.charAt(0))).collect(Collectors.toList());
            int index = list1.indexOf(selectedMusic);
            index--;
            if (index <= 0){
                index = list1.size()-1;
            }
            selectedMusic = list1.get(index);

                JLabel label = (JLabel) ui.getComponentFromName("currentMusic");
                label.setText(selectedMusic);
                onSongSelected(PLAY_LIST_CONFIG.getFileNameAndPathMap().get(selectedMusic));
                updateTuneInfo();
                loadSongWithParser(PLAY_LIST_CONFIG.getFileNameAndPathMap().get(selectedMusic), getSelectedParser());
                JButton button = (JButton) ui.getComponentFromName("pause");
                UILoader.trySetIcon(button, "/images/pause.png");
                playController.stopPlay();
                setSpeed();
                playController.startPlay();
        }
    }

    @Listener(name = "nextMusic", parent = ActionListener.class)
    public static class NextMusic implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            UIContext ui = UILoader.UI;

            List<String> list1 = new ArrayList<>(PLAY_LIST_CONFIG.getFileNameAndPathMap().keySet());
            list1 = list1.stream().sorted(Comparator.comparingInt(s -> s.charAt(0))).collect(Collectors.toList());
            try {
                TUNE_STEP_CONFIG.writeTuneStepToConfig(selectedMusic,getCurrentTune());
            } catch (IOException ex) {
                LOGGER.error("Failed to save tune step",ex);
            }
            int index = list1.indexOf(selectedMusic);
            index++;
            if (index >= list1.size()){
                index = 0;
            }
            selectedMusic = list1.get(index);

            JLabel label = (JLabel) ui.getComponentFromName("currentMusic");
            label.setText(selectedMusic);
            onSongSelected(PLAY_LIST_CONFIG.getFileNameAndPathMap().get(selectedMusic));
            updateTuneInfo();
            loadSongWithParser(PLAY_LIST_CONFIG.getFileNameAndPathMap().get(selectedMusic), getSelectedParser());
            JButton button = (JButton) ui.getComponentFromName("pause");
            UILoader.trySetIcon(button, "/images/pause.png");
            playController.stopPlay();
            setSpeed();
            playController.startPlay();
        }
    }

    @Listener(name = "addSpeedListener", parent = ActionListener.class)
    public static class addSpeedListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateUI();
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("speed");
            try {
                int i = (int) (Double.parseDouble(textField.getText()) * 10);
                textField.setText(String.valueOf((i + 1) / 10d));
                setSpeed();
            } catch (NumberFormatException ex) {
                LOGGER.warn(ex);
            }
        }
    }

    @Listener(name = "subSpeedListener", parent = ActionListener.class)
    public static class subSpeedListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateUI();
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("speed");
            try {
                int i = (int) (Double.parseDouble(textField.getText()) * 10);
                if (i < 0) {
                    i = 1;
                }
                if (i - 1 > 0) {
                    i -= 1;
                }
                textField.setText(String.valueOf(i / 10d));
                setSpeed();
            } catch (NumberFormatException ex) {
                LOGGER.warn(ex);
            }
        }
    }

    @Listener(name = "autoTune", parent = ActionListener.class)
    public static class AutoTuneListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            autoTune();
            updateTuneInfo();
        }
    }

    @Listener(name = "addOctave", parent = ActionListener.class)
    public static class AddOctave implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateUI();
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("octave");
            try {
                int i = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf(i + 1));
                updateTuneInfo();
            } catch (NumberFormatException ex) {
                LOGGER.error(ex);
            }

        }
    }

    @Listener(name = "subOctave", parent = ActionListener.class)
    public static class SubOctave implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateUI();
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("octave");
            try {
                int i = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf(i - 1));
                updateTuneInfo();
            } catch (NumberFormatException ex) {
                LOGGER.error(ex);
            }

        }
    }

    @Listener(name = "addTune", parent = ActionListener.class)
    public static class AddTune implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateUI();
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("tune");
            try {
                int i = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf(i + 1));
                updateTuneInfo();
            } catch (NumberFormatException ex) {
                LOGGER.error(ex);
            }

        }
    }

    @Listener(name = "subTune", parent = ActionListener.class)
    public static class SubTune implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateUI();
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("tune");
            try {
                int i = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf(i - 1));
                updateTuneInfo();
            } catch (NumberFormatException ex) {
                LOGGER.error(ex);
            }

        }
    }

    @Listener(name = "keyMapListener", parent = ItemListener.class)
    public static class KeyMapSelected implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            updateUI();
            UIContext ui = UILoader.UI;
            if (ui != null) {
                LOGGER.info("Setting keyMap to " + getSelectedKeyMap());
                playController.setActiveKeyMap(getSelectedKeyMap());
            }
        }
    }

    @Listener(name = "selectAwt", parent = ActionListener.class)
    public static class SelectAwt implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (((WebRadioButton) e.getSource()).isSelected()) {
                playController.setPostMessage(false);
                LOGGER.info("Setting post message window to false");
                setSpeed();
            }
        }
    }

    @Listener(name = "selectPostMessage", parent = ActionListener.class)
    public static class SelectPostMessage implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (((WebRadioButton) e.getSource()).isSelected()) {
                playController.setPostMessage(true);
                LOGGER.info("Setting post message window to true");
                setSpeed();
            }
        }
    }

    @Listener(name = "windowTitle", parent = ItemListener.class)
    public static class WindowTitleChanged implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED)
                playController.setPostMessageWindow(((JComboBox<String>) e.getSource()).getSelectedItem().toString());
        }
    }

    @Listener(name = "startPlay", parent = ActionListener.class)
    public static class StartPlay implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateUI();
            if (playController.isPlaying()) {
                if (playController.isPaused()) {
                    UILoader.trySetIcon((Component) e.getSource(), "/images/pause.png");
                } else {
                    UILoader.trySetIcon((Component) e.getSource(), "/images/resume.png");
                }
                playController.switchPause();
            } else {
                UILoader.trySetIcon((Component) e.getSource(), "/images/pause.png");
                playController.startPlay();
            }

        }
    }

    @Listener(name = "stopPlay", parent = ActionListener.class)
    public static class StopPlay implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateUI();
            playController.stopPlay();
            UIContext ui = UILoader.UI;
            UILoader.trySetIcon(ui.getComponentFromName("pause"), "/images/resume.png");
        }
    }

    @Listener(name = "WindowTitleRefresher", parent = PopupMenuListener.class)
    public static class WindowTitleRefresher implements PopupMenuListener {
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            JComboBox<String> comboBox = ((JComboBox<String>) e.getSource());
            Object last = comboBox.getSelectedItem();
            WString string = WindowsMessage.INSTANCE.listWindows();
            String[] tmp = string.toString().split(";");
            comboBox.removeAllItems();
            for (String s : tmp) {
                comboBox.addItem(s);
            }
            comboBox.setSelectedItem(last);
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
        }
    }

    @Listener(name = "sliderChanged", parent = MouseListener.class)
    public static class SliderChanged implements MouseListener {

        boolean pressing = false;

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            updateUI();
            if (!pressing) {
                if (!playController.isPaused() && playController.isPlaying()) {
                    playController.switchPause();
                }
                pressing = true;
            } else {
                if (playController.isPlaying()) {
                    UIContext ui = UILoader.UI;
                    WebLabel label_currentPlayTime = (WebLabel) ui.getComponentFromName("currentTime");
                    WebLabel label_totalPlayTime = (WebLabel) ui.getComponentFromName("totalTime");
                    double progress = (double) ((JSlider) e.getSource()).getValue() / 100;
                    label_currentPlayTime.setText(TimeUtils.getMMSSFromS((int) (TimeUtils.getSFromMMSS(label_totalPlayTime.getText()) * progress)));
                }
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            updateUI();
            if (playController.isPaused() && playController.isPlaying()) {
                playController.switchPause();
                int tickToJump = (int) (((double) ((JSlider) e.getSource()).getValue()) / 100 * playController.getTotalTick());
                playController.jumpToTick(tickToJump);
                pressing = false;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    @Listener(name = "OctaveSame", parent = ActionListener.class)
    public static class SameOctave implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateUI();
        }
    }

    private static void addToJList(JList<String> list, Object content) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ListModel<String> fileList = list.getModel();
        Method method = fileList.getClass().getMethod("add", Object.class);
        method.invoke(fileList, content);
        list.setModel(fileList);
    }

    private static void removeFromList(JList<String> list, Object content) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ListModel<String> fileList = list.getModel();
        Method method = fileList.getClass().getMethod("remove", Object.class);
        method.invoke(fileList, content);
        list.setModel(fileList);

    }

    private static void removeAllFromList(JList<String> list) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        WebListModel<String> fileList = (WebListModel) list.getModel();
        fileList.removeAll();
        list.setModel(fileList);
    }

    /**
     * register drag event
     */
    private static void drag(Frame frame) {

        new DropTarget(frame, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dropTargetDropEvent) {
                try {
                    if (dropTargetDropEvent.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        java.util.List<File> list = (List<File>) (dropTargetDropEvent.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        long start = System.currentTimeMillis();
                        if (list.size() > 0) {
                            String[] names = list.stream().map(File::getName).toArray(String[]::new);
                            String[] paths = list.stream().map(File::getAbsolutePath).toArray(String[]::new);
                            addToFileList(names, paths);
                        }
                        long end = System.currentTimeMillis();
                        LOGGER.info("Import files took " + (end - start) + " ms.");
                    } else {
                        dropTargetDropEvent.rejectDrop();
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to create drag event :", e);
                }
            }


        });
    }

    private static void addToFileList(String[] names, String[] paths) {
        for (int i = 0; i < names.length; i++) {
            PLAY_LIST_CONFIG.getFileNameAndPathMap().put(names[i], paths[i]);
        }
        refreshPlayList();
        try {
            PLAY_LIST_CONFIG.writeToConfig();
        } catch (IOException e) {
            LOGGER.warn("Failed to write to config.", e);
        }
    }

    private static void addToFileList(String name, String path) {
        PLAY_LIST_CONFIG.getFileNameAndPathMap().put(name, path);
        refreshPlayList();
        try {
            PLAY_LIST_CONFIG.writeToConfig();
        } catch (IOException e) {
            LOGGER.warn("Failed to write to config.", e);
        }
    }

    private static final Map<String, AbstractMusicParser> parserNameMap = new HashMap<>();

    private static void onSongSelected(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            String[] tmp = file.getName().split("\\.");
            Set<AbstractMusicParser> parsers = MusicParserRegistry.getSuffixParsers(tmp[tmp.length - 1]);
            UIContext ui = UILoader.UI;
            JComboBox<String> parsersCombo = (JComboBox<String>) ui.getComponentFromName("ComboBoxFileType");
            if (parsers != null && !parsers.isEmpty()) {
                String prevSelectItem = (String) parsersCombo.getSelectedItem();
                parsersCombo.removeAllItems();
                parserNameMap.clear();

                for (AbstractMusicParser parser : parsers) {
                    parserNameMap.put(parser.getMusicFileTypeName(), parser);
                    parsersCombo.addItem(parser.getMusicFileTypeName());
                }
                if (prevSelectItem != null && !prevSelectItem.isEmpty()) {
                    if (parserNameMap.containsKey(prevSelectItem)) {
                        parsersCombo.setSelectedItem(prevSelectItem);
                    }
                }
            }
        }
    }

    private static final Map<Integer, TuneStepLabel> tunePanels = new HashMap<>();

    private static void loadSongWithParser(String filePath, AbstractMusicParser musicParser) {
        File file = new File(filePath);
        try {
            FileInputStream stream = new FileInputStream(file);
            LOGGER.info("Loading song:" + file.getName() + ",path:" + file.getAbsolutePath());
            playController.loadMusicWithParser(stream, musicParser, file.getName());
            setSpeed();
            stream.close();
        } catch (IOException e) {
            LOGGER.error("Failed to load song :", e);
            return;
        }
        resetLoadTuneUI();
        loadTuneUI();
        updateTuneInfo();
        try {
            TuneStep tuneStep = TUNE_STEP_CONFIG.getTuneStepFromConfig(file.getName());
            if (tuneStep!=null){
                setToTuneStep(tuneStep);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load config.",e);
        }
    }

    private static void loadTuneUI() {
        UIContext ui = UILoader.UI;
        WebList tuneList = (WebList) ui.getComponentFromName("tuneList");
        tuneList.setEnabled(false);
        int index = 0;
        try {
            removeAllFromList(tuneList);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error(e);
        }
        for (Map.Entry<Integer, Map<Integer, Set<NoteInfo>>> integerMapEntry : playController.getTrackMusic().getTracks().entrySet()) {
            TuneStepLabel label = new TuneStepLabel(integerMapEntry.getKey(), playController.getTrackMusic().getTrackInfo(integerMapEntry.getKey()));
            label.setBounds(0, index * 60, label.getWidth(), label.getHeight());
            tuneList.add(label);
            tunePanels.put(integerMapEntry.getKey(), label);
            index++;
        }
        try {
            for (int i = 0; i < (index) * 2.5; i++) {
                addToJList(tuneList, " ");
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error(e);
        }
        tuneList.repaint();
    }

    private static void resetLoadTuneUI() {
        UIContext ui = UILoader.UI;
        WebList tuneList = (WebList) ui.getComponentFromName("tuneList");
        for (Component component : tuneList.getComponents()) {
            tuneList.remove(component);
        }
        try {
            removeAllFromList(tuneList);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("Impossible exception occurred", e);
        }
        WebTextField tune = (WebTextField) ui.getComponentFromName("tune");
        WebTextField octave = (WebTextField) ui.getComponentFromName("octave");
        WebToggleButton webToggleButton = (WebToggleButton) ui.getComponentFromName("EveryTrackOctaveSame");
        tune.setText("0");
        octave.setText("0");
        webToggleButton.setSelected(false);
    }

    private static AbstractMusicParser getSelectedParser() {
        UIContext ui = UILoader.UI;
        JComboBox<String> comboBoxFileType = (JComboBox<String>) ui.getComponentFromName("ComboBoxFileType");
        return parserNameMap.get(comboBoxFileType.getSelectedItem());
    }

    private static final int MIN_OCTAVE = -1;
    private static final int MAX_OCTAVE = 1;

    private static void autoTune() {
        UIContext ui = UILoader.UI;
        if (!selectedMusic.isEmpty()) {
            File file = new File(PLAY_LIST_CONFIG.getFileNameAndPathMap().get(selectedMusic));
            if (file.exists()) {
                WebToggleButton octaveSame = (WebToggleButton) ui.getComponentFromName("EveryTrackOctaveSame");
                WebTextField octaveTF = (WebTextField) ui.getComponentFromName("octave");
                WebTextField tuneTF = (WebTextField) ui.getComponentFromName("tune");
                TuneStep bestTune = playController.autoTune(MIN_OCTAVE, MAX_OCTAVE, octaveSame.isSelected());
                if (octaveSame.isSelected()) {
                    int octave = bestTune.tune / 12;
                    int note = bestTune.tune % 12;
                    octaveTF.setText(String.valueOf(octave));
                    tuneTF.setText(String.valueOf(note));
                } else {
                    tuneTF.setText(String.valueOf(bestTune.tune));
                    bestTune.trackOctave.forEach((track, best) -> {
                        tunePanels.get(track).setOctave(best);
                    });
                }
            }
        }
    }

    private static TuneStep getCurrentTune() {
        UIContext ui = UILoader.UI;
        WebToggleButton octaveSame = (WebToggleButton) ui.getComponentFromName("EveryTrackOctaveSame");
        WebTextField octave = (WebTextField) ui.getComponentFromName("octave");
        WebTextField tuneTF = (WebTextField) ui.getComponentFromName("tune");
        if (octaveSame.isSelected()) {
            int pitch = Integer.parseInt(octave.getText());
            int tune = Integer.parseInt(tuneTF.getText());
            TuneStep tuneStep = new TuneStep();
            tuneStep.tracksSame = true;
            tuneStep.tune = pitch * 12 + tune;
            return tuneStep;
        } else {
            TuneStep tuneStep = new TuneStep();
            tuneStep.tracksSame = false;
            tuneStep.tune = Integer.parseInt(tuneTF.getText());
            tunePanels.forEach((track, textField) -> {
                tuneStep.trackOctave.put(track, textField.getTuneStep());
            });
            return tuneStep;
        }
    }

    private static KeyMap getSelectedKeyMap() {
        UIContext ui = UILoader.UI;
        JComboBox<String> comboBoxFileType = (JComboBox<String>) ui.getComponentFromName("ComboBoxKeyMap");
        return KeyMapLoader.getInstance().getLoadedKeyMap(comboBoxFileType.getSelectedItem().toString());
    }

    private static void setSpeed() {
        UIContext ui = UILoader.UI;
        JTextField textField = (JTextField) ui.getComponentFromName("speed");
        playController.setSpeed(Double.parseDouble(textField.getText()));
        LOGGER.info("Setting speed to :" + playController.getSpeed());
    }

    public static void updateTuneInfo() {
        playController.setTuneStep(getCurrentTune());
    }

    public static void updateUI() {
        UIContext ui = UILoader.UI;
        if (ui != null) {
            JToggleButton sameOctave = (JToggleButton) ui.getComponentFromName("EveryTrackOctaveSame");
            JTextField octave = (JTextField) ui.getComponentFromName("octave");
            JSlider slider = (JSlider) ui.getComponentFromName("slider");
            JButton button = (JButton) ui.getComponentFromName("pause");

            if (sameOctave.isSelected()) {
                octave.setEnabled(true);
                for (Map.Entry<Integer, TuneStepLabel> integerTuneStepLabelEntry : tunePanels.entrySet()) {
                    integerTuneStepLabelEntry.getValue().setEnableOctave(false);
                }
            } else {
                octave.setEnabled(false);
                for (Map.Entry<Integer, TuneStepLabel> integerTuneStepLabelEntry : tunePanels.entrySet()) {
                    integerTuneStepLabelEntry.getValue().setEnableOctave(true);
                }
            }
            if (!playController.isPlaying()) {
                slider.setValue(0);
                UILoader.trySetIcon(button, "/images/resume.png");
            }
        }
    }
    @PostProcessor
    public static void refreshPlayList() {
        try {
            UIContext ui = UILoader.UI;
            JList<String> list1 = ((JList<String>) ui.getComponentFromName("fileList"));
            removeAllFromList(list1);
            List<String> list = new ArrayList<>(PLAY_LIST_CONFIG.getFileNameAndPathMap().keySet());
            list = list.stream().sorted(Comparator.comparingInt(s -> s.charAt(0))).collect(Collectors.toList());
            for (String s : list) {
                addToJList(list1, s);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("Impossible exception occurred!", e);
        }
    }

    public static void setToTuneStep(TuneStep tuneStep){
        UIContext ui = UILoader.UI;
        WebToggleButton octaveSame = (WebToggleButton) ui.getComponentFromName("EveryTrackOctaveSame");
        WebTextField octave = (WebTextField) ui.getComponentFromName("octave");
        WebTextField tuneTF = (WebTextField) ui.getComponentFromName("tune");
        octaveSame.setSelected(tuneStep.tracksSame);
        if (tuneStep.tracksSame) {
            octave.setText(String.valueOf(tuneStep.tune/12));
            tuneTF.setText(String.valueOf(tuneStep.tune%12));
            tunePanels.forEach((track, textField) -> {
                textField.setEnableOctave(false);
            });
        } else {
            tuneTF.setText(String.valueOf(tuneStep.tune));
            tunePanels.forEach((track, textField) -> {
                textField.setEnableOctave(true);
                try {
                    textField.setOctave(tuneStep.trackOctave.get(track));
                } catch (Exception e) {
                    LOGGER.warn("set tune step failed.",e);
                }
            });
        }
        updateUI();
    }
}
