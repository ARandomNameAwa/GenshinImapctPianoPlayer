package me.ironblock.genshinimpactmusicplayer.ui;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMapLoader;
import me.ironblock.genshinimpactmusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.genshinimpactmusicplayer.note.AbstractNoteMessage;
import me.ironblock.genshinimpactmusicplayer.playController.MusicParserAndPlayerRegistry;
import me.ironblock.genshinimpactmusicplayer.playController.PlayController;
import me.ironblock.genshinimpactmusicplayer.utils.IOUtils;
import me.ironblock.genshinimpactmusicplayer.utils.TimeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 控制窗口(也是主窗口)
 *
 * @author Iron__Block
 */
public class ControllerFrame extends JFrame {
    public static final String programName = "Genshin Impact Music Player";
    public static final String programVersion = "v1.1.0";
    public static final String programAuthor = "Iron_Block";
    public static final int frameWidth = 650;
    public static final int frameHeight = 320;
    public static ControllerFrame instance;
    private final JLabel label_file_path = new JLabel("File Path");
    private final JLabel label_speed = new JLabel("Speed");
    private final JLabel label_tps = new JLabel("tps");
    private final JLabel label_parser = new JLabel("File Type");
    private final JLabel label_keyMap = new JLabel("Key Map");
    private final JComboBox<String> comboBox_parser = new JComboBox<>();
    private final JComboBox<String> comboBox_keyMap = new JComboBox<>();
    private final JTextField textField_file_path = new JTextField();
    private final JTextField textField_speed = new JTextField();
    private final JTextArea textArea_info = new JTextArea();
    private final JButton button_start = new JButton("Start");
    private final JButton button_pause = new JButton("Pause");
    private final JButton button_stop = new JButton("Stop");
    private final PlayController playController = new PlayController();
    private final Map<String, AbstractMusicParser> parserNameMap = new HashMap<>();

    public static void init() {
        instance = new ControllerFrame();
        instance.setup();
    }

    /**
     * 初始化frame
     */
    private void setup() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        // 屏幕的物理大小还需要知道屏幕的dpi 意思是说一英寸多少个象素
        double dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        // 然后用象素除以dpi 就可以得到多少英寸了，在英寸转厘米

        double w = width / dpi;
        double h = height / dpi;
        //居中
        this.setBounds(((int) (frameWidth / 2 - w / 2)), ((int) (frameHeight / 2 - h / 2)), frameWidth, frameHeight);
        this.setLayout(null);
        label_file_path.setBounds(20, 30, 85, 30);
        label_speed.setBounds(20, 70, 85, 30);
        button_start.setBounds(20, 230, 85, 50);
        button_pause.setBounds(130, 230, 85, 50);
        button_stop.setBounds(230, 230, 85, 50);
        textField_file_path.setBounds(130, 30, 200, 25);
        textField_speed.setBounds(130, 70, 150, 25);
        label_tps.setBounds(280, 70, 50, 25);
        textArea_info.setBounds(350, 30, 250, 250);
        label_parser.setBounds(20, 110, 90, 30);
        label_keyMap.setBounds(20, 190, 85, 30);
        comboBox_parser.setBounds(130, 110, 200, 30);
        comboBox_keyMap.setBounds(130, 190, 200, 30);
        button_start.addActionListener(e -> this.onStartButtonClicked());
        button_pause.addActionListener(e -> this.onPauseButtonClicked());
        button_stop.addActionListener(e -> this.onStopButtonClicked());
        textField_file_path.addKeyListener(new KeyAdapter() {
                                               @Override
                                               public void keyTyped(KeyEvent e) {
                                                   onTextFieldAndComboBoxUpdate();
                                               }
                                           }

        );
        comboBox_parser.addActionListener(l -> {
            onTextFieldAndComboBoxUpdate();
        });

        this.add(label_file_path);
        this.add(label_speed);
        this.add(label_tps);
        this.add(textField_file_path);
        this.add(textArea_info);
        this.add(textField_speed);
        this.add(button_start);
        this.add(button_pause);
        this.add(button_stop);
        this.add(comboBox_parser);
        this.add(comboBox_keyMap);
        this.add(label_parser);
        this.add(label_keyMap);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        for (String s : KeyMapLoader.getInstance().getAllLoadedMapName()) {
            comboBox_keyMap.addItem(s);
        }
        this.setResizable(false);
//        this.setAlwaysOnTop(true);
        super.setTitle(programName + " " + programVersion + " by " + programAuthor);
        this.setVisible(true);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        super.setTitle(programName + " " + programVersion + " by " + programAuthor + ":" + title);
    }

    /**
     * 开始按钮被触发后的事件
     */
    private void onStartButtonClicked() {
        try {
            System.out.println("Setting keyMap to " + comboBox_keyMap.getSelectedItem());
            playController.setActiveKeyMap(KeyMapLoader.getInstance().getLoadedKeyMap((String) comboBox_keyMap.getSelectedItem()));
            playController.setSpeed(Integer.parseInt(textField_speed.getText()));

            playController.startPlay(IOUtils.openStream(textField_file_path.getText()), parserNameMap.get(Objects.requireNonNull(comboBox_parser.getSelectedItem()).toString()));

        } catch (Exception exceptionNeedToBeDisplayed) {

            exceptionNeedToBeDisplayed.printStackTrace();
            setTitle(exceptionNeedToBeDisplayed.toString());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(byteArrayOutputStream);
            exceptionNeedToBeDisplayed.printStackTrace(writer);
            writer.flush();
            System.out.println(byteArrayOutputStream);
            textArea_info.setText(byteArrayOutputStream.toString());
        }
    }

    /**
     * 暂停按钮被触发的事件
     */
    private void onPauseButtonClicked() {
        try {
            playController.setSpeed(Integer.parseInt(textField_speed.getText()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        playController.switchPause();
        if (button_pause.getText().equals("Pause")) {
            button_pause.setText("Resume");
        } else {
            button_pause.setText("Pause");
        }
    }

    /**
     * 停止按钮被触发后的事件
     */
    private void onStopButtonClicked() {
        playController.stopPlay();
    }

    /**
     * 更新信息栏(由AbstractMusicPlayer.updateInfo()调用)
     *
     * @param actualSpeed 真实速度
     * @param currentTick 目前的tick
     * @param lengthTick  总tick
     * @param speed       速度
     * @param finished    是否播放完毕
     */
    public void updateInfoTextField(int actualSpeed, long currentTick, long lengthTick, int speed, boolean finished) {
        if (!finished) {
            textArea_info.setText("Actual Speed:" + actualSpeed + "tps\n" + "currentTick:" + currentTick + "/" + lengthTick + "\n" + TimeUtils.getMMSSFromS((int) (currentTick / speed)) + TimeUtils.progressBar(((double) currentTick) / lengthTick, 20) + TimeUtils.getMMSSFromS(((int) (lengthTick / speed))));
        } else {
            textArea_info.setText("Music Finished");
        }


    }

    private void onTextFieldAndComboBoxUpdate() {
        updateComboBox();
    }

    /**
     * 更新下拉框
     */
    private void updateComboBox() {
        //update parser selector
        if (!textField_file_path.getText().isEmpty()) {
            File file = new File(textField_file_path.getText());
            if (file.exists()) {
                String[] tmp = file.getName().split("\\.");
                Set<AbstractMusicParser> parsers = MusicParserAndPlayerRegistry.getSuffixParsers(tmp[tmp.length - 1]);
                if (parsers != null && !parsers.isEmpty()) {
                    String prevSelectItem = (String) comboBox_parser.getSelectedItem();
                    comboBox_parser.removeAllItems();
                    parserNameMap.clear();

                    for (AbstractMusicParser parser : parsers) {
                        parserNameMap.put(parser.getMusicFileTypeName(), parser);
                        comboBox_parser.addItem(parser.getMusicFileTypeName());
                    }
                    if (prevSelectItem != null && !prevSelectItem.isEmpty()) {
                        if (parserNameMap.containsKey(prevSelectItem)) {
                            comboBox_parser.setSelectedItem(prevSelectItem);
                        }
                    }
                }

            }
        }

    }


}
