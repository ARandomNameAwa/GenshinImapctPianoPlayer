package me.ironblock.genshinimpactmusicplayer.ui;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMapLoader;
import me.ironblock.genshinimpactmusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.AbstractMusicPlayer;
import me.ironblock.genshinimpactmusicplayer.note.AbstractNoteMessage;
import me.ironblock.genshinimpactmusicplayer.playController.PlayController;
import me.ironblock.genshinimpactmusicplayer.playController.SuffixDealerRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerFrame extends JFrame {
    public static final String programName = "Genshin Impact Music Player";
    public static final String programVersion = "v1.0";
    public static final String programAuthor = "Iron_Block";
    public static final int frameWidth = 650;
    public static final int frameHeight = 320;

    private final JLabel label_file_path = new JLabel("File Path");
    private final JLabel label_speed = new JLabel("Speed");
    private final JLabel label_tps = new JLabel("tps");
    private final JLabel label_parser = new JLabel("Parser");
    private final JLabel label_player = new JLabel("Player");
    private final JLabel label_keyMap = new JLabel("Key Map");
    private final JComboBox<String> comboBox_parser = new JComboBox<>();
    private final JComboBox<String> comboBox_player = new JComboBox<>();
    private final JComboBox<String> comboBox_keyMap = new JComboBox<>();

    private final JTextField textField_file_path = new JTextField();
    private final JTextField textField_speed = new JTextField();
    private final JTextArea textArea_info = new JTextArea();
    private final JButton button_start = new JButton("Start");
    private final JButton button_pause = new JButton("Pause");
    private final JButton button_stop = new JButton("Stop");


    private final PlayController playController = new PlayController();

    private void setup() {
        //https://blog.csdn.net/qq_27870421/article/details/90710218
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
        label_speed.setBounds(20, 80, 85, 30);
        button_start.setBounds(20, 210, 85, 50);
        button_pause.setBounds(130, 210, 85, 50);
        button_stop.setBounds(230, 210, 85, 50);
        textField_file_path.setBounds(130, 30, 200, 25);
        textField_speed.setBounds(130, 80, 150, 25);
        label_tps.setBounds(280, 80, 50, 25);
        textArea_info.setBounds(350, 30, 250, 230);
        label_parser.setBounds(20, 120, 85, 30);
        label_player.setBounds(130, 120, 85, 30);
        label_keyMap.setBounds(230, 120, 85, 30);
        comboBox_parser.setBounds(20, 160, 85, 30);
        comboBox_player.setBounds(130, 160, 85, 30);
        comboBox_keyMap.setBounds(230, 160, 85, 30);
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
        comboBox_parser.addActionListener(l -> {
            onTextFieldAndComboBoxUpdate();
        });
        comboBox_player.addActionListener(l -> {
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
        this.add(comboBox_player);
        this.add(label_parser);
        this.add(label_player);
        this.add(label_keyMap);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        for (String s : KeyMapLoader.getInstance().getAllLoadedMapName()) {
            comboBox_keyMap.addItem(s);
        }
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        super.setTitle(programName + " " + programVersion + " by " + programAuthor);
        this.setVisible(true);
    }

    public void setTitle(String title) {
        super.setTitle(programName + " " + programVersion + " by " + programAuthor + ":" + title);
    }


    private void onStartButtonClicked() {
        try {
            playController.startPlay(textField_file_path.getText(), parserNameMap.get(comboBox_parser.getSelectedItem()), playerNameMap.get(comboBox_player.getSelectedItem()));
            playController.setActiveKeyMap(KeyMapLoader.getInstance().getLoadedKeyMap((String) comboBox_keyMap.getSelectedItem()));
            playController.setSpeed(Integer.parseInt(textField_speed.getText()));
        } catch (Exception exceptionNeedToBeDisplayed) {
            exceptionNeedToBeDisplayed.printStackTrace();
            setTitle(exceptionNeedToBeDisplayed.getMessage());
        }
    }

    private void onPauseButtonClicked() {
        playController.setSpeed(Integer.parseInt(textField_speed.getText()));
        playController.switchPause();
        if (button_pause.getText().equals("Pause")) {
            button_pause.setText("Resume");
        } else {
            button_pause.setText("Pause");
        }
    }

    private void onStopButtonClicked() {
        playController.stopPlay();
    }

    public void updateInfoTextField(int actualSpeed, long currentTick, long lengthTick, boolean finished) {
        if (!finished) {
            textArea_info.setText("Actual Speed:" + actualSpeed + "tps\n" + "currentTick:" + currentTick + "/" + lengthTick);
        } else {
            textArea_info.setText("Music Finished");
        }


    }

    private void onTextFieldAndComboBoxUpdate() {
        updateComboBox();
    }

    private final Map<String, AbstractMusicParser> parserNameMap = new HashMap<>();
    private final Map<String, AbstractMusicPlayer> playerNameMap = new HashMap<>();


    private void updateComboBox() {
        //update parser selector
        if (!textField_file_path.getText().isEmpty()) {
            File file = new File(textField_file_path.getText());
            if (file.exists()) {
                String[] tmp = file.getName().split("\\.");
                Set<AbstractMusicParser<?, ? extends AbstractNoteMessage>> parsers = SuffixDealerRegistry.getSuffixParsers(tmp[tmp.length - 1]);
                if (parsers != null && !parsers.isEmpty()) {
                    String prevSelectItem = (String) comboBox_parser.getSelectedItem();
                    comboBox_parser.removeAllItems();
                    parserNameMap.clear();

                    for (AbstractMusicParser<?, ? extends AbstractNoteMessage> parser : parsers) {
                        parserNameMap.put(parser.getClass().getSimpleName(), parser);
                        comboBox_parser.addItem(parser.getClass().getSimpleName());
                    }
                    if (prevSelectItem != null && !prevSelectItem.isEmpty()) {
                        if (parserNameMap.containsKey(prevSelectItem)) {
                            comboBox_parser.setSelectedItem(prevSelectItem);
                        }
                    }
                }

            }
        }
        //update player selector
        if (comboBox_parser.getSelectedItem() != null) {
            Class<? extends AbstractNoteMessage> noteType = parserNameMap.get(comboBox_parser.getSelectedItem()).getNoteType();
            Set<AbstractMusicPlayer> players = SuffixDealerRegistry.getNotePlayers(noteType);
            if (players != null && !players.isEmpty()) {
                String prevSelectItem = (String) comboBox_player.getSelectedItem();
                playerNameMap.clear();
                comboBox_player.removeAllItems();
                for (AbstractMusicPlayer player : players) {
                    playerNameMap.put(player.getClass().getSimpleName(), player);
                    comboBox_player.addItem(player.getClass().getSimpleName());
                }
                if (prevSelectItem != null && !prevSelectItem.isEmpty()) {
                    if (playerNameMap.containsKey(prevSelectItem)) {
                        comboBox_player.setSelectedItem(prevSelectItem);
                    }
                }
            }
        }
    }


    public static ControllerFrame instance;

    public static void init() {
        instance = new ControllerFrame();
        instance.setup();
    }


}
