package me.ironblock.genshinimpactmusicplayer.musicPlayer;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.music.AbstractMusic;
import me.ironblock.genshinimpactmusicplayer.note.AbstractNoteMessage;
import me.ironblock.genshinimpactmusicplayer.ui.ControllerFrame;
import me.ironblock.genshinimpactmusicplayer.utils.Timer;

/**
 * 抽象音乐播放器类
 *
 * @param <T> 这个播放器播放音乐的类型
 * @param <K> 这个播放器播放音符的类型
 */
public abstract class AbstractMusicPlayer<T extends AbstractMusic<K>, K extends AbstractNoteMessage> {
    /**
     * 执行音乐播放的线程
     */
    protected Thread musicPlayerThread;
    /**
     * 正在播放的歌曲
     */
    protected T musicPlayed;
    /**
     * 播放歌曲的速度(tps)
     */
    protected int speed;
    protected KeyMap activeKeyMap;
    //与线程执行有关的变量
    private boolean isPlaying, paused;
    //计时器
    private final Timer timer = new Timer(20);

    public void setSpeed(int speedIn) {
        speed = speedIn;
        timer.setTps(speed);
    }

    public abstract void playNote(K note);

    public void playMusic(T music) throws Exception {

        if (musicPlayed!=null&&musicPlayed.equals(music)) {
            if (isPlaying) {
                music.reset();
            } else {

                musicPlayerThread = new Thread(this::playMusicPlayerThread, "MusicPlayerThread");
                musicPlayerThread.start();
            }
        } else {
            musicPlayed = music;
            musicPlayerThread = new Thread(this::playMusicPlayerThread, "MusicPlayerThread");
            musicPlayerThread.start();
        }
        isPlaying = true;

    }

    /**
     * 播放器在每tick之前做的事情
     */
    public void preTick() {
    }

    /**
     * 播放器线程
     */
    protected void playMusicPlayerThread() {
        while (!musicPlayed.isMusicFinished() && isPlaying) {
            if (!paused) {
                if (timer.update()) {
                    preTick();
                    updateInfo(false);
                    for (K k : musicPlayed.getNextTickNote()) {
                        playNote(k);
                    }
                }
            }
        }
        isPlaying = false;
        updateInfo(true);
    }
    private final Timer updateTimer = new Timer(1);
    private int speedTimer = 0;
    private void updateInfo(boolean forceUpdate){
        speedTimer++;
        if (updateTimer.update()||forceUpdate){
            ControllerFrame.instance.updateInfoTextField(speedTimer,musicPlayed.getCurrentTick(),musicPlayed.length,speed,musicPlayed.isMusicFinished()||!isPlaying);
            speedTimer=0;
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
        if (paused){
            resume();
            System.out.println("继续!");
        }
        else{
            pause();
            System.out.println("暂停");
        }
    }

    public KeyMap getActiveKeyMap() {
        return activeKeyMap;
    }

    public void setActiveKeyMap(KeyMap activeKeyMap) {
        this.activeKeyMap = activeKeyMap;
    }
}
