package me.ironblock.automusicplayer.keyMap;

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

/**
 * 键映射文件加载器
 */
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
     * 加载keyMap
     *
     * @param inputStream 从这里加载
     * @param name        加载后的keyMap的名字
     * @return 加载的keyMap
     */
    public KeyMap loadKeyMapFromFile(InputStream inputStream, String name) {

        return loadKeyMapFromString(IOUtils.readStringFully(inputStream), name);
    }

    /**
     * 加载keyMap
     *
     * @param fileName 加载的文件路径
     * @return 加载后的keyMap
     */
    public KeyMap loadKeyMapFromFile(String fileName) {
        File file = new File(fileName);
        try {
            loadKeyMapFromFile(new FileInputStream(file), file.getName().substring(0, file.getName().lastIndexOf(".")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载keyMap
     *
     * @param text 从这个字符串里加载
     * @param name 名字
     * @return 加载后的keyMap
     */
    public KeyMap loadKeyMapFromString(String text, String name) {
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
                    throw new IllegalArgumentException("音符写法无法解析");
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
                System.out.println("在解析KeyMap第" + lineNumber + "时出错");
                e.printStackTrace();
            }
        }
        keyMap.minNoteIndex = minNote;
        keyMap.maxNoteIndex = maxNote;
        keyMap.minNoteOctave = minNote / 12;
        keyMap.maxNoteOctave = maxNote / 12;
        System.out.println("keyMap" + name + "的最低音域是" + minNote + ",最高音域是" + maxNote);
        loadedKeyMap.put(name, keyMap);
        return keyMap;
    }

    /**
     * 通过名字获取加载过keyMap
     *
     * @param name 名字
     * @return 如果加载过这个名字的keyMap, 则返回keyMap, 否则返回null
     */
    public KeyMap getLoadedKeyMap(String name) {
        return loadedKeyMap.get(name);
    }

    /**
     * 获取所有加载过的keyMap的名字
     *
     * @return 所有加载过的keyMap的名字
     */
    public Set<String> getAllLoadedMapName() {
        return loadedKeyMap.keySet();
    }
}
