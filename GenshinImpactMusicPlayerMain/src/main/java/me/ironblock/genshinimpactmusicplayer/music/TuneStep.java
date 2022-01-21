package me.ironblock.genshinimpactmusicplayer.music;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :Iron__Block
 * @Date :2022/1/22 1:22
 */
public class TuneStep {
    /**
     * 这个调音方式是否是所有轨道相同升降八度的
     */
    public boolean tracksSame;
    /**
     * 如果tracksSame为true,这个为pitch*12+tune,否则为所有track的tune
     */
    public int tune;

    /**
     * 表示每个track对应的pitch
     */
    public final Map<Integer, Integer> trackPitch = new HashMap<>();

    @Override
    public String toString() {
        return "TuneStep{" +
                "tune=" + tune +
                ", trackPitch=" + trackPitch +
                '}';
    }
}
