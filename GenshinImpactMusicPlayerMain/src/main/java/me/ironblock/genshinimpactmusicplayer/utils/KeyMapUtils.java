package me.ironblock.genshinimpactmusicplayer.utils;


import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class KeyMapUtils {
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private static final Map<String, Integer> noteNameNoteIndexMap = new HashMap<>();
    private static final Map<Integer, String> noteIndexNoteNameMap = new HashMap<>();

    static {
        for (int i = 0; i < NOTE_NAMES.length; i++) {
            noteNameNoteIndexMap.put(NOTE_NAMES[i], i);
        }
        for (int i = 0; i < NOTE_NAMES.length; i++) {
            noteIndexNoteNameMap.put(i, NOTE_NAMES[i]);
        }
    }


    /**
     * 通过键盘上的字符计算出对应的vk_code
     *
     * @param key 键盘上的字符
     * @return 对应的vk_code
     */
    public static int getVKCodeFromKeyChar(String key) {
        if (key.length() == 1) {
            return KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
        } else {
            try {
                Field field = KeyEvent.class.getDeclaredField("VK_" + key.toUpperCase());
                return (int) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println("解析" + key + "时出错");
        return 0;
    }

    /**
     * 通过音名计算音名索引
     *
     * @param noteName 音名
     * @return 音名索引
     */
    public static int getNoteIndexFromNoteName(String noteName) {
        return noteNameNoteIndexMap.get(noteName);
    }

    /**
     * 通过索引计算音名
     */
    public static String getNoteNameFromNoteIndex(int index) {
        return noteIndexNoteNameMap.get(index);
    }

    /**
     * 通过noteIndex计算音名+第几个八度
     */
    public static String getFullNameFromNoteIndex(int index) {
        int octave = index / 12;
        int note = index % 12;
        return getNoteNameFromNoteIndex(note) + octave;

    }
}
