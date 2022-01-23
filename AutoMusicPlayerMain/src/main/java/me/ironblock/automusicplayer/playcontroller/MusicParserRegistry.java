package me.ironblock.automusicplayer.playcontroller;

import me.ironblock.automusicplayer.music.parser.AbstractMusicParser;
import me.ironblock.automusicplayer.music.parser.MidiMusicParser;
import me.ironblock.automusicplayer.music.parser.StringMusicParser;

import java.util.*;


public class MusicParserRegistry {

    private static final Map<String, Set<AbstractMusicParser>> suffixParserMap = new HashMap<>();


    public static void registerSuffixParser(AbstractMusicParser musicParser) {
        String suffix = musicParser.getMusicFileSuffix();
        if (suffixParserMap.containsKey(suffix)) {
            suffixParserMap.get(suffix).add(musicParser);
        } else {
            suffixParserMap.put(suffix, new HashSet<>(Collections.singletonList(musicParser)));
        }
    }


    public static Set<AbstractMusicParser> getSuffixParsers(String suffix) {
        return suffixParserMap.get(suffix);
    }


    public static void init() {
        registerSuffixParser(new MidiMusicParser());
        registerSuffixParser(new StringMusicParser());

    }


}
