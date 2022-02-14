package me.ironblock.automusicplayer.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class KeyMapUtils {
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private static final Map<String, Integer> NOTE_NAME_NOTE_INDEX_MAP = new HashMap<>();
    private static final Map<Integer, String> NOTE_INDEX_NOTE_NAME_MAP = new HashMap<>();

    public static final Logger LOGGER = LogManager.getLogger(KeyMapUtils.class);
    static {
        for (int i = 0; i < NOTE_NAMES.length; i++) {
            NOTE_NAME_NOTE_INDEX_MAP.put(NOTE_NAMES[i], i);
        }
        for (int i = 0; i < NOTE_NAMES.length; i++) {
            NOTE_INDEX_NOTE_NAME_MAP.put(i, NOTE_NAMES[i]);
        }
    }


    /**
     * Get the vk code of the key on the keyboard
     *
     * @param key the specific key
     * @return vk_code
     */
    public static int getVKCodeFromKeyChar(String key) {
        if (key.length() == 1) {
            return KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
        } else {
            try {
                Field field = KeyEvent.class.getDeclaredField("VK_" + key.toUpperCase());
                return (int) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.warn("The key "+ key + " isn't a key on the keyboard",e);
            }
        }
        LOGGER.info("Failed to parse" + key);
        return 0;
    }

    /**
     * Get note index with the note name
     *
     * @param noteName the note name
     * @return note index (0~11)
     */
    public static int getNoteIndexFromNoteName(String noteName) {
        return NOTE_NAME_NOTE_INDEX_MAP.get(noteName);
    }

    /**
     * Get the note name of note index
     */
    public static String getNoteNameFromNoteIndex(int index) {
        return NOTE_INDEX_NOTE_NAME_MAP.get(index);
    }

    /**
     * Get the full note name with the note index
     * For example getFullNameFromNoteIndex(12) will return "C1";
     */
    public static String getFullNameFromNoteIndex(int index) {
        int octave = index / 12;
        int note = index % 12;
        return getNoteNameFromNoteIndex(note) + octave;

    }
}
