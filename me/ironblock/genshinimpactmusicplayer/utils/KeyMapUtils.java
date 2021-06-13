package me.ironblock.genshinimpactmusicplayer.utils;


import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyMapUtils {
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#","A", "A#", "B"};
    private static final Map<String, Integer> noteNameNoteIndexMap = new HashMap<>();
    static {
        for (int i = 0; i < NOTE_NAMES.length; i++) {
            noteNameNoteIndexMap.put(NOTE_NAMES[i],i);
        }
    }


    /**
     * 通过键盘上的字符计算出对应的vk_code
     * @param key 键盘上的字符
     * @return 对应的vk_code
     */
    public static int getVKCodeFromKeyChar(char key){
        return KeyEvent.getExtendedKeyCodeForChar(key);
    }

    /**
     * 通过音名计算音名索引
     * @param noteName 音名
     * @return 音名索引
     */
    public static int getNoteIndexFromNoteName(String noteName){
        return noteNameNoteIndexMap.get(noteName);
    }

}
