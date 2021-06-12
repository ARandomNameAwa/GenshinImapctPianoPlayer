package me.ironblock.genshinimpactmusicplayer.ui;

import me.ironblock.genshinimpactmusicplayer.exceptions.ExceptionNeedToBeDisplayed;
import me.ironblock.genshinimpactmusicplayer.musicParser.MidiMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicParser.StringMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.CommonMusicPlayer;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.MidiMusicPlayer;
import me.ironblock.genshinimpactmusicplayer.playController.PlayController;
import me.ironblock.genshinimpactmusicplayer.playController.SuffixDealerRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControllerFrame extends JFrame {
    public static final String programName = "Genshin Impact Music Player";
    public static final String programVersion = "v1.0";
    public static final String programAuthor = "Iron_Block";
    public static final int frameWidth = 650;
    public static final int frameHeight = 250;

    private final JLabel label_file_path = new JLabel("File Path");
    private final JLabel label_speed = new JLabel("Speed");
    private final JLabel label_tps = new JLabel("tps");
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
        button_start.setBounds(20, 140, 85, 50);
        button_pause.setBounds(130, 140, 85, 50);
        button_stop.setBounds(230, 140, 85, 50);
        textField_file_path.setBounds(130, 30, 200, 25);
        textField_speed.setBounds(130, 80, 150, 25);
        label_tps.setBounds(280, 80, 50, 25);
        textArea_info.setBounds(350, 30, 250, 150);
        button_start.addActionListener(e->this.onStartButtonClicked());
        button_pause.addActionListener(e->this.onPauseButtonClicked());
        button_stop.addActionListener(e->this.onStopButtonClicked());
        this.add(label_file_path);
        this.add(label_speed);
        this.add(label_tps);
        this.add(textField_file_path);
        this.add(textArea_info);
        this.add(textField_speed);
        this.add(button_start);
        this.add(button_pause);
        this.add(button_stop);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        super.setTitle(programName + " " + programVersion + " by " + programAuthor);
        this.setVisible(true);
    }

    public void setTitle(String title) {
        super.setTitle(programName + " " + programVersion + " by " + programAuthor + ":" + title);
    }


    private void onStartButtonClicked(){
        try {
            playController.startPlay(textField_file_path.getText());
            playController.setSpeed(Integer.parseInt(textField_speed.getText()));
        } catch (Exception exceptionNeedToBeDisplayed) {
            exceptionNeedToBeDisplayed.printStackTrace();
            setTitle(exceptionNeedToBeDisplayed.getMessage());
        }
    }

    private void onPauseButtonClicked(){
        playController.setSpeed(Integer.parseInt(textField_speed.getText()));
        playController.switchPause();
        if (button_pause.getText().equals("Pause")){
            button_pause.setText("Resume");
        }else{
            button_pause.setText("Pause");
        }
    }

    private void onStopButtonClicked(){
        playController.stopPlay();
    }

    public void updateInfoTextField(int actualSpeed,long currentTick,long lengthTick,boolean finished){
        if(!finished){
            textArea_info.setText("Actual Speed:"+actualSpeed+"tps\n"+"currentTick:"+currentTick+"/"+lengthTick);
        }else{
            textArea_info.setText("Music Finished");
        }


    }




    public static ControllerFrame instance;

    public static void init() {
        registerMusicDealers();
        instance = new ControllerFrame();
        instance.setup();
    }

    private static void registerMusicDealers(){
        SuffixDealerRegistry.registerSuffixDealer("mid",new MidiMusicPlayer(),new MidiMusicParser());
        SuffixDealerRegistry.registerSuffixDealer("txt",new CommonMusicPlayer(),new StringMusicParser());
    }


}
