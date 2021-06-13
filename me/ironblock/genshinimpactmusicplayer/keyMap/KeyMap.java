package me.ironblock.genshinimpactmusicplayer.keyMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KeyMap {
    static class NoteInfo{
        //在第几个八度
        public int octave;
        //音名
        public int note;

        public NoteInfo(int octave, int note) {
            this.octave = octave;
            this.note = note;
        }

        public NoteInfo(int noteIndex) {
            this((noteIndex / 12) /*- 1*/,noteIndex % 12);
        }
        public int getNoteIndex(){
            return octave*12+note;
        }
        public void increaseOneKey(){
            note++;
            if (note>=12){
                octave++;
                note-=12;
            }
        }
        public void decreaseOnKey(){
            note--;
            if (note<0){
                octave--;
                note+=12;
            }
        }
        @Override
        public String toString() {
            return "NoteInfo{" + "octave=" + octave + ", note=" + note + '}';
        }
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            NoteInfo noteInfo = (NoteInfo) o;
            return octave == noteInfo.octave && note == noteInfo.note;
        }

        @Override
        public int hashCode() {
            return Objects.hash(octave, note);
        }
    }

    /**
     * 音符-VK_Code对应
     */
    protected final Map<NoteInfo, Integer> noteKeyMap = new HashMap<>();
    protected int minNoteIndex,maxNoteIndex;
    protected int minNoteOctave,maxNoteOctave;
    /**
     * 获取一个音符对应的键的vk_code
     * @return vk_code,如果找不到返回-1
     */
    public int getNoteKey(int octave,int note){
        NoteInfo noteInfo = new NoteInfo(octave, note);
        //有没有超过最低音域
        if (noteInfo.getNoteIndex()<minNoteIndex){
            noteInfo.octave = minNoteOctave;
            if (noteInfo.getNoteIndex()<minNoteIndex){
                noteInfo.octave++;
            }
        }
        //有没有超过最高音域
        if (noteInfo.getNoteIndex()>maxNoteIndex){
            noteInfo.octave = maxNoteOctave;
            if (noteInfo.getNoteIndex()>maxNoteIndex){
                noteInfo.octave--;
            }

        }
        //如果尝试调入音域失败,则返回-1
        if (noteInfo.getNoteIndex()<minNoteIndex||noteInfo.getNoteIndex()>maxNoteIndex){
            System.out.println("尝试调入音域失败!!");
            return -1;
        }

        if (noteKeyMap.containsKey(noteInfo)){  //如果有已知的key
            return noteKeyMap.get(noteInfo);
        }else{          //尝试半音
            noteInfo.increaseOneKey();
            if (noteKeyMap.containsKey(noteInfo)){
                return noteKeyMap.get(noteInfo);
            }
            noteInfo.decreaseOnKey();
            noteInfo.decreaseOnKey();
            if (noteKeyMap.containsKey(noteInfo)){
                return noteKeyMap.get(noteInfo);
            }
            System.out.println("尝试半音失败");
            noteInfo.increaseOneKey();
            System.out.println(noteInfo);
            return -1;
        }

    }

    public Map<NoteInfo, Integer> getNoteKeyMap() {
        return noteKeyMap;
    }

    public int getMinNoteIndex() {
        return minNoteIndex;
    }

    public void setMinNoteIndex(int minNoteIndex) {
        this.minNoteIndex = minNoteIndex;
    }

    public int getMaxNoteIndex() {
        return maxNoteIndex;
    }

    public void setMaxNoteIndex(int maxNoteIndex) {
        this.maxNoteIndex = maxNoteIndex;
    }

    public int getMinNoteOctave() {
        return minNoteOctave;
    }

    public void setMinNoteOctave(int minNoteOctave) {
        this.minNoteOctave = minNoteOctave;
    }

    public int getMaxNoteOctave() {
        return maxNoteOctave;
    }

    public void setMaxNoteOctave(int maxNoteOctave) {
        this.maxNoteOctave = maxNoteOctave;
    }
}
