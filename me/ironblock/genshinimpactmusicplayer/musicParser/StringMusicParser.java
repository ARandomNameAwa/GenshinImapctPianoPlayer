package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.music.AbstractMusic;
import me.ironblock.genshinimpactmusicplayer.music.CommonMusic;
import me.ironblock.genshinimpactmusicplayer.note.CommonNoteMessage;
import me.ironblock.genshinimpactmusicplayer.utils.IOUtils;

import java.io.*;
import java.util.*;

public class StringMusicParser extends AbstractMusicParser<String, CommonNoteMessage> {
    @Override
    public AbstractMusic<CommonNoteMessage> parseMusic(String musicIn) throws Exception{
            musicIn = IOUtils.readStringFully(new FileInputStream(musicIn)).toLowerCase(Locale.ROOT);
            CommonMusic music = new CommonMusic();
            long currentTick = 0;

            Stack<Character> stack = new Stack<>();
            boolean enableStack = false;
            for (char c : musicIn.toCharArray()) {

                if (c == ')') {
                    currentTick++;
                    enableStack = false;
                    music.addNoteToTrack(0, currentTick, getKeyMessage(stack.toArray(new Character[0])).toArray(new CommonNoteMessage[0]));
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
                    music.addNoteToTrack(0, currentTick, getKeyMessage(c).toArray(new CommonNoteMessage[0]));
                }

            }
            music.length = currentTick + 1;
            return music;
    }

    private List<CommonNoteMessage> getKeyMessage(Character... c) {
        List<CommonNoteMessage> msg = new ArrayList<>();
        for (Character character : c) {
            CommonNoteMessage message = new CommonNoteMessage();
            message.key = character;
            msg.add(message);
        }
        return msg;
    }



}
