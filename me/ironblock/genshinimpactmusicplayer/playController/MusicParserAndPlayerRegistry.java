package me.ironblock.genshinimpactmusicplayer.playController;

import me.ironblock.genshinimpactmusicplayer.musicParser.AbstractMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicParser.MidiMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicParser.StringMusicParser;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.AbstractMusicPlayer;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.CommonMusicPlayer;
import me.ironblock.genshinimpactmusicplayer.musicPlayer.MidiMusicPlayer;
import me.ironblock.genshinimpactmusicplayer.note.AbstractNoteMessage;
import me.ironblock.genshinimpactmusicplayer.note.CommonNoteMessage;
import me.ironblock.genshinimpactmusicplayer.note.MidiNoteMessage;

import java.util.*;

public class MusicParserAndPlayerRegistry {
    private static final Map<String, Set<AbstractMusicParser<?,? extends AbstractNoteMessage>>> suffixParserMap = new HashMap<>();
    private static final Map<Class<? extends AbstractNoteMessage>, Set<AbstractMusicPlayer>> notePlayerMap = new HashMap<>();


    public static void registerSuffixParser(String suffix,AbstractMusicParser musicParser){
        if (suffixParserMap.containsKey(suffix)){
            suffixParserMap.get(suffix).add(musicParser);
        }else{
            suffixParserMap.put(suffix, new HashSet(Collections.singletonList( musicParser)));
        }




    }
    public static void registerNotePlayer(Class<? extends AbstractNoteMessage> clazz,AbstractMusicPlayer player){
        if (notePlayerMap.containsKey(clazz)){
            notePlayerMap.get(clazz).add(player);
        }else{
            notePlayerMap.put(clazz, new HashSet<>(Collections.singletonList(player)));
        }
    }


    public static Set<AbstractMusicParser<?,? extends AbstractNoteMessage>> getSuffixParsers(String suffix){
        return suffixParserMap.get(suffix);
    }

    public static Set<AbstractMusicPlayer> getNotePlayers(Class<? extends AbstractNoteMessage> clazz){
        return notePlayerMap.get(clazz);
    }


    public static void init(){
        registerSuffixParser("mid",new MidiMusicParser());
        registerSuffixParser("txt",new StringMusicParser());
        registerNotePlayer(MidiNoteMessage.class,new MidiMusicPlayer());
        registerNotePlayer(CommonNoteMessage.class,new CommonMusicPlayer());
    }




}
