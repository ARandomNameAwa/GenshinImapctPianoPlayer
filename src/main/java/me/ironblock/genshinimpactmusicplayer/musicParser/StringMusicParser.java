package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.music.Music;
import me.ironblock.genshinimpactmusicplayer.note.KeyAction;
import me.ironblock.genshinimpactmusicplayer.utils.IOUtils;
import me.ironblock.genshinimpactmusicplayer.utils.KeyMapUtils;

import java.io.InputStream;
import java.util.*;

/**
 * 字符串音乐解析器
 */
public class StringMusicParser extends AbstractMusicParser{
    /**
     * 解析字符串音乐
     *
     * @param musicStream 文件流
     * @param keyMap 使用的keyMap
     * @return 解析出的音乐
     */
    @Override
    public Music parseMusic(InputStream musicStream, KeyMap keyMap){
        String musicIn = IOUtils.readStringFully(musicStream).toLowerCase(Locale.ROOT);
        Music music = new Music();
        long currentTick = 0;
        //使用栈来解析括号
        Stack<Character> stack = new Stack<>();
        boolean enableStack = false;
        List<Character> pressedChars = new ArrayList<>();
        for (char c : musicIn.toCharArray()) {


            for (Character pressedChar : pressedChars) {
                KeyAction action = new KeyAction(false,KeyMapUtils.getVKCodeFromKeyChar(String.valueOf(pressedChar)));
                music.addNoteToTick(currentTick*2+1, action);
            }
            pressedChars.clear();

            if (c == ')') {
                currentTick++;
                enableStack = false;
                for (Character character : stack) {
                    pressedChars.add(character);
                    KeyAction action = new KeyAction(true,KeyMapUtils.getVKCodeFromKeyChar(String.valueOf(character)));
                    music.addNoteToTick(currentTick*2, action);
                }
                stack.clear();
                continue;

            }
            if (enableStack) {
                if (c != '\n' && c != ' ') {
                    stack.push(c);
                    continue;
                }
            }
            if (c == '(') {
                enableStack = true;
                continue;
            }
            if (c != '\n'){
                currentTick++;
            }
            if (c != '\n' && c != ' ') {
                pressedChars.add(c);
                KeyAction action = new KeyAction(true,KeyMapUtils.getVKCodeFromKeyChar(String.valueOf(c)));
                music.addNoteToTick( currentTick*2,action);
            }


        }
        music.length = currentTick*2 + 1;
        music.realDuration = music.length;
        music.tpsReal = 5;
        return music;
    }

    @Override
    public String getMusicFileSuffix() {
        return "txt";
    }

    @Override
    public String getMusicFileTypeName() {
        return "PressKey";
    }


}
