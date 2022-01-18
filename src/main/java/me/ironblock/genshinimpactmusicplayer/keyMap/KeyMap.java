package me.ironblock.genshinimpactmusicplayer.keyMap;

import me.ironblock.genshinimpactmusicplayer.music.TuneInfo;
import me.ironblock.genshinimpactmusicplayer.note.NoteInfo;
import me.ironblock.genshinimpactmusicplayer.utils.KeyMapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 键映射
 */
public class KeyMap {
    /**
     * 音符-VK_Code对应
     */
    protected final Map<NoteInfo, Integer> noteKeyMap = new HashMap<>();
    protected int minNoteIndex, maxNoteIndex;
    protected int minNoteOctave, maxNoteOctave;


    /**
     * 获取一个音符对应的键的vk_code
     *
     * @return vk_code, 如果找不到返回-1
     */
    public int getNoteKey(NoteInfo noteInfo) {
        //有没有超过最低音域
        if (noteInfo.getNoteIndex() < minNoteIndex) {
            System.out.println("超过最低音域:尝试找到" + KeyMapUtils.getNoteNameFromNoteIndex(noteInfo.note) + noteInfo.octave);
            noteInfo.octave++;
        }
        //有没有超过最高音域
        if (noteInfo.getNoteIndex() > maxNoteIndex) {
            System.out.println("超过最高音域:尝试找到" + KeyMapUtils.getNoteNameFromNoteIndex(noteInfo.note) + noteInfo.octave);
            noteInfo.octave--;
        }
        //如果尝试调入音域失败,则返回-1
        if (noteInfo.getNoteIndex() < minNoteIndex || noteInfo.getNoteIndex() > maxNoteIndex) {
            System.out.println("尝试调入音域失败!!");
            return -1;
        }

        if (noteKeyMap.containsKey(noteInfo)) {  //如果有已知的key
            return noteKeyMap.get(noteInfo);
        } else {          //尝试半音
            System.out.println("找不到"+KeyMapUtils.getNoteNameFromNoteIndex(noteInfo.note) + noteInfo.octave);
            noteInfo.increaseOneKey();
            if (noteKeyMap.containsKey(noteInfo)) {
                System.out.println("上半音:"+KeyMapUtils.getNoteNameFromNoteIndex(noteInfo.note) + noteInfo.octave);
                return noteKeyMap.get(noteInfo);
            }
            noteInfo.decreaseOnKey();
            noteInfo.decreaseOnKey();
            if (noteKeyMap.containsKey(noteInfo)) {
                System.out.println("下半音:"+KeyMapUtils.getNoteNameFromNoteIndex(noteInfo.note) + noteInfo.octave);
                return noteKeyMap.get(noteInfo);
            }
            System.out.println("尝试半音失败");
            noteInfo.increaseOneKey();
            System.out.println(noteInfo);
            return -1;
        }

    }

    public TuneInfo getNoteInaccuracy(NoteInfo noteInfo, int tune){
        NoteInfo noteInfo1 = new NoteInfo(noteInfo.getNoteIndex());
        TuneInfo tuneInfo = new TuneInfo();
        noteInfo1.addKey(tune);
        //有没有超过最低音域
        if (noteInfo1.getNoteIndex() < minNoteIndex) {
            int wrongPitch = (minNoteIndex - noteInfo1.getNoteIndex())/12+1;
            if (wrongPitch>1){
                int inaccuracy = 2 * wrongPitch * wrongPitch * noteInfo1.getNoteIndex()*noteInfo1.getNoteIndex()/3;
                tuneInfo.setOverHighestPitchInaccuracy(inaccuracy);
                return tuneInfo;
            }else{
                tuneInfo.setOverHighestPitchInaccuracy(6);
                noteInfo1.addKey(12);
            }

        }
        //有没有超过最高音域
        if (noteInfo1.getNoteIndex() > maxNoteIndex) {
            int wrongPitch = (noteInfo1.getNoteIndex()-maxNoteIndex)/12+1;
            if (wrongPitch>1){
                int inaccuracy = 4 * wrongPitch * wrongPitch * wrongPitch * noteInfo1.getNoteIndex() * noteInfo1.getNoteIndex() / 3;
                tuneInfo.setBelowLowestPitchInaccuracy(inaccuracy);
                return tuneInfo;
            }else{
                tuneInfo.setOverHighestPitchInaccuracy(6);
                noteInfo1.addKey(-12);
            }
        }

        if (!noteKeyMap.containsKey(noteInfo1)) {  //如果有已知的key
            boolean upFind = false;
            boolean downFind = false;

            noteInfo1.increaseOneKey();
            if (noteKeyMap.containsKey(noteInfo1)) {
                upFind = true;
            }
            noteInfo1.decreaseOnKey();
            noteInfo1.decreaseOnKey();
            if (noteKeyMap.containsKey(noteInfo1)) {
                downFind = true;
            }
            if (upFind||downFind){
                if (tuneInfo.getInaccuracy()==0){
                    tuneInfo.setWrongNoteInaccuracy(20*noteInfo1.getNoteIndex());
                }else{
                    tuneInfo.setWrongNoteInaccuracy(60*noteInfo1.getNoteIndex());
                }
            }else{
                if (tuneInfo.getInaccuracy()==0){
                    tuneInfo.setWrongNoteInaccuracy(50*noteInfo1.getNoteIndex());
                }else{
                    tuneInfo.setWrongNoteInaccuracy(150*noteInfo1.getNoteIndex());
                }
            }
        }
        return tuneInfo;
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
