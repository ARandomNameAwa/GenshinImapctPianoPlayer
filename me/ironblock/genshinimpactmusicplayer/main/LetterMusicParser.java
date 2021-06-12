//package me.ironblock.genshinimpactmusicplayer.main;
//
//import java.util.*;
//
//public class LetterMusicParser {
//    public CommonMusic parseMusic(String musicIn) {
//        musicIn = musicIn.toLowerCase(Locale.ROOT);
//        CommonMusic music = new CommonMusic();
//        CommonMusicTrack track = new CommonMusicTrack();
//        long currentTick = 0;
//
//        Stack<Character> stack = new Stack<>();
//        boolean enableStack = false;
//        for (char c : musicIn.toCharArray()) {
//            currentTick++;
//            if (c==')'){
//                enableStack = false;
//                track.noteMap.put(currentTick,getKeyMessage(stack.toArray(new Character[0])));
//                System.out.println(Arrays.toString(stack.toArray(new Character[0])));
//                stack.clear();
//                continue;
//
//            }
//            if (enableStack){
//                if (c!='\n'&&c!=' '){
//                    stack.push(c);
//                    continue;
//                }
//            }
//            if (c=='('){
//                enableStack =true;
//                continue;
//            }
//            if (c != '\n' && c != ' '){
//                track.noteMap.put(currentTick,getKeyMessage(c));
//                System.out.println(c);
//            }
//
//            currentTick++;
//
////            track.noteMap.put(currentTick, getKeyMessage(c));
//        }
//        music.musicTrackList.add(track);
//        music.length = currentTick+1;
//        return music;
//    }
//
//
//    private List<KeyMessage> getKeyMessage(Character... c) {
//        List<KeyMessage> msg = new ArrayList<>();
//        for (Character character : c) {
//            for (int i = 0; i < KeyMap.chars.length; i++) {
//                for (int j = 0; j < KeyMap.chars[i].length; j++) {
//                    if (KeyMap.chars[i][j] == character) {
//                        KeyMessage message = new KeyMessage();
//                        message.key = character;
//                        message.octave = i;
//                        message.note = j;
//                        msg.add(message);
//                    }
//                }
//            }
//        }
//        return msg;
//    }
//}
