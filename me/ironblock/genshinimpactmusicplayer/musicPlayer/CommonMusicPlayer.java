package me.ironblock.genshinimpactmusicplayer.musicPlayer;

import me.ironblock.genshinimpactmusicplayer.music.CommonMusic;
import me.ironblock.genshinimpactmusicplayer.note.CommonNoteMessage;
import me.ironblock.genshinimpactmusicplayer.utils.KeyMap;

import java.awt.*;
import java.util.HashSet;

public class CommonMusicPlayer extends AbstractMusicPlayer<CommonMusic, CommonNoteMessage>{
    private Robot robot;
    private final HashSet<Character> keysToBeReleased = new HashSet<>();
    @Override
    public void playNote(CommonNoteMessage note) {
        keysToBeReleased.add(note.key);
        robot.keyPress(KeyMap.getVKCodeFromKeyChar(note.key));
    }

    @Override
    public void preTick() {
        if (robot == null){
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }

        for (Character character : keysToBeReleased) {
            robot.keyRelease(KeyMap.getVKCodeFromKeyChar(character));
        }
        keysToBeReleased.clear();
    }
}
