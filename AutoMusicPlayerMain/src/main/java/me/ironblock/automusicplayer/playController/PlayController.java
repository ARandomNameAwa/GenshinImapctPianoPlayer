package me.ironblock.automusicplayer.playController;

import me.ironblock.automusicplayer.keyMap.KeyMap;
import me.ironblock.automusicplayer.music.KeyActionMusic;
import me.ironblock.automusicplayer.music.TrackMusic;
import me.ironblock.automusicplayer.music.TuneStep;
import me.ironblock.automusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.automusicplayer.musicPlayer.MusicPlayer;

import java.io.InputStream;


/**
 * 演奏控制器
 */
public class PlayController {
    private final MusicPlayer player = new MusicPlayer();
    private TrackMusic trackMusic;
    private KeyMap activeKeyMap;
    private String currentMusicName = "";

    public void prepareMusicPlayed(InputStream file, AbstractMusicParser parser, String name) {
        if (!currentMusicName.equals(name)) {
            trackMusic = parser.parseMusic(file);
            currentMusicName = name;
        }
    }

    /**
     * 开始演奏
     */
    public void startPlay(TuneStep tune) {
        KeyActionMusic keyActionMusic = KeyActionMusic.getFromTrackMusic(trackMusic, activeKeyMap, tune);
        this.player.playMusic(keyActionMusic);
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        player.stop();
    }

    /**
     * 切换播放状态
     */
    public void switchPause() {
        player.switchPause();
    }

    public void setSpeed(double speed) {
        player.setSpeed((int) (speed * trackMusic.tpsReal));
    }

    /**
     * 自动调音
     *
     * @param minPitch        调音扫描范围最小的八度
     * @param maxPitch        调音扫描范围最大的八度
     * @param tracksPitchSame 不同轨道的升降八度是否一致
     * @return 如果tracksPitchSame为true 返回一个元素的Map,这个元素的key为114514,内容为pitch*12+tune 否则返回一个的Map
     * 其中的元素的key为轨道编号,value为轨道的pitch,key为114514的value为所有轨道的tune
     */
    public TuneStep autoTune(int minPitch, int maxPitch, boolean tracksPitchSame) {
        return trackMusic.autoTune(activeKeyMap, minPitch, maxPitch, tracksPitchSame);
    }

    public void setActiveKeyMap(KeyMap activeKeyMap) {
        this.activeKeyMap = activeKeyMap;
    }

    public void jumpToTick(int tick) {
        player.getKeyActionMusicPlayed().jumpToTick(tick);
    }

    public int getTotalTick() {
        return (int) player.getKeyActionMusicPlayed().length;
    }

    public int getSpeed() {
        return player.getSpeed();
    }

    public boolean isPlaying() {
        return trackMusic != null && player.getKeyActionMusicPlayed() != null;
    }

    public TrackMusic getTrackMusic() {
        return trackMusic;
    }

    public boolean TrackMusicLoaded() {
        return trackMusic != null;
    }

}
