//package me.ironblock.genshinimpactmusicplayer.main;
//
//
//import me.ironblock.genshinimpactmusicplayer.music.Music;
//
//import java.awt.*;
//import java.util.List;
//
//public class MusicPlayer {
//    private Robot robot;
//    public static final int NOTE_ON = 0x90;
//    public static final int NOTE_OFF = 0x80;
//
//
//    private long startSleepTime;
//    public static long OneDividedBySpeed = 100;
//    public static long maxJumpReadTicks = 100;
//    int currentTick = -1;
//    {
//        try {
//            robot = new Robot();
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }
//    }
//    public void playMusic(Music musicToPlay){
//        int jumpReadTimer = 0;
//        while (currentTick< musicToPlay.length){
//            if (checkSleep()) {
//                currentTick++;
//                List<KeyMessage> list = musicToPlay.getNextTickKeyMessages();
//                if (!list.isEmpty()||jumpReadTimer>=maxJumpReadTicks){
//                    for (KeyMessage nextTickKeyMessage : list) {
//                        if (nextTickKeyMessage.command == NOTE_ON){
//                            robot.keyPress(KeyMap.getKey(nextTickKeyMessage));
//                        }else if(nextTickKeyMessage.command==NOTE_OFF){
//                            robot.keyRelease(KeyMap.getKey(nextTickKeyMessage));
//                        }
//                    }
//
//                    jumpReadTimer = 0;
//                }else{
//                    jumpReadTimer++;
//                    continue;
//                }
//
//                sleep();
//            }
//
//        }
//    }
//
//    /**
//     * 检查是否正在睡眠
//     * @return 不在返回true 在返回false
//     */
//    private boolean checkSleep(){
//        return System.currentTimeMillis()-startSleepTime> OneDividedBySpeed;
//    }
//    private void sleep(){
//        startSleepTime = System.currentTimeMillis();
//    }
//}
