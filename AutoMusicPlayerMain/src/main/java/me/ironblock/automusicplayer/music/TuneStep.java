package me.ironblock.automusicplayer.music;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The steps of the tune
 *
 * @author :Iron__Block
 * @Date :2022/1/22 1:22
 */
public class TuneStep {
    /**
     * The octave of the tracks,if tracksSame is true,this map will be empty
     */
    public final Map<Integer, Integer> trackOctave = new HashMap<>();
    /**
     * If every track must have the same octave
     */
    public boolean tracksSame;
    /**
     * If tracksSame is true,this is octave*12+tune,or it is the tune of the tracks
     */
    public int tune;

    @Override
    public String toString() {
        return "TuneStep{" +
                "tune=" + tune +
                ", trackPitch=" + trackOctave +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TuneStep tuneStep = (TuneStep) o;
        return tracksSame == tuneStep.tracksSame && tune == tuneStep.tune && Objects.equals(trackOctave, tuneStep.trackOctave);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tracksSame, tune, trackOctave);
    }
}
