package me.ironblock.genshinimpactmusicplayer.music;

import com.sun.org.apache.xalan.internal.lib.NodeInfo;
import me.ironblock.genshinimpactmusicplayer.note.NoteInfo;

import java.util.*;

/**
 * 类似midi结构的music类
 * @author :Iron__Block
 * @Date :2022/1/18 20:42
 */
public class TrackMusic {
    private final List<Map<Integer, Set<NoteInfo>>> tracks = new ArrayList<>();
    /**
     * 音乐时长
     */
    public long length;
    /**
     * 真实的音乐长度
     */
    public double realDuration;
    /**
     * 真实的音乐tps
     */
    public int tpsReal;

    public void putNode(int track,int tick,NoteInfo... noteInfo){
        if (track>= tracks.size()){
            tracks.add(new HashMap<>());
        }
        if (!tracks.get(track).containsKey(tick)){
            tracks.get(track).put(tick, new HashSet<>());
        }
        tracks.get(track).get(tick).addAll(Arrays.asList(noteInfo));
    }

    public List<Map<Integer, Set<NoteInfo>>> getTracks(){
        return tracks;
    }

}
