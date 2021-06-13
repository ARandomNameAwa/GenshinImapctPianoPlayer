package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.music.AbstractMusic;
import me.ironblock.genshinimpactmusicplayer.music.CharacterMusic;
import me.ironblock.genshinimpactmusicplayer.note.CharacterNoteMessage;
import me.ironblock.genshinimpactmusicplayer.utils.IOUtils;

import java.io.*;
import java.util.*;

/**
 * 字符串音乐解析器
 */
public class StringMusicParser extends AbstractMusicParser<String, CharacterNoteMessage> {
    /**
     * 解析字符串音乐
     * @param musicIn 文件路径
     * @return 解析出的音乐
     * @throws Exception 抛出的异常
     */
    @Override
    public AbstractMusic<CharacterNoteMessage> parseMusic(String musicIn) throws Exception{
            musicIn = IOUtils.readStringFully(new FileInputStream(musicIn)).toLowerCase(Locale.ROOT);
            CharacterMusic music = new CharacterMusic();
            long currentTick = 0;
            //使用栈来解析括号
            Stack<Character> stack = new Stack<>();
            boolean enableStack = false;
            for (char c : musicIn.toCharArray()) {

                if (c == ')') {
                    currentTick++;
                    enableStack = false;
                    music.addNoteToTrack(0, currentTick, getKeyMessage(stack.toArray(new Character[0])).toArray(new CharacterNoteMessage[0]));
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
                if (c != '\n' && c != ' ') {
                    currentTick++;
                    music.addNoteToTrack(0, currentTick, getKeyMessage(c).toArray(new CharacterNoteMessage[0]));
                }

            }
            music.length = currentTick + 1;
            return music;
    }

    /**
     * 用若干个字符构造出若干个音符
     * @param c 字符
     * @return 构造出的音符
     */
    private List<CharacterNoteMessage> getKeyMessage(Character... c) {
        List<CharacterNoteMessage> msg = new ArrayList<>();
        for (Character character : c) {
            CharacterNoteMessage message = new CharacterNoteMessage();
            message.key = character;
            msg.add(message);
        }
        return msg;
    }



}
