package me.ironblock.genshinimpactmusicplayer.playController;

import me.ironblock.genshinimpactmusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.AbstractMusicPlayer;

import java.util.HashMap;
import java.util.Map;

public class SuffixDealerRegistry {
    private static final Map<String, MusicDealers> suffixDealerMap = new HashMap<>();
    public static void registerSuffixDealer(String suffix,AbstractMusicPlayer musicPlayer,AbstractMusicParser musicParser){
        suffixDealerMap.put(suffix,new MusicDealers(musicPlayer,musicParser));
    }
    public static MusicDealers getSuffixDealer(String suffix){
        return suffixDealerMap.get(suffix);
    }

    static class MusicDealers{
        public AbstractMusicPlayer player;
        public AbstractMusicParser parser;

        public MusicDealers(AbstractMusicPlayer player, AbstractMusicParser parser) {
            this.player = player;
            this.parser = parser;
        }
    }
}
