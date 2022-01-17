package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.music.Music;

import java.io.InputStream;

/**
 * MusicParser抽象类
 *
 */
public abstract class AbstractMusicParser{
    /**
     * 解析音乐
     * @param musicStream 音乐文件流
     * @param keyMap 使用的keyMap
     * @return 解析好的音乐
     * @throws Exception 搞不好什么错误
     */
    public abstract Music parseMusic(InputStream musicStream, KeyMap keyMap,int tune) throws Exception;


    /**
     * 获取所解析音乐文件的后缀
     * @return 音乐文件的后缀
     */
    public abstract String getMusicFileSuffix();


    /**
     * 获取锁解析音乐文件的种类名字
     * @return 音乐文件的种类名字
     */
    public abstract String getMusicFileTypeName();


    public abstract int totalNoteInaccuracy(InputStream musicStream, KeyMap keyMap,int tune) throws Exception;

}
