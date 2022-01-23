package me.ironblock.automusicplayer.music;

/**
 * The information of tune inaccuracy
 *
 * @author :Iron__Block
 * @Date :2022/1/18 22:00
 */
public class TuneInaccuracy {

    private int overHighestPitchInaccuracy;

    private int belowLowestPitchInaccuracy;

    private int wrongNoteInaccuracy;

    private int tuneInaccuracy;

    public int getOverHighestPitchInaccuracy() {
        return overHighestPitchInaccuracy;
    }

    public void setOverHighestPitchInaccuracy(int overHighestPitchInaccuracy) {
        this.overHighestPitchInaccuracy = overHighestPitchInaccuracy;
    }

    public int getBelowLowestPitchInaccuracy() {
        return belowLowestPitchInaccuracy;
    }

    public void setBelowLowestPitchInaccuracy(int belowLowestPitchInaccuracy) {
        this.belowLowestPitchInaccuracy = belowLowestPitchInaccuracy;
    }

    public int getWrongNoteInaccuracy() {
        return wrongNoteInaccuracy;
    }

    public void setWrongNoteInaccuracy(int wrongNoteInaccuracy) {
        this.wrongNoteInaccuracy = wrongNoteInaccuracy;
    }

    public int getTuneInaccuracy() {
        return tuneInaccuracy;
    }

    public void setTuneInaccuracy(int tuneInaccuracy) {
        this.tuneInaccuracy = tuneInaccuracy;
    }


    public int getInaccuracy() {
        return overHighestPitchInaccuracy + belowLowestPitchInaccuracy + wrongNoteInaccuracy + tuneInaccuracy;
    }

    @Override
    public String toString() {
        return "TuneInfo{" +
                "Inaccuracy=" + getInaccuracy() +
                ",overHighestPitchInaccuracy=" + overHighestPitchInaccuracy +
                ", belowLowestPitchInaccuracy=" + belowLowestPitchInaccuracy +
                ", wrongNoteInaccuracy=" + wrongNoteInaccuracy +
                ", tuneInaccuracy=" + tuneInaccuracy +
                '}';
    }
}
