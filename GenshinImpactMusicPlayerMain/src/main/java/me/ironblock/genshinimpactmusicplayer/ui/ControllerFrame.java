package me.ironblock.genshinimpactmusicplayer.ui;

import me.ironblock.genshinimpactmusicplayer.Launch;
import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMapLoader;
import me.ironblock.genshinimpactmusicplayer.music.TrackMusic;
import me.ironblock.genshinimpactmusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.genshinimpactmusicplayer.playController.MusicParserAndPlayerRegistry;
import me.ironblock.genshinimpactmusicplayer.playController.PlayController;
import me.ironblock.genshinimpactmusicplayer.utils.IOUtils;
import me.ironblock.genshinimpactmusicplayer.utils.TimeUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * 控制窗口(也是主窗口)
 *
 * @author Iron__Block
 */
public class ControllerFrame extends JFrame {
    public static final String programName = "Genshin Impact Music Player";
    public static final String programVersion = "v1.2.0";

    public static final int frameWidth = 450;
    public static final int frameHeight = 320;

    public static final int tuneFrameWidth = 550;
    public static final int tuneFrameHeight = 120;

    public static ControllerFrame instance;

    private final JFrame tuneFrame = new JFrame();


    private final JLabel label_file_path = new JLabel("文件路径");
    private final JLabel label_speed = new JLabel("速度");
    private final JLabel label_tps = new JLabel("倍");
    private final JLabel label_parser = new JLabel("文件类型");
    private final JLabel label_keyMap = new JLabel("Key Map");
    private final JLabel label_tune = new JLabel("曲调");
    private final JLabel label_pitch = new JLabel("升降八度");
    private final JLabel label_currentPlayTime = new JLabel("00:00");
    private final JLabel label_totalPlayTime = new JLabel("00:00");

    private final JComboBox<String> comboBox_parser = new JComboBox<>();
    private final JComboBox<String> comboBox_keyMap = new JComboBox<>();

    private final JTextField textField_file_path = new JTextField();
    private final JTextField textField_speed = new JTextField("1");
    private final JTextField textField_tune = new JTextField("0");
    private final JTextField textField_pitch = new JTextField("0");


    private final JButton button_start = new JButton("Start");
    private final JButton button_pause = new JButton("Pause");
    private final JButton button_stop = new JButton("Stop");
    private final JButton button_autoTune = new JButton("自动调音");
    private final JButton button_choseFile = new JButton("...");

    private final JFileChooser fileChooser = new JFileChooser(new File("."));
    private final JSlider jSlider = new JSlider(0, 100, 0);
    private final JCheckBox checkbox_syncPitch = new JCheckBox("每个音轨升降八度同步");

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

        tuneFrame.setBounds(((int) (tuneFrameWidth / 2 - w / 2)) - 50, ((int) (tuneFrameHeight / 2 - h / 2)) - 50, tuneFrameWidth, tuneFrameHeight);
        tuneFrame.setLayout(null);
        //label
        label_file_path.setBounds(20, 30, 85, 30);
        label_speed.setBounds(20, 70, 85, 30);
        label_tps.setBounds(280, 70, 50, 25);
        label_parser.setBounds(20, 110, 90, 30);
        label_keyMap.setBounds(20, 150, 85, 30);
        label_pitch.setBounds(10, 30, 80, 30);
        label_tune.setBounds(130, 30, 50, 30);
        label_currentPlayTime.setBounds(20, 184, 60, 40);
        label_totalPlayTime.setBounds(300, 184, 60, 40);
        //button
        button_choseFile.setBounds(280, 30, 30, 25);
        button_start.setBounds(20, 230, 85, 50);
        button_pause.setBounds(130, 230, 85, 50);
        button_stop.setBounds(230, 230, 85, 50);
        button_autoTune.setBounds(402, 30, 100, 30);

        //textFields
        textField_file_path.setBounds(130, 30, 150, 25);
        textField_speed.setBounds(130, 70, 150, 25);
        textField_pitch.setBounds(70, 30, 50, 30);
        textField_tune.setBounds(165, 30, 50, 30);


        comboBox_parser.setBounds(130, 110, 200, 30);
        comboBox_keyMap.setBounds(130, 150, 200, 30);

        jSlider.setBounds(58, 180, 240, 50);
        checkbox_syncPitch.setBounds(238, 30, 160, 30);
        checkbox_syncPitch.setSelected(true);

        //Listeners
        button_start.addActionListener(e -> this.onStartButtonClicked());
        button_pause.addActionListener(e -> this.onPauseButtonClicked());
        button_stop.addActionListener(e -> this.onStopButtonClicked());
        textField_file_path.addKeyListener(new KeyAdapter() {
                                               @Override
                                               public void keyPressed(KeyEvent e) {
                                                   onTextFieldAndComboBoxUpdate();
                                               }
                                           }

        );
        comboBox_parser.addActionListener(l -> onTextFieldAndComboBoxUpdate());

        textField_pitch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                onPitchTextFieldKeyTyped(e);
            }
        });


        textField_tune.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                onTuneTextFieldKeyTyped(e);
            }
        });

        button_choseFile.addActionListener(e -> chooseFile());

        button_autoTune.addActionListener(e -> autoTune());

        jSlider.addChangeListener(this::onSliderDragged);
        jSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onMouseStartPressSlider();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                onMouseStopPressSlider();
            }
        });

        checkbox_syncPitch.addChangeListener(this::onJCheckBoxStateChanged);


        if (!Launch.DEBUG_MODE)
            this.setAlwaysOnTop(true);


        this.add(button_choseFile);
        tuneFrame.add(button_autoTune);
        this.add(label_file_path);
        this.add(label_speed);
        this.add(label_tps);
        this.add(textField_file_path);
        this.add(textField_speed);
        this.add(button_start);
        this.add(button_pause);
        this.add(button_stop);
        this.add(comboBox_parser);
        this.add(comboBox_keyMap);
        this.add(label_parser);
        this.add(label_keyMap);
        tuneFrame.add(label_tune);
        tuneFrame.add(label_pitch);
        tuneFrame.add(textField_tune);
        tuneFrame.add(textField_pitch);
        tuneFrame.add(checkbox_syncPitch);

        this.add(label_currentPlayTime);
        this.add(label_totalPlayTime);
        this.add(jSlider);


        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        tuneFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        for (String s : KeyMapLoader.getInstance().getAllLoadedMapName()) {
            comboBox_keyMap.addItem(s);
        }

        drag();
        this.setResizable(false);
        tuneFrame.setResizable(false);
        super.setTitle(programName + " " + programVersion);
        this.setVisible(true);
        tuneFrame.setVisible(true);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        super.setTitle(programName + " " + programVersion + ":" + title);
    }

    /**
     * 开始按钮被触发后的事件
     */
    private void onStartButtonClicked() {
        try {
            System.out.println("Setting keyMap to " + comboBox_keyMap.getSelectedItem());
            playController.setActiveKeyMap(KeyMapLoader.getInstance().getLoadedKeyMap((String) comboBox_keyMap.getSelectedItem()));
            if (!playController.TrackMusicLoaded())
                playController.prepareMusicPlayed(IOUtils.openStream(textField_file_path.getText()), parserNameMap.get(Objects.requireNonNull(comboBox_parser.getSelectedItem()).toString()), textField_file_path.getText());

            playController.startPlay(getCurrentTune());
            playController.setSpeed(Double.parseDouble(textField_speed.getText()));
            textField_pitch.setEditable(false);
            textField_tune.setEditable(false);
            button_autoTune.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 暂停按钮被触发的事件
     */
    private void onPauseButtonClicked() {
        try {
            playController.setSpeed(Double.parseDouble(textField_speed.getText()));
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
        textField_pitch.setEditable(true);
        textField_tune.setEditable(true);
        button_autoTune.setEnabled(true);
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
            label_currentPlayTime.setText(TimeUtils.getMMSSFromS((int) (currentTick / speed)));
            label_totalPlayTime.setText(TimeUtils.getMMSSFromS(((int) (lengthTick / speed))));
            jSlider.setValue((int) (((double) currentTick) / lengthTick * 100));
        } else {
            textField_pitch.setEditable(true);
            textField_tune.setEditable(true);
            button_autoTune.setEnabled(true);
        }


    }

    /**
     * 升降八度的文本框的事件处理
     */
    private void onPitchTextFieldKeyTyped(KeyEvent event) {
        int key = Integer.parseInt(((JTextField) event.getSource()).getText());

        if (event.getKeyCode() == 38) {
            //上键
            key += 1;

        } else if (event.getKeyCode() == 40) {
            //上键
            key -= 1;
        }
        ((JTextField) event.getSource()).setText(String.valueOf(key));

        event.consume();
    }


    /**
     * 升降音调的文本框的事件处理
     */
    private void onTuneTextFieldKeyTyped(KeyEvent event) {
        int key = Integer.parseInt(textField_tune.getText());
        while (key < -7) {
            key += 8;
        }
        while (key > 7) {
            key -= 8;
        }
        if (event.getKeyCode() == 38) {
            //上键
            if (key != 7) {
                key += 1;
            } else {
                key = -7;
            }

        } else if (event.getKeyCode() == 40) {
            //上键
            if (key != -7) {
                key -= 1;
            } else {
                key = 7;
            }
        }
        textField_tune.setText(String.valueOf(key));
        event.consume();
    }

    private void onTextFieldAndComboBoxUpdate() {
        File file = new File(textField_file_path.getText());
        if (file.exists()) {
            onFilePathCompleted();
        }
    }

    private static final int tuneMin = -20;
    private static final int tuneMax = 20;

    private void autoTune() {
        if (!textField_file_path.getText().isEmpty()) {
            File file = new File(textField_file_path.getText());
            if (file.exists()) {
                AbstractMusicParser parser = parserNameMap.get(Objects.requireNonNull(comboBox_parser.getSelectedItem()).toString());
                KeyMap keyMap = KeyMapLoader.getInstance().getLoadedKeyMap(Objects.requireNonNull(comboBox_keyMap.getSelectedItem()).toString());

                playController.prepareMusicPlayed(IOUtils.openStream(file.getAbsolutePath()), parser, file.getAbsolutePath());
                playController.setActiveKeyMap(keyMap);

                int bestTune = playController.autoTune(tuneMin, tuneMax);

                int octave = bestTune / 12;
                int note = bestTune % 12;

                textField_pitch.setText(String.valueOf(octave));
                textField_tune.setText(String.valueOf(note));

            }

        }
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

    /**
     * 获取现在的音调
     *
     * @return 音调
     */
    private int getCurrentTune() {
        int pitch = Integer.parseInt(textField_pitch.getText());
        int tune = Integer.parseInt(textField_tune.getText());
        return pitch * 12 + tune;
    }


    private void chooseFile() {
        int status = fileChooser.showOpenDialog(this);
        if (status == JFileChooser.FILES_ONLY) {
            textField_file_path.setText(fileChooser.getSelectedFile().getAbsolutePath());
            onFilePathCompleted();
        }
    }

    private void onFilePathCompleted() {
        updateComboBox();
        playController.prepareMusicPlayed(IOUtils.openStream(textField_file_path.getText()), parserNameMap.get(Objects.requireNonNull(comboBox_parser.getSelectedItem()).toString()), textField_file_path.getText());
        addTracksUIForMusic(playController.getTrackMusic());
        IOUtils.closeAllStreams();

    }

    /**
     * 注册拖拽事件
     */
    private void drag() {
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {

                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));

                        if (list.size() > 0) {
                            textField_file_path.setText(list.get(0).getAbsolutePath());
                            onFilePathCompleted();
                        }

                    } else {

                        dtde.rejectDrop();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });


    }

    private boolean mousePressingSlider = false;

    private void onSliderDragged(ChangeEvent event) {
        if (mousePressingSlider && playController.isPlaying()) {
            double progress = ((double) jSlider.getValue()) / 100;
            label_currentPlayTime.setText(TimeUtils.getMMSSFromS((int) (TimeUtils.getSFromMMSS(label_totalPlayTime.getText()) * progress)));
        }
    }

    private void onMouseStartPressSlider() {
        if (playController.isPlaying()) {
            onPauseButtonClicked();
        }
        mousePressingSlider = true;
    }

    private void onMouseStopPressSlider() {
        if (playController.isPlaying()) {
            int tickToJump = (int) (((double) jSlider.getValue()) / 100 * playController.getTotalTick());
            playController.jumpToTick(tickToJump);
            onPauseButtonClicked();
        }
        mousePressingSlider = false;
    }

    private final Set<Component> components = new HashSet<>();
    private final Map<Integer, JTextField> trackTextFieldMap = new HashMap<>();

    private void addTracksUIForMusic(TrackMusic music) {
        for (Component component : components) {
            tuneFrame.remove(component);
        }
        components.clear();

        int loopTime = 0;
        for (int trackIndex : music.getTracks().keySet()) {
            JLabel label_trackDescriptor = new JLabel("轨道" + trackIndex + ":" + music.getTrackInfo(trackIndex));
            JLabel label_pitch = new JLabel("升降八度");
            JTextField textField_pitch = new JTextField("0");
            JButton button = new JButton("静音");
            textField_pitch.setEditable(!checkbox_syncPitch.isSelected());
            label_trackDescriptor.setBounds(10, 50 + loopTime * 70, 300, 45);
            label_pitch.setBounds(10, 85 + loopTime * 70, 80, 30);
            textField_pitch.setBounds(90, 85 + loopTime * 70, 80, 30);
            button.setBounds(190, 85 + loopTime * 70, 80, 30);


            button.addActionListener(e -> onMuteButtonClicked(trackIndex, e));
            textField_pitch.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    onPitchTextFieldKeyTyped(e);
                }
            });


            tuneFrame.add(label_trackDescriptor);
            tuneFrame.add(label_pitch);
            tuneFrame.add(textField_pitch);
            tuneFrame.add(button);

            trackTextFieldMap.put(trackIndex, textField_pitch);

            components.add(label_trackDescriptor);
            components.add(label_pitch);
            components.add(textField_pitch);
            components.add(button);


            loopTime++;
        }

        tuneFrame.setBounds(tuneFrame.getX(), tuneFrame.getY(), tuneFrameWidth, tuneFrameHeight + loopTime * 70);
        tuneFrame.setVisible(false);
        tuneFrame.setVisible(true);
    }

    private void onMuteButtonClicked(int track, ActionEvent event) {
        if (((JButton) event.getSource()).getText().equals("静音")) {
            playController.getTrackMusic().muteTrack(track);
            ((JButton) event.getSource()).setText("解除静音");
        } else {
            playController.getTrackMusic().dismuteTrack(track);
            ((JButton) event.getSource()).setText("静音");
        }
    }


    private void onJCheckBoxStateChanged(ChangeEvent e) {
        JCheckBox source = ((JCheckBox) e.getSource());
        if (source.isSelected()) {
            textField_pitch.setEditable(true);
            for (JTextField value : trackTextFieldMap.values()) {
                value.setEditable(false);
            }
        } else {
            textField_pitch.setEditable(false);
            for (JTextField value : trackTextFieldMap.values()) {
                value.setEditable(true);
            }
        }
    }


}
