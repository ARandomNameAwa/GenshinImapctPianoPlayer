package me.ironblock.genshinimpactmusicplayer.main;


import java.awt.*;
import java.util.List;

public class MusicPlayer {
    private Robot robot;
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;

    private long lastSleepTickThanUsual = 0;

    public static long sleepTime = 100;
    int currentTick = -1;
    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    public void playMusic(Music musicToPlay){
        while (currentTick< musicToPlay.length){
            long start = System.currentTimeMillis();
            currentTick++;
            Timer.start();
            List<KeyMessage> list = musicToPlay.getNextTickKeyMessages();
            long i = Timer.end();
            if (i>50){
                System.out.println("获取音符:"+i +",一共"+list.size()+"个");
            }
            Timer.start();
            for (KeyMessage nextTickKeyMessage : list) {
                if (nextTickKeyMessage.command == NOTE_ON){
                    robot.keyPress(KeyMap.getKey(nextTickKeyMessage));
                }else if(nextTickKeyMessage.command==NOTE_OFF){
                    robot.keyRelease(KeyMap.getKey(nextTickKeyMessage));
                }
            }
            long end = Timer.end();
            if(end>50){
                System.out.println("演奏音符"+(end)+",一共"+list.size()+"个");
            }
            try {
                Timer.start();
                if (lastSleepTickThanUsual<sleepTime){
                    Thread.sleep(sleepTime-lastSleepTickThanUsual);
                }

                long time = Timer.end();
                //呜呜呜我的电脑不好只能这么搞让它不那么卡
                if (time>sleepTime){
                    if (time>2*sleepTime)
                    System.out.println("明明应该sleep"+(sleepTime-lastSleepTickThanUsual)+"毫秒,但是sleep了"+time);
                    lastSleepTickThanUsual=time-sleepTime;
                }else{
                    lastSleepTickThanUsual=0;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
