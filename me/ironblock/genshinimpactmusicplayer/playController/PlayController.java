package me.ironblock.genshinimpactmusicplayer.playController;

import me.ironblock.genshinimpactmusicplayer.exceptions.ExceptionNeedToBeDisplayed;
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
    public void startPlay(String file) throws ExceptionNeedToBeDisplayed {
        try {
            File file1 = new File(file);
            //获取后缀
            String[] tmp = file1.getName().split("\\.");
            SuffixDealerRegistry.MusicDealers dealers = SuffixDealerRegistry.getSuffixDealer(tmp[tmp.length - 1]);
            parser = dealers.parser;
            player = dealers.player;
            player.playMusic(parser.parseMusic(file));

        }catch (ExceptionNeedToBeDisplayed e1){
            throw e1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionNeedToBeDisplayed(e);
        }

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
}
