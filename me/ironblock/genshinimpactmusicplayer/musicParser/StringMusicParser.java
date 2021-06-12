package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.music.AbstractMusic;
import me.ironblock.genshinimpactmusicplayer.music.CommonMusic;
import me.ironblock.genshinimpactmusicplayer.note.CommonNoteMessage;

import java.io.*;
import java.util.*;

public class StringMusicParser extends AbstractMusicParser<String, CommonNoteMessage> {
    @Override
    public AbstractMusic<CommonNoteMessage> parseMusic(String musicIn) {
        musicIn = readFile(musicIn).toLowerCase(Locale.ROOT);
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

    private String readFile(String filePath){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String tmp;
            StringBuilder sb = new StringBuilder();
            while ((tmp=reader.readLine())!=null){
                sb.append(tmp).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
