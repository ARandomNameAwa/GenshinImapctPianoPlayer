package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.music.AbstractMusic;
import me.ironblock.genshinimpactmusicplayer.note.AbstractNoteMessage;

import java.lang.reflect.ParameterizedType;

/**
 * MusicParser抽象类
 *
 * @param <T> 解析的参数种类
 * @param <K> 返回的音乐的音符类型
 */
public abstract class AbstractMusicParser<T, K extends AbstractNoteMessage> {
    public abstract AbstractMusic<K> parseMusic(T music) throws Exception;

    /**
     * 获取这个解析器解析出的音乐的音符类型
     *
     * @return 这个解析器解析出的音乐的音符类型
     */
    public Class<K> getNoteType() {
        return (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

}
