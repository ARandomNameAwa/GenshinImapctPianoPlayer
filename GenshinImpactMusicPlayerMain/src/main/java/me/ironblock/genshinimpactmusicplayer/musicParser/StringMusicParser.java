package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.music.TrackMusic;
import me.ironblock.genshinimpactmusicplayer.note.NoteInfo;
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
     * @return 解析出的音乐
     */
    @Override
    public TrackMusic parseMusic(InputStream musicStream){
        String musicIn = IOUtils.readStringFully(musicStream).toLowerCase(Locale.ROOT);
        TrackMusic trackMusic = new TrackMusic();
        long currentTick = 0;
        //使用栈来解析括号
        Stack<Character> stack = new Stack<>();
        boolean enableStack = false;
        for (char c : musicIn.toCharArray()) {


            if (c == ')') {
                currentTick++;
                enableStack = false;
                for (Character character : stack) {
                    NoteInfo noteInfo = new NoteInfo(true,KeyMapUtils.getVKCodeFromKeyChar(String.valueOf(character)));
                    trackMusic.putNode(0, (int) (currentTick*9), noteInfo);
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
                NoteInfo noteInfo = new NoteInfo(true,KeyMapUtils.getVKCodeFromKeyChar(String.valueOf(c)));
                trackMusic.putNode(0, (int) (currentTick*9), noteInfo);

            }


        }

        trackMusic.length = currentTick*9 + 1;
        trackMusic.realDuration = ((double) trackMusic.length)/2;
        trackMusic.tpsReal = 25;
        return trackMusic;
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
