package me.ironblock.automusicplayer.musicParser;

import me.ironblock.automusicplayer.music.TrackMusic;

import java.io.InputStream;

/**
 * MusicParser抽象类
 */
public abstract class AbstractMusicParser {
    /**
     * 解析音乐
     *
     * @param musicStream 音乐文件流
     * @return 解析好的音乐
     */
    public abstract TrackMusic parseMusic(InputStream musicStream);


    /**
     * 获取所解析音乐文件的后缀
     *
     * @return 音乐文件的后缀
     */
    public abstract String getMusicFileSuffix();


    /**
     * 获取锁解析音乐文件的种类名字
     *
     * @return 音乐文件的种类名字
     */
    public abstract String getMusicFileTypeName();


}
