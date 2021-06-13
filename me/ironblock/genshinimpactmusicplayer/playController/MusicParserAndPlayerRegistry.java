package me.ironblock.genshinimpactmusicplayer.playController;

import me.ironblock.genshinimpactmusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicParser.MidiMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicParser.StringMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.AbstractMusicPlayer;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.CharacterMusicPlayer;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.MidiMusicPlayer;
import me.ironblock.genshinimpactmusicplayer.note.AbstractNoteMessage;
import me.ironblock.genshinimpactmusicplayer.note.CharacterNoteMessage;
import me.ironblock.genshinimpactmusicplayer.note.MidiNoteMessage;

import java.util.*;

/**
 * 音乐解析器和演奏器的注册处
 */
public class MusicParserAndPlayerRegistry {
    /**
     * 文件后缀和解析器的Map
     */
    private static final Map<String, Set<AbstractMusicParser<?, ? extends AbstractNoteMessage>>> suffixParserMap = new HashMap<>();
    /**
     * 音符类型和演奏器的Map
     */
    private static final Map<Class<? extends AbstractNoteMessage>, Set<AbstractMusicPlayer>> notePlayerMap = new HashMap<>();

    /**
     * 注册后缀suffix的解析器
     *
     * @param suffix      要注册的后缀
     * @param musicParser 要注册的解析器
     */
    public static void registerSuffixParser(String suffix, AbstractMusicParser musicParser) {
        if (suffixParserMap.containsKey(suffix)) {
            suffixParserMap.get(suffix).add(musicParser);
        } else {
            suffixParserMap.put(suffix, new HashSet(Collections.singletonList(musicParser)));
        }
    }

    /**
     * 注册clazz音符的演奏器
     *
     * @param clazz  音符类型
     * @param player 演奏器类型
     */
    public static void registerNotePlayer(Class<? extends AbstractNoteMessage> clazz, AbstractMusicPlayer player) {
        if (notePlayerMap.containsKey(clazz)) {
            notePlayerMap.get(clazz).add(player);
        } else {
            notePlayerMap.put(clazz, new HashSet<>(Collections.singletonList(player)));
        }
    }

    /**
     * 获取suffix的解析器
     *
     * @param suffix 后缀
     * @return 解析器
     */
    public static Set<AbstractMusicParser<?, ? extends AbstractNoteMessage>> getSuffixParsers(String suffix) {
        return suffixParserMap.get(suffix);
    }

    /**
     * 获取音符clazz的演奏器
     *
     * @param clazz 音符类型
     * @return 演奏器
     */
    public static Set<AbstractMusicPlayer> getNotePlayers(Class<? extends AbstractNoteMessage> clazz) {
        return notePlayerMap.get(clazz);
    }

    /**
     * 注册一些东西
     */
    public static void init() {
        registerSuffixParser("mid", new MidiMusicParser());
        registerSuffixParser("txt", new StringMusicParser());
        registerNotePlayer(MidiNoteMessage.class, new MidiMusicPlayer());
        registerNotePlayer(CharacterNoteMessage.class, new CharacterMusicPlayer());
    }


}
