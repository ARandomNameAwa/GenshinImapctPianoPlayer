package me.ironblock.genshinimpactmusicplayer.musicPlayer;

import me.ironblock.genshinimpactmusicplayer.music.CharacterMusic;
import me.ironblock.genshinimpactmusicplayer.note.CharacterNoteMessage;
import me.ironblock.genshinimpactmusicplayer.utils.KeyMapUtils;

import java.awt.*;
import java.util.HashSet;

public class CharacterMusicPlayer extends AbstractMusicPlayer<CharacterMusic, CharacterNoteMessage> {
    private final HashSet<Character> keysToBeReleased = new HashSet<>();
    private Robot robot;

    @Override
    public void playNote(CharacterNoteMessage note) {
        keysToBeReleased.add(note.key);
        robot.keyPress(KeyMapUtils.getVKCodeFromKeyChar(String.valueOf(note.key)));
    }

    /**
     * 更新机器人并松开上一个tick被按下的按键
     */
    @Override
    public void preTick() {
        if (robot == null) {
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }

        for (Character character : keysToBeReleased) {
            robot.keyRelease(KeyMapUtils.getVKCodeFromKeyChar(String.valueOf(character)));
        }
        keysToBeReleased.clear();
    }
}
