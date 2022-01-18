package me.ironblock.genshinimpactmusicplayer.music;

import com.sun.org.apache.xalan.internal.lib.NodeInfo;
import me.ironblock.genshinimpactmusicplayer.Launch;
import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.note.NoteInfo;
import me.ironblock.genshinimpactmusicplayer.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 类似midi结构的music类
 * @author :Iron__Block
 * @Date :2022/1/18 20:42
 */
public class TrackMusic {
    private final Map<Integer,Map<Integer, Set<NoteInfo>>> tracks = new HashMap<>();
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
        if (!tracks.containsKey(track)){
            tracks.put(track,new HashMap<>());
        }
        if (!tracks.get(track).containsKey(tick)){
            tracks.get(track).put(tick, new HashSet<>());
        }
        tracks.get(track).get(tick).addAll(Arrays.asList(noteInfo));
    }

    public Map<Integer,Map<Integer, Set<NoteInfo>>> getTracks(){
        return tracks;
    }

    /**
     * 计算在keyMap下,调音后整个曲子的不精确度
     * @param keyMap keyMap
     * @param tune 调音
     * @return 具体的不精确度信息
     */
    public TuneInfo totalInaccuracy(KeyMap keyMap,int tune){
        TuneInfo tuneInfoTotal = new TuneInfo();
        for (Map<Integer, Set<NoteInfo>> track : getTracks().values()) {
            for (Set<NoteInfo> value : track.values()) {
                for (NoteInfo noteInfo : value) {
                    TuneInfo tuneInfo1 = keyMap.getNoteInaccuracy(noteInfo,tune);
                    tuneInfoTotal.setWrongNoteInaccuracy(tuneInfoTotal.getWrongNoteInaccuracy() + tuneInfo1.getWrongNoteInaccuracy());
                    tuneInfoTotal.setBelowLowestPitchInaccuracy(tuneInfoTotal.getBelowLowestPitchInaccuracy() + tuneInfo1.getBelowLowestPitchInaccuracy());
                    tuneInfoTotal.setOverHighestPitchInaccuracy(tuneInfoTotal.getOverHighestPitchInaccuracy() + tuneInfo1.getOverHighestPitchInaccuracy());
                }
            }
        }
        return tuneInfoTotal;
    }

    /**
     * 寻找所给的keyMap最佳的tune
     * @param keyMap keyMap
     * @param minTune 搜索范围下边界
     * @param maxTune 搜索范围上边界
     * @return 最佳的tune
     */
    public int autoTune(KeyMap keyMap,int minTune,int maxTune){
        Map<Integer, TuneInfo> tuneInaccuracyMap = new HashMap<>();
        for (int i = minTune;i<=maxTune;i++){
            TuneInfo tuneInfo = totalInaccuracy(keyMap, i);
            tuneInfo.setTuneInaccuracy((int) (tuneInfo.getInaccuracy()*(1+ ((double) Math.abs(i))/(maxTune-minTune)/10)));
            tuneInaccuracyMap.put(i, tuneInfo);

        }
        if (Launch.DEBUG_MODE){
            System.out.println("==========");
            tuneInaccuracyMap.forEach(((integer, tuneInfo) -> System.out.println(integer+"---->"+tuneInfo.toString())));
            System.out.println("==========");

        }
        return tuneInaccuracyMap.entrySet().stream()
                .min(Comparator.comparingInt(tune -> tune.getValue().getInaccuracy()))
                .get().getKey();
    }

}
