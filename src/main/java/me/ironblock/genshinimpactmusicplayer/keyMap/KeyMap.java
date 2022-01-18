package me.ironblock.genshinimpactmusicplayer.keyMap;

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
     * 返回音符对应的键(有就返回没有就不返回不会做近似)
     *
     * @param noteInfo 音符
     * @return 对应的键 没有的话就返回-1
     */
    public int getNoteKeyOrigin(NoteInfo noteInfo) {
        return noteKeyMap.getOrDefault(noteInfo, -1);
    }


    /**
     * 获取一个音符对应的键的vk_code
     *
     * @return vk_code, 如果找不到返回-1
     */
    public int getNoteKey(int octave, int note) {
        NoteInfo noteInfo = new NoteInfo(octave, note);
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

    public int getNoteInaccuracy(NoteInfo noteInfo){
        boolean keyAdded = false;
        //有没有超过最低音域
        if (noteInfo.getNoteIndex() < minNoteIndex) {
            int wrongPitch = (minNoteIndex - noteInfo.getNoteIndex())/12+1;
            if (wrongPitch>1){
                return 2 * wrongPitch * wrongPitch * noteInfo.getNoteIndex()*noteInfo.getNoteIndex()/3;
            }else{
                noteInfo.addKey(12);
                keyAdded = true;
            }

        }
        //有没有超过最高音域
        if (noteInfo.getNoteIndex() > maxNoteIndex) {
            int wrongPitch = (noteInfo.getNoteIndex()-maxNoteIndex)/12+1;
            if (wrongPitch>1){
                return 4 *wrongPitch*wrongPitch*wrongPitch*noteInfo.getNoteIndex()*noteInfo.getNoteIndex()/3;
            }else{
                noteInfo.addKey(-12);
                keyAdded = true;
            }
        }

        if (noteKeyMap.containsKey(noteInfo)) {  //如果有已知的key
            return keyAdded?6:0;
        } else {          //尝试半音
            noteInfo.increaseOneKey();
            if (noteKeyMap.containsKey(noteInfo)) {
                return keyAdded?100*noteInfo.getNoteIndex():20*noteInfo.getNoteIndex();
            }
            noteInfo.decreaseOnKey();
            noteInfo.decreaseOnKey();
            if (noteKeyMap.containsKey(noteInfo)) {
                return keyAdded?100*noteInfo.getNoteIndex():20*noteInfo.getNoteIndex();
            }
            return keyAdded?160*noteInfo.getNoteIndex():50*noteInfo.getNoteIndex();
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
