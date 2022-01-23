package me.ironblock.automusicplayer.keymap;

import me.ironblock.automusicplayer.note.NoteInfo;
import me.ironblock.automusicplayer.utils.IOUtils;
import me.ironblock.automusicplayer.utils.KeyMapUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class KeyMapLoader {
    private static KeyMapLoader instance;
    private final Map<String, KeyMap> loadedKeyMap = new HashMap<>();

    public static KeyMapLoader getInstance() {
        if (instance == null) {
            instance = new KeyMapLoader();
        }
        return instance;
    }

    /**
     * Load a keyMap
     *
     * @param inputStream keyMap file stream
     * @param name        the name of the keyMap
     */
    public void loadKeyMapFromFile(InputStream inputStream, String name) {
        loadKeyMapFromString(IOUtils.readStringFully(inputStream), name);
    }

    /**
     * Load a keyMap
     *
     * @param fileName the file path of the keyMap file
     */
    public void loadKeyMapFromFile(String fileName) {
        File file = new File(fileName);
        try {
            loadKeyMapFromFile(new FileInputStream(file), file.getName().substring(0, file.getName().lastIndexOf(".")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a keyMap
     *
     * @param text Load from this string
     * @param name the name of the keyMap
     */
    public void loadKeyMapFromString(String text, String name) {
        String[] lines = text.split("\n");
        KeyMap keyMap = new KeyMap();
        int minNote = 0, maxNote = 0;
        int lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            try {
                String[] keyValue = line.split(":");
                keyValue[0] = keyValue[0].toUpperCase();
                keyValue[1] = keyValue[1].toLowerCase(Locale.ROOT);
                int octave, note;
                if (keyValue[0].length() == 2) {
                    octave = Integer.parseInt(String.valueOf(keyValue[0].charAt(1)));
                    note = KeyMapUtils.getNoteIndexFromNoteName(String.valueOf(keyValue[0].charAt(0)));
                } else if (keyValue[0].length() == 3) {
                    octave = Integer.parseInt(String.valueOf(keyValue[0].charAt(2)));
                    note = KeyMapUtils.getNoteIndexFromNoteName(keyValue[0].substring(0, 2));
                } else {
                    throw new IllegalArgumentException("Failed to parse the key Map");
                }
                NoteInfo noteInfo = new NoteInfo(octave, note);
                keyMap.noteKeyMap.put(noteInfo, KeyMapUtils.getVKCodeFromKeyChar(keyValue[1]));
                if (minNote == 0 || noteInfo.getNoteIndex() < minNote) {
                    minNote = noteInfo.getNoteIndex();
                }
                if (maxNote == 0 || noteInfo.getNoteIndex() > maxNote) {
                    maxNote = noteInfo.getNoteIndex();
                }

            } catch (Exception e) {
                System.out.println("Failed to parse the keyMap file at line " + lineNumber);
                e.printStackTrace();
            }
        }
        keyMap.minNoteIndex = minNote;
        keyMap.maxNoteIndex = maxNote;
        keyMap.minNoteOctave = minNote / 12;
        keyMap.maxNoteOctave = maxNote / 12;
        loadedKeyMap.put(name, keyMap);
    }

    /**
     * Get a loaded keyMap from a name
     *
     * @param name the name of the keyMap
     * @return if the specific keyMap was loaded,return the keyMap,or return null
     */
    public KeyMap getLoadedKeyMap(String name) {
        return loadedKeyMap.get(name);
    }

    /**
     * Get the names of all the loaded keyMaps
     *
     * @return The names of all the loaded keyMaps
     */
    public Set<String> getAllLoadedMapName() {
        return loadedKeyMap.keySet();
    }
}
