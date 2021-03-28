package me.ironblock.genshinimpactmusicplayer.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends Frame {
    private final Label path_label = new Label("midi file path");
    private final Label divideBy_label = new Label("1/tps");
    private final Label speed_label = new Label("speed(1/(ticks/sec))");
    private final TextField path_tf = new TextField("");
    private final TextField divideBy_tf = new TextField("50");
    private final TextField speed_tf = new TextField("20");
    private final Button confirm = new Button("Start");
    private final Button stop = new Button("stop");
    public static MainFrame instance;


    private Thread thread;
    public static void main(String[] args) {
        new MainFrame().init();
    }
    public void init(){
        instance = this;
        this.setBounds(100,100,500,250);
        path_label.setBounds(20,30,250,20);
        divideBy_label.setBounds(20,60,250,20);
        speed_label.setBounds(20,90,250,20);
        path_tf.setBounds(270,30,120,20);
        divideBy_tf.setBounds(270,60,120,20);
        speed_tf.setBounds(270,90,120,20);
        confirm.setBounds(270,110,50,30);
        stop.setBounds(350,110,50,30);
        this.add(path_label);
        this.add(divideBy_label);
        this.add(speed_label);
        this.add(path_tf);
        this.add(divideBy_tf);
        this.add(speed_tf);
        this.add(confirm);
        this.add(stop);
        this.setLayout(null);
        this.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             *
             * @param e
             */
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        confirm.addActionListener(e -> {
            start();
        });
        stop.addActionListener(e->{
            stop();
        });
        this.setVisible(true);


    }
    public void start(){
        if (thread==null|| !thread.isAlive()){
            MusicPlayer.sleepTime = Long.parseLong(speed_tf.getText());
            MidiParser.TIMEDIVIDEBY = Integer.parseInt(divideBy_tf.getText());
            thread = new Thread(()->{
                try {
                    MidiPlayer.play(path_tf.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            setAsTitle("Start to play in 5 secs");
            thread.start();
        }else{
            setAsTitle("Already playing!");
        }

    }
    public void stop(){
        if (thread==null|| !thread.isAlive()){

        }else{
            thread.stop();
            setAsTitle("Stopped");
        }
    };
    public static void setAsTitle(String msg){
        instance.setTitle(msg);
    }
}
