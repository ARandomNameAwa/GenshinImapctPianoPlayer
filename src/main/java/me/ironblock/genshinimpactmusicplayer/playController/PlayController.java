package me.ironblock.genshinimpactmusicplayer.playController;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.music.KeyActionMusic;
import me.ironblock.genshinimpactmusicplayer.music.TrackMusic;
import me.ironblock.genshinimpactmusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.MusicPlayer;

import java.io.InputStream;


/**
 * 演奏控制器
 */
public class PlayController {
    private final MusicPlayer player = new MusicPlayer();
    private TrackMusic trackMusic;
    private KeyMap activeKeyMap;

    public void prepareMusicPlayed(InputStream file,AbstractMusicParser parser){
        trackMusic = parser.parseMusic(file);
    }

    /**
     * 开始演奏
     *
     */
    public void startPlay(int tune) {
        KeyActionMusic keyActionMusic = KeyActionMusic.getFromTrackMusic(trackMusic,activeKeyMap,tune);
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
        player.setSpeed((int) (speed*trackMusic.tpsReal));
    }

    public int autoTune(int minTune,int maxTune){
        return trackMusic.autoTune(activeKeyMap, minTune, maxTune);
    }

    public void setActiveKeyMap(KeyMap activeKeyMap) {
        this.activeKeyMap = activeKeyMap;
    }


}
