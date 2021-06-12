//package me.ironblock.genshinimpactmusicplayer.ui;
//
//import me.ironblock.genshinimpactmusicplayer.main.CommonMusicPlayer;
//import me.ironblock.genshinimpactmusicplayer.main.LetterMusicParser;
//import me.ironblock.genshinimpactmusicplayer.main.MidiPlayer;
//import me.ironblock.genshinimpactmusicplayer.main.MusicPlayer;
//
//import java.awt.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.io.*;
//
//public class MainFrame extends Frame {
//    private final Label path_label = new Label("midi file path");
//    private final Label speed_label = new Label("speed(tps)");
//    private final TextField path_tf = new TextField("");
//    public final TextField speed_tf = new TextField("100");
//    private final Button confirm = new Button("Start");
//    private final Button stop = new Button("stop");
//    public static MainFrame instance;
//
//
//    private Thread thread;
//    public static void main(String[] args) {
//        new MainFrame().init();
//    }
//    public void init(){
//        instance = this;
//        this.setBounds(100,100,500,250);
//        path_label.setBounds(20,30,250,20);
//        speed_label.setBounds(20,90,250,20);
//        path_tf.setBounds(270,30,120,20);
//        speed_tf.setBounds(270,90,120,20);
//        confirm.setBounds(270,110,50,30);
//        stop.setBounds(350,110,50,30);
//        this.add(path_label);
//        this.add(speed_label);
//        this.add(path_tf);
//        this.add(speed_tf);
//        this.add(confirm);
//        this.add(stop);
//        this.setLayout(null);
//        this.addWindowListener(new WindowAdapter() {
//            /**
//             * Invoked when a window is in the process of being closed.
//             * The close operation can be overridden at this point.
//             *
//             * @param e
//             */
//            @Override
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });
//        confirm.addActionListener(e -> {
//            start();
//        });
//        stop.addActionListener(e->{
//            stop();
//        });
//        this.setVisible(true);
//
//
//    }
//    public void start(){
//        if (thread==null|| !thread.isAlive()){
//            MusicPlayer.OneDividedBySpeed = 1000/Long.parseLong(speed_tf.getText());
//            if (path_tf.getText().endsWith(".mid"))
//            thread = new Thread(()->{
//                try {
//                    MidiPlayer.play(path_tf.getText());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//            else
//                thread = new Thread(()->{
//                    try {
//                        Thread.sleep(5000);
//                        new CommonMusicPlayer().playMusic(new LetterMusicParser().parseMusic(getString(path_tf.getText())));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//            setAsTitle("Start to play in 5 secs");
//            thread.start();
//        }else{
//            setAsTitle("Already playing!");
//        }
//
//    }
//    public void stop(){
//        if (thread==null|| !thread.isAlive()){
//
//        }else{
//            thread.stop();
//            setAsTitle("Stopped");
//        }
//    }
//
//    public static void setAsTitle(String msg){
//        instance.setTitle(msg);
//    }
//    private String getString(String path){
//        StringBuilder builder = new StringBuilder();
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
//            String s;
//            while ((s=reader.readLine())!=null){
//                builder.append(s);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return builder.toString();
//    }
//}
