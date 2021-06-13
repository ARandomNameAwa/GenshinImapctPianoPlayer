package me.ironblock.genshinimpactmusicplayer.utils;

import me.ironblock.genshinimpactmusicplayer.note.MidiNoteMessage;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import static java.awt.event.KeyEvent.*;

public class KeyMapUtils {
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#","A", "A#", "B"};
    private static final Map<String, Integer> noteNameNoteIndexMap = new HashMap<>();
    private static final Map<Integer,Integer> keymap = new HashMap<>();
    private static final Map<Integer,Integer> noteNormalizeMap = new HashMap<>();
    static {
        for (int i = 0; i < NOTE_NAMES.length; i++) {
            noteNameNoteIndexMap.put(NOTE_NAMES[i],i);
        }
        keymap.put(0, VK_Q);
        keymap.put(1,VK_W);
        keymap.put(2,VK_E);
        keymap.put(3,VK_R);
        keymap.put(4,VK_T);
        keymap.put(5,VK_Y);
        keymap.put(6,VK_U);
        keymap.put(7,VK_A);
        keymap.put(8,VK_S);
        keymap.put(9,VK_D);
        keymap.put(10,VK_F);
        keymap.put(11,VK_G);
        keymap.put(12,VK_H);
        keymap.put(13,VK_J);
        keymap.put(14,VK_Z);
        keymap.put(15,VK_X);
        keymap.put(16,VK_C);
        keymap.put(17,VK_V);
        keymap.put(18,VK_B);
        keymap.put(19,VK_N);
        keymap.put(20,VK_M);
        noteNormalizeMap.put(0,0);//C->C
        noteNormalizeMap.put(1,0);//C#->C
        noteNormalizeMap.put(2,1);//D->D
        noteNormalizeMap.put(3,1);//D#->D
        noteNormalizeMap.put(4,2);
        noteNormalizeMap.put(5,3);
        noteNormalizeMap.put(6,3);
        noteNormalizeMap.put(7,4);
        noteNormalizeMap.put(8,4);
        noteNormalizeMap.put(9,5);
        noteNormalizeMap.put(10,5);
        noteNormalizeMap.put(11,6);
    }

    /**
     * 通过一个音符信息计算出所对应键盘上键的vk_code
     * @param keyMessage 音符信息
     * @return vk_code
     */
    public static int getVKCodeFromNoteOctaveAndName(MidiNoteMessage keyMessage){
        int note = keyMessage.note;
        int octave = keyMessage.octave-3;


        if (octave<0){
            octave = 0;
        }else if(octave>2){
            octave = 2;
        }
        note = noteNormalizeMap.get(note);

        int keyPos = (2-octave)*7+note;
        return keymap.get(keyPos);
    }

    /**
     * 通过键盘上的字符计算出对应的vk_code
     * @param key 键盘上的字符
     * @return 对应的vk_code
     */
    public static int getVKCodeFromKeyChar(char key){
        return KeyEvent.getExtendedKeyCodeForChar(key);
    }
    public static int getNoteIndexFromNoteName(String noteName){
        return noteNameNoteIndexMap.get(noteName);
    }

}
