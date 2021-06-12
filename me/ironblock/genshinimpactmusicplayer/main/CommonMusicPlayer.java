//package me.ironblock.genshinimpactmusicplayer.main;
//
//import me.ironblock.genshinimpactmusicplayer.ui.MainFrame;
//
//import java.awt.*;
//import java.util.HashMap;
//import java.util.Map;
//
//public class CommonMusicPlayer {
//    Robot robot;
//
//    {
//        try {
//            robot = new Robot();
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private Map<Character, Integer> pressTime = new HashMap<>();
//
//    public void playMusic(CommonMusic music) {
//        System.out.println("开始播放");
//        int current = 0;
//        while (current < music.length) {
//            updateKeyMap();
//            for (KeyMessage nextTickKeyMessage : music.getNextTickKeyMessages()) {
//                pressTime.put((char) nextTickKeyMessage.key, 0);
//                System.out.println("按下了" + (char) nextTickKeyMessage.key);
//                robot.keyPress(getKeyCode((char) nextTickKeyMessage.key));
//
//            }
//            System.out.println("下一tick");
//
//            try {
//                Thread.sleep(Long.parseLong(MainFrame.instance.speed_tf.getText()));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void updateKeyMap() {
//        Map<Character,Integer> tmp = new HashMap<>();
//        for (Map.Entry<Character, Integer> characterIntegerEntry : pressTime.entrySet()) {
//            if(characterIntegerEntry.getValue()<=0){
//                robot.keyRelease(getKeyCode(characterIntegerEntry.getKey()));
//                System.out.println("松开了"+characterIntegerEntry.getKey());
//            }else{
//                tmp.put(characterIntegerEntry.getKey(),characterIntegerEntry.getValue()-1);
//            }
//        }
//        pressTime = tmp;
//    }
//
//    private int getKeyCode(char chara) {
////        KeyStroke keyStroke = KeyStroke.getKeyStroke(chara);
////        return keyStroke.getKeyCode();
//        return Character.toUpperCase(chara);
////        return KeyMap.charMap.get((char) ((int)chara + 32));
//    }
//}
