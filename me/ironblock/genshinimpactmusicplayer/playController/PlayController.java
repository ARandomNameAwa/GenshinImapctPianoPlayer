package me.ironblock.genshinimpactmusicplayer.playController;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.AbstractMusicPlayer;

import java.io.File;

public class PlayController {
    private AbstractMusicParser parser;
    private AbstractMusicPlayer player;
    /**
     * 开始演奏
     * @param file 文件名
     */
    public void startPlay(String file,AbstractMusicParser parser,AbstractMusicPlayer player) throws Exception {
            File file1 = new File(file);
            this.parser = parser;
            this.player = player;
            this.player.playMusic(this.parser.parseMusic(file));
    }

    /**
     * 停止播放
     */
    public void stopPlay(){
        player.stop();
    }

    /**
     * 切换播放状态
     */
    public void switchPause(){
        player.switchPause();
    }

    public void setSpeed(int speed){
        player.setSpeed(speed);
    }

    public void setActiveKeyMap(KeyMap activeKeyMap){

        player.setActiveKeyMap(activeKeyMap);
    }
}
