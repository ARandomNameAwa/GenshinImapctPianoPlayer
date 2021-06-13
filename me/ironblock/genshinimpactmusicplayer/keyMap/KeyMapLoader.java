package me.ironblock.genshinimpactmusicplayer.keyMap;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.ironblock.genshinimpactmusicplayer.utils.IOUtils;
import me.ironblock.genshinimpactmusicplayer.utils.KeyMapUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeyMapLoader {
    private final Map<String, KeyMap> loadedKeyMap = new HashMap<>();
    public KeyMap loadKeyMapFromFile(InputStream inputStream,String name){

        return loadKeyMapFromString(IOUtils.readStringFully(inputStream),name);
    }
    public KeyMap loadKeyMapFromFile(String fileName){
        File file = new File(fileName);
        try {
            loadKeyMapFromFile(new FileInputStream(file), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyMap loadKeyMapFromString(String text, String name){
        String[] lines = text.split("\n");
        KeyMap keyMap = new KeyMap();
        int minNote = 0,maxNote = 0;
        int lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            try {
                String[] keyValue = line.split(":");
                int octave,note;
                if (keyValue[0].length()==2){
                    octave = Integer.parseInt(String.valueOf(keyValue[0].charAt(1)));
                    note = KeyMapUtils.getNoteIndexFromNoteName(String.valueOf(keyValue[0].charAt(0)));
                }else if (keyValue[0].length()==3){
                    octave = Integer.parseInt(String.valueOf(keyValue[0].charAt(2)));
                    note = KeyMapUtils.getNoteIndexFromNoteName(keyValue[0].substring(0,2));
                }else{
                    throw new IllegalArgumentException("音符写法无法解析");
                }
                KeyMap.NoteInfo noteInfo = new KeyMap.NoteInfo(octave,note);
                keyMap.noteKeyMap.put(noteInfo, KeyMapUtils.getVKCodeFromKeyChar(keyValue[1].charAt(0)));
                if (minNote==0||noteInfo.getNoteIndex()<minNote){
                    minNote = noteInfo.getNoteIndex();
                }
                if (maxNote==0||noteInfo.getNoteIndex()>maxNote){
                    maxNote = noteInfo.getNoteIndex();
                }

            } catch (Exception e) {
                System.out.println("在解析KeyMap第"+lineNumber+"时出错");
                e.printStackTrace();
            }
        }
        keyMap.minNoteIndex = minNote;
        keyMap.maxNoteIndex = maxNote;
        keyMap.minNoteOctave = minNote/12;
        keyMap.maxNoteOctave = maxNote/12;
        loadedKeyMap.put(name, keyMap);
        return keyMap;
    }
    public KeyMap getLoadedKeyMap(String name){
        return loadedKeyMap.get(name);
    }
    public Set<String> getAllLoadedMapName(){
        return loadedKeyMap.keySet();
    }












    private static KeyMapLoader instance;
    public static KeyMapLoader getInstance() {
        if (instance == null) {
            instance = new KeyMapLoader();
        }
        return instance;
    }
}
