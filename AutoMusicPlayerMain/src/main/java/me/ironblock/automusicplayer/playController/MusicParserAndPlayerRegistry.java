package me.ironblock.automusicplayer.playController;

import me.ironblock.automusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.automusicplayer.musicParser.MidiMusicParser;
import me.ironblock.automusicplayer.musicParser.StringMusicParser;

import java.util.*;

/**
 * 音乐解析器和演奏器的注册处
 */
public class MusicParserAndPlayerRegistry {
    /**
     * 文件后缀和解析器的Map
     */
    private static final Map<String, Set<AbstractMusicParser>> suffixParserMap = new HashMap<>();

    /**
     * 注册后缀suffix的解析器
     *
     * @param musicParser 要注册的解析器
     */
    public static void registerSuffixParser(AbstractMusicParser musicParser) {
        String suffix = musicParser.getMusicFileSuffix();
        if (suffixParserMap.containsKey(suffix)) {
            suffixParserMap.get(suffix).add(musicParser);
        } else {
            suffixParserMap.put(suffix, new HashSet<>(Collections.singletonList(musicParser)));
        }
    }


    /**
     * 获取suffix的解析器
     *
     * @param suffix 后缀
     * @return 解析器
     */
    public static Set<AbstractMusicParser> getSuffixParsers(String suffix) {
        return suffixParserMap.get(suffix);
    }


    /**
     * 注册一些东西
     */
    public static void init() {
        registerSuffixParser(new MidiMusicParser());
        registerSuffixParser(new StringMusicParser());

    }


}
