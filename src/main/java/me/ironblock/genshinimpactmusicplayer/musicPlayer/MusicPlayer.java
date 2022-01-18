package me.ironblock.genshinimpactmusicplayer.musicPlayer;

import me.ironblock.genshinimpactmusicplayer.music.KeyActionMusic;
import me.ironblock.genshinimpactmusicplayer.note.KeyAction;
import me.ironblock.genshinimpactmusicplayer.ui.ControllerFrame;
import me.ironblock.genshinimpactmusicplayer.utils.Timer;

import java.awt.*;
import java.util.Set;

/**
 * @author :Iron__Block
 * @Date :2022/1/16 0:09
 */
public class MusicPlayer {
    //计时器
    private final Timer timer = new Timer(20);
    private final Timer updateTimer = new Timer(1);
    //机器人
    private Robot robot;
    /**
     * 执行音乐播放的线程
     */
    protected Thread musicPlayerThread;
    /**
     * 正在播放的歌曲
     */
    protected KeyActionMusic keyActionMusicPlayed;
    /**
     * 播放歌曲的速度(tps)
     */
    protected int speed;
    //与线程执行有关的变量
    private boolean isPlaying, paused;
    private int speedTimer = 0;

    /**
     * 机器人得在构造器里创建
     */
    public MusicPlayer() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置播放速度(tps)
     *
     * @param speedIn 要设置的播放速度(tps)
     */
    public void setSpeed(int speedIn) {
        speed = speedIn;
        timer.setTps(speedIn);
    }

    /**
     * 播放音符的具体方法
     *
     * @param note 要播放的音符
     */
    public void playNote(KeyAction note) {
        if (note.getCommand()) {
            robot.keyPress(note.getKey());
        } else {
            robot.keyRelease(note.getKey());
        }
    }

    /**
     * 开始演奏音乐
     *
     * @param keyActionMusic 要演奏的音乐
     */
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


    /**
     * 播放器线程
     */
    protected void playMusicPlayerThread() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        isPlaying = false;
        updateInfo(true);
    }

    /**
     * 更新ControllerFrame的TextArea_Info
     *
     * @param forceUpdate 是否强制更新
     */
    private void updateInfo(boolean forceUpdate) {
        speedTimer++;
        if (updateTimer.update() || forceUpdate) {
            ControllerFrame.instance.updateInfoTextField(speedTimer, keyActionMusicPlayed.getCurrentTick(), keyActionMusicPlayed.length, speed, keyActionMusicPlayed.isMusicFinished() || !isPlaying);
            speedTimer = 0;
        }
    }


    /**
     * 暂停
     */
    public void pause() {
        paused = true;
    }

    /**
     * 继续
     */
    public void resume() {
        paused = false;
    }

    /**
     * 停止
     */
    public void stop() {
        isPlaying = false;
    }

    /**
     * 切换暂停或继续
     */
    public void switchPause() {
        if (paused) {
            resume();
            System.out.println("继续!");
        } else {
            pause();
            System.out.println("暂停");
        }
    }

    public int getSpeed() {
        return speed;
    }

    public KeyActionMusic getKeyActionMusicPlayed() {
        return keyActionMusicPlayed;
    }

}
