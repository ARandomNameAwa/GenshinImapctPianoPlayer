package me.ironblock.genshinimpactmusicplayer.main;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class Music {
    public int tps;
    public long length;
    public List<MusicTrack> musicTrackList = new ArrayList<>();
    public List<KeyMessage> getNextTickKeyMessages(){
        List<KeyMessage> keyMessages = new ArrayList<>();
        for (MusicTrack musicTrack : musicTrackList) {
            keyMessages.addAll(musicTrack.getNextTickKeys());
        }
        return keyMessages;
    }

}
