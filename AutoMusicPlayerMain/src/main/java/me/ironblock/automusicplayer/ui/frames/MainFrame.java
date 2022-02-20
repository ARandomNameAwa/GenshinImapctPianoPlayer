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
import me.ironblock.automusicplayer.keymap.KeyMapLoader;
import me.ironblock.automusicplayer.music.TuneStep;
import me.ironblock.automusicplayer.music.parser.AbstractMusicParser;
import me.ironblock.automusicplayer.nativeInvoker.WindowsMessage;
import me.ironblock.automusicplayer.note.NoteInfo;
import me.ironblock.automusicplayer.playcontroller.MusicParserRegistry;
import me.ironblock.automusicplayer.playcontroller.PlayController;
import me.ironblock.automusicplayer.ui.annotations.Initializer;
import me.ironblock.automusicplayer.ui.annotations.Listener;
import me.ironblock.automusicplayer.ui.annotations.WindowComponent;
import me.ironblock.automusicplayer.ui.annotations.WindowFrame;
import me.ironblock.automusicplayer.ui.components.TuneStepLabel;
import me.ironblock.automusicplayer.ui.loader.UIContext;
import me.ironblock.automusicplayer.ui.loader.UILoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

    @WindowComponent(name = "labelFileList", x = 50, y = 0, width = 140, height = 45, initPara = "PlayLists")
    public WebLabel label1;
    @WindowComponent(name = "labelPlaySettings", x = 400, y = 0, width = 140, height = 45, initPara = "Play Settings")
    public WebLabel label2;
    @WindowComponent(name = "fileList", x = 50, y = 50, width = 330, height = 560, initializer = "addScroll", listeners = {"jListMouseListener"})
    public WebList list1;
    @WindowComponent(name = "slider", x = 50, y = 650, width = 880, height = 35, initializer = "initSlider")
    public WebSlider slider;
    @WindowComponent(name = "addFile", x = 50, y = 610, width = 30, height = 30, background = "/images/addFile.png", listeners = {"addFileActionListener"})
    public WebButton button1;
    @WindowComponent(name = "deleteFile", x = 80, y = 610, width = 30, height = 30, background = "/images/deleteFile.png", listeners = {"deleteFileListener"})
    public WebButton button2;
    @WindowComponent(name = "labelSpeed", x = 440, y = 50, width = 120, height = 40, initPara = "Speed:")
    public WebLabel label5;
    @WindowComponent(name = "speed", x = 590, y = 50, width = 120, height = 40, initPara = "1.0")
    public WebTextField textField1;
    @WindowComponent(name = "addSpeed", x = 705, y = 50, width = 20, height = 23, background = "/images/add1.png",listeners = {"addSpeedListener"})
    public WebButton button6;
    @WindowComponent(name = "subSpeed", x = 705, y = 67, width = 20, height = 23, background = "/images/sub1.png",listeners = {"" + "subSpeedListener"})
    public WebButton button7;
    @WindowComponent(name = "labelFileType", x = 440, y = 100, width = 120, height = 40, initPara = "File Type:")
    public WebLabel label6;
    @WindowComponent(name = "ComboBoxFileType", x = 590, y = 100, width = 240, height = 40)
    public WebComboBox comboBox1;
    @WindowComponent(name = "playMode", x = 440, y = 150, width = 120, height = 40, initPara = "PlayMode:")
    public WebLabel label3;
    @WindowComponent(name = "postMessage", x = 730, y = 150, width = 120, height = 40, initPara = "PostMessage", initializer = "initPostMessage")
    public WebRadioButton button4;
    @WindowComponent(name = "awt", x = 590, y = 150, width = 120, height = 40, initPara = "AWT Robot", initializer = "initRadioButton")
    public WebRadioButton button3;
    @WindowComponent(name = "labelKeyMap", x = 440, y = 200, width = 120, height = 40, initPara = "Key Map")
    public WebLabel label11;
    @WindowComponent(name = "ComboBoxKeyMap", x = 590, y = 200, width = 240, height = 40, initializer = "initKeyMap")
    public WebComboBox comboBox3;
    @WindowComponent(name = "labelSelectWindow", x = 440, y = 250, width = 120, height = 40, initPara = "Select window:")
    public WebLabel label4;
    @WindowComponent(name = "ComboBoxSelectWindow", x = 590, y = 250, width = 240, height = 40, initializer = "initComboBoxWindowTitle")
    public WebComboBox comboBox2;
    @WindowComponent(name = "labelTuneSettings", x = 400, y = 300, width = 140, height = 45, initPara = "Tune Settings")
    public WebLabel label7;
    @WindowComponent(name = "tuneList", x = 440, y = 390, width = 480, height = 180, initializer = "addScroll")
    public WebList tuneList;
    @WindowComponent(name = "lastSong", x = 400, y = 690, width = 50, height = 50, background = "/images/lastSong.png")
    public WebButton button5;
    @WindowComponent(name = "pause", x = 450, y = 690, width = 50, height = 50, background = "/images/resume.png")
    public WebButton button8;
    @WindowComponent(name = "stop", x = 500, y = 690, width = 50, height = 50, background = "/images/stop.png")
    public WebButton button9;
    @WindowComponent(name = "nextSong", x = 550, y = 690, width = 50, height = 50, background = "/images/nextSong.png")
    public WebButton button10;
    @WindowComponent(name = "currentMusic", x = 430, y = 620, width = 200, height = 50, initPara = "Please select a music to play.")
    public WebLabel label8;
    @WindowComponent(name = "currentTime", x = 50, y = 660, width = 200, height = 50, initPara = "00:00")
    public WebLabel label9;
    @WindowComponent(name = "totalTime", x = 900, y = 660, width = 200, height = 50, initPara = "00:00")
    public WebLabel label10;
    @WindowComponent(name = "EveryTrackOctaveSame", x = 755, y = 340, width = 150, height = 30,initPara = "EveryTrackOctaveSame")
    public WebToggleButton button11;
    @WindowComponent(name = "OctaveLabel", x = 590, y = 330, width = 50, height = 50, initPara = "Octave:")
    public WebLabel label12;
    @WindowComponent(name = "octave", x = 640, y = 335, width = 50, height = 40, initPara = "0")
    public WebTextField textField2;
    @WindowComponent(name = "addOctave",x = 687,y = 335,width = 23,height = 23,background = "/images/add1.png",listeners = {"addOctave"})
    public WebButton button13;
    @WindowComponent(name = "subOctave",x = 687,y = 352,width = 23,height = 23, background = "/images/sub1.png",listeners = {"subOctave"})
    public WebButton button14;
    @WindowComponent(name = "tuneLabel", x = 440, y = 330, width = 50, height = 50, initPara = "Tune:")
    public WebLabel label13;
    @WindowComponent(name = "tune", x = 490, y = 335, width = 50, height = 40, initPara = "0")
    public WebTextField textField3;
    @WindowComponent(name = "addTune",x = 537,y = 335,width = 23,height = 23,background = "/images/add1.png",listeners = {"addTune"})
    public WebButton button15;
    @WindowComponent(name = "subTune",x = 537,y = 352,width = 23,height = 23, background = "/images/sub1.png",listeners = {"subTune"})
    public WebButton button16;
    @WindowComponent(name = "autoTune",x = 775,y = 580,width = 150,height = 30,initPara = "AutoTune",listeners = {"autoTune"})
    public WebButton button12;
    private static String selectedMusic = "";
    private static final PlayController playController = new PlayController();

    @Initializer(name = "initFrame")
    public static void initFrame(JFrame frame) {
        frame.setResizable(false);
        drag(frame);
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
    }


    private static final Map<String, String> fileNameAndPathMap = new HashMap<>();

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
                    fileNameAndPathMap.remove(s);
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
            if (e.getClickCount() == 2) {
                UIContext ui = UILoader.UI;
                JList<String> list = ((JList<String>) ui.getComponentFromName("fileList"));
                if (list.getSelectedValue() != null) {
                    selectedMusic = list.getSelectedValue();
                    JLabel label = (JLabel) ui.getComponentFromName("currentMusic");
                    label.setText(selectedMusic);
                    onSongSelected(fileNameAndPathMap.get(selectedMusic));
                    loadSongWithParser(fileNameAndPathMap.get(selectedMusic), getSelectedParser());
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
    @Listener(name = "addSpeedListener",parent = ActionListener.class)
    public static class addSpeedListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("speed");
            try {
                int i = (int)(Double.parseDouble(textField.getText())*10);
                textField.setText(String.valueOf((i+1)/10d));
            } catch (NumberFormatException ex) {
                LOGGER.warn(ex);
            }
        }
    }
    @Listener(name = "subSpeedListener",parent = ActionListener.class)
    public static class subSpeedListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("speed");
            try {
                int i = (int) (Double.parseDouble(textField.getText())*10);
                if (i < 0){
                    i = 1;
                }
                if (i-1>0){
                    i-=1;
                }
                textField.setText(String.valueOf(i/10d));
            } catch (NumberFormatException ex) {
                LOGGER.warn(ex);
            }
        }
    }

    @Listener(name = "autoTune",parent = ActionListener.class)
    public static class AutoTuneListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            autoTune();
        }
    }
    @Listener(name = "addOctave",parent = ActionListener.class)
    public static class AddOctave implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("octave");
            try {
                int i = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf(i+1));
            } catch (NumberFormatException ex) {
                LOGGER.error(ex);
            }

        }
    }
    @Listener(name = "subOctave",parent = ActionListener.class)
    public static class SubOctave implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("octave");
            try {
                int i = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf(i-1));
            } catch (NumberFormatException ex) {
                LOGGER.error(ex);
            }

        }
    }
    @Listener(name = "addTune",parent = ActionListener.class)
    public static class AddTune implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("tune");
            try {
                int i = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf(i+1));
            } catch (NumberFormatException ex) {
                LOGGER.error(ex);
            }

        }
    }
    @Listener(name = "subTune",parent = ActionListener.class)
    public static class SubTune implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            UIContext ui = UILoader.UI;
            JTextField textField = (JTextField) ui.getComponentFromName("tune");
            try {
                int i = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf(i-1));
            } catch (NumberFormatException ex) {
                LOGGER.error(ex);
            }

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
                            addToFileList(names,paths);
                        }
                        long end = System.currentTimeMillis();
                        LOGGER.info("Import files took "+(end-start)+" ms.");
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
        try {
            UIContext ui = UILoader.UI;
            for (int i = 0; i < names.length; i++) {
                fileNameAndPathMap.put(names[i], paths[i]);
            }
            JList<String> list1 = ((JList<String>) ui.getComponentFromName("fileList"));
            removeAllFromList(list1);
            List<String> list = new ArrayList<>(fileNameAndPathMap.keySet());
            list = list.stream().sorted(Comparator.comparingInt(s->s.charAt(0))).collect(Collectors.toList());
            for (String s :list) {
                addToJList(list1, s);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("Impossible exception occurred!", e);
        }

    }
    private static void addToFileList(String name, String path) {
        try {
            UIContext ui = UILoader.UI;
            fileNameAndPathMap.put(name, path);
            JList<String> list1 = ((JList<String>) ui.getComponentFromName("fileList"));
            removeAllFromList(list1);
            List<String> list = new ArrayList<>(fileNameAndPathMap.keySet());
            list = list.stream().sorted(Comparator.comparingInt(s->s.charAt(0))).collect(Collectors.toList());
            for (String s :list) {
                addToJList(list1, s);
            }

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("Impossible exception occurred!", e);
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
        try {
            File file = new File(filePath);
            FileInputStream stream = new FileInputStream(file);
            LOGGER.info("Loading song:"+file.getName()+",path:"+file.getAbsolutePath());
            playController.loadMusicWithParser(stream, musicParser, file.getName());
            stream.close();
        } catch (IOException e) {
            LOGGER.error("Failed to load song :", e);
            return;
        }
        resetLoadTuneUI();
        loadTuneUI();
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
            for (int i = 0; i < (index)*2.5; i++) {
                addToJList(tuneList," ");
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
            LOGGER.error("Impossible exception occurred",e);
        }
    }

    private static AbstractMusicParser getSelectedParser() {
        UIContext ui = UILoader.UI;
        JComboBox<String> comboBoxFileType = (JComboBox<String>) ui.getComponentFromName("ComboBoxFileType");
        return parserNameMap.get(comboBoxFileType.getSelectedItem());
    }

    private static void autoTune(){

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


}
