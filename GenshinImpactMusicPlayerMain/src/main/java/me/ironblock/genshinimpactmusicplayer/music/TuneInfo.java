package me.ironblock.genshinimpactmusicplayer.music;

/**
 * @author :Iron__Block
 * @Date :2022/1/18 22:00
 */
public class TuneInfo {
    /**
     * 因为音符超过音域的不准确度
     */
    private int overHighestPitchInaccuracy;
    /**
     * 因为音符低于音域的不准确度
     */
    private int belowLowestPitchInaccuracy;
    /**
     * 音符变成离它最近/无法变成最近的不准确度
     */
    private int wrongNoteInaccuracy;
    /**
     * 因为调音的大小的不准确度
     */
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

    /**
     * 获取这个音符的总不准确度
     */
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
