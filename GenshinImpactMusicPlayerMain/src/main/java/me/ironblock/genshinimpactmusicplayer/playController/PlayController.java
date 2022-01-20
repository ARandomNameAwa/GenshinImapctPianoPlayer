package me.ironblock.genshinimpactmusicplayer.playController;

import me.ironblock.genshinimpactmusicplayer.music.TrackMusic;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.MusicPlayer;
import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.music.KeyActionMusic;
import me.ironblock.genshinimpactmusicplayer.musicParser.AbstractMusicParser;

import java.io.InputStream;


/**
 * 演奏控制器
 */
public class PlayController {
    private final MusicPlayer player = new MusicPlayer();
    private TrackMusic trackMusic;
    private KeyMap activeKeyMap;
    private String currentMusicName = "";

    public void prepareMusicPlayed(InputStream file,AbstractMusicParser parser,String name){
        if (!currentMusicName.equals(name)){
            trackMusic = parser.parseMusic(file);
            currentMusicName = name;
        }
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

    public void jumpToTick(int tick){
        player.getKeyActionMusicPlayed().jumpToTick(tick);
    }
    public int getTotalTick(){
        return (int) player.getKeyActionMusicPlayed().length;
    }
    public int getSpeed(){
        return player.getSpeed();
    }

    public boolean isPlaying(){
        return trackMusic != null && player.getKeyActionMusicPlayed() != null;
    }

    public TrackMusic getTrackMusic(){
        return trackMusic;
    }

    public boolean TrackMusicLoaded(){
        return trackMusic!=null;
    }

}
