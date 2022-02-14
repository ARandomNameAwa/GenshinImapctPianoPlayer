package me.ironblock.automusicplayer.music.player;

import me.ironblock.automusicplayer.note.KeyAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

/**
 * @author :Iron__Block
 * @Date :2022/1/16 0:09
 */
public class RobotMusicPlayer extends AbstractMusicPlayer {
    public static final Logger LOGGER = LogManager.getLogger(RobotMusicPlayer.class);

    private Robot robot;
    public RobotMusicPlayer() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            LOGGER.error("Failed to init awt robot.....How could this happen.",e);
        }
    }


    @Override
    public void playNote(KeyAction note) {
        if (note.getCommand()) {
            robot.keyPress(note.getKey());
        } else {
            robot.keyRelease(note.getKey());
        }
    }
}
