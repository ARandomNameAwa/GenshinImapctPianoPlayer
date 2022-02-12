package me.ironblock.automusicplayer.music.player;

import me.ironblock.automusicplayer.music.KeyActionMusic;
import me.ironblock.automusicplayer.note.KeyAction;
import me.ironblock.automusicplayer.ui.ControllerFrame;
import me.ironblock.automusicplayer.utils.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Set;

/**
 * @author :Iron__Block
 * @Date :2022/2/13 1:13
 */
public abstract class AbstractMusicPlayer {
    public static final Logger LOGGER = LogManager.getLogger(AbstractMusicPlayer.class);
    protected final Timer timer = new Timer(20);
    protected final Timer updateTimer = new Timer(1);
    protected Thread musicPlayerThread;
    /**
     * Music being played
     */
    protected KeyActionMusic keyActionMusicPlayed;
    /**
     * the speed of the music (in tps)
     */
    protected int speed;

    protected boolean isPlaying, paused;


    public abstract void playNote(KeyAction note);

    public void playMusic(KeyActionMusic keyActionMusic) {

        if (keyActionMusicPlayed != null && keyActionMusicPlayed.equals(keyActionMusic)) {
            if (isPlaying) {
                keyActionMusic.reset();
            } else {
                musicPlayerThread = new Thread(this::playMusicPlayerThread, "MusicPlayerThread");
                musicPlayerThread.start();
            }
        } else {
            keyActionMusicPlayed = keyActionMusic;
            musicPlayerThread = new Thread(this::playMusicPlayerThread, "MusicPlayerThread");
            musicPlayerThread.start();
        }
        isPlaying = true;

    }
    protected void playMusicPlayerThread() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.warn("Thread interrupted while sleeping:",e);
        }
        while (!keyActionMusicPlayed.isMusicFinished() && isPlaying) {
            if (!paused) {
                if (timer.update()) {
                    updateInfo(false);
                    Set<KeyAction> set = keyActionMusicPlayed.getNextTickNote();
                    if (set != null) {
                        for (KeyAction k : set) {
                            playNote(k);
                        }
                    }
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                LOGGER.warn("Thread interrupted while sleeping:",e);
            }
        }
        isPlaying = false;
        updateInfo(true);
    }

    /**
     * Update the controller frame
     *
     * @param forceUpdate ignore timer
     */
    private void updateInfo(boolean forceUpdate) {
        if (updateTimer.update() || forceUpdate) {
            ControllerFrame.instance.updateInfoTextField(keyActionMusicPlayed.getCurrentTick(), keyActionMusicPlayed.length, speed, keyActionMusicPlayed.isMusicFinished() || !isPlaying);
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void stop() {
        isPlaying = false;
    }

    public void switchPause() {
        if (paused) {
            resume();
            LOGGER.info("Resume!");
        } else {
            pause();
            LOGGER.info("Pause");
        }
    }

    public int getSpeed() {
        return speed;
    }

    /**
     * Set the speed (in tps)
     *
     * @param speedIn speed (in tps)
     */
    public void setSpeed(int speedIn) {
        speed = speedIn;
        timer.setTps(speedIn);
    }

    public KeyActionMusic getKeyActionMusicPlayed() {
        return keyActionMusicPlayed;
    }

}
