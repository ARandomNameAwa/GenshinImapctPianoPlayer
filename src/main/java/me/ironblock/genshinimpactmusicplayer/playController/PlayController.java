package me.ironblock.genshinimpactmusicplayer.playController;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.MusicPlayer;

import java.io.File;
import java.io.InputStream;


/**
 * 演奏控制器
 */
public class PlayController {
    private final MusicPlayer player = new MusicPlayer();
    private KeyMap activeKeyMap;

    /**
     * 开始演奏
     *
     * @param file 文件名
     */
    public void startPlay(InputStream file, AbstractMusicParser parser) throws Exception {
        this.player.playMusic(parser.parseMusic(file,activeKeyMap));
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

    public void setSpeed(int speed) {
        player.setSpeed(speed);
    }

    public void setActiveKeyMap(KeyMap activeKeyMap) {
        this.activeKeyMap = activeKeyMap;
    }
}
