package me.ironblock.automusicplayer.keyMap;

import me.ironblock.automusicplayer.music.TuneInfo;
import me.ironblock.automusicplayer.note.NoteInfo;

import java.util.HashMap;
import java.util.Map;

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
            noteInfo.octave++;
        }
        //有没有超过最高音域
        if (noteInfo.getNoteIndex() > maxNoteIndex) {
            noteInfo.octave--;
        }
        //如果尝试调入音域失败,则返回-1
        if (noteInfo.getNoteIndex() < minNoteIndex || noteInfo.getNoteIndex() > maxNoteIndex) {
            return -1;
        }

        if (noteKeyMap.containsKey(noteInfo)) {  //如果有已知的key
            return noteKeyMap.get(noteInfo);
        } else {          //尝试半音
            noteInfo.increaseOneKey();
            if (noteKeyMap.containsKey(noteInfo)) {
                return noteKeyMap.get(noteInfo);
            }
            noteInfo.decreaseOnKey();
            noteInfo.decreaseOnKey();
            if (noteKeyMap.containsKey(noteInfo)) {
                return noteKeyMap.get(noteInfo);
            }
            noteInfo.increaseOneKey();
            System.out.println(noteInfo);
            return -1;
        }

    }

    public TuneInfo getNoteInaccuracy(NoteInfo noteInfo, int tune) {
        NoteInfo noteInfo1 = new NoteInfo(noteInfo.getNoteIndex());
        TuneInfo tuneInfo = new TuneInfo();
        noteInfo1.addKey(tune);
        //有没有超过最低音域
        if (noteInfo1.getNoteIndex() < minNoteIndex) {
            int wrongPitch = (minNoteIndex - noteInfo1.getNoteIndex()) / 12 + 1;
            if (wrongPitch > 1) {
                int inaccuracy = 2 * wrongPitch * noteInfo1.getNoteIndex() * noteInfo1.getNoteIndex() / 10;
                tuneInfo.setBelowLowestPitchInaccuracy(inaccuracy);
                return tuneInfo;
            } else {
                tuneInfo.setBelowLowestPitchInaccuracy(6);
                noteInfo1.addKey(12);
            }

        }
        //有没有超过最高音域
        if (noteInfo1.getNoteIndex() > maxNoteIndex) {
            int wrongPitch = (noteInfo1.getNoteIndex() - maxNoteIndex) / 12 + 1;
            if (wrongPitch > 1) {
                int inaccuracy = 6 * wrongPitch * wrongPitch * wrongPitch * noteInfo1.getNoteIndex() * noteInfo1.getNoteIndex() / 3;
                tuneInfo.setOverHighestPitchInaccuracy(inaccuracy);
                return tuneInfo;
            } else {
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
            if (upFind || downFind) {
                if (tuneInfo.getInaccuracy() == 0) {
                    tuneInfo.setWrongNoteInaccuracy((int) (10 * Math.pow(noteInfo1.getNoteIndex(),0.4)));
                } else {
                    tuneInfo.setWrongNoteInaccuracy((int) (30 * Math.pow(noteInfo1.getNoteIndex(),0.4)));
                }
            } else {
                if (tuneInfo.getInaccuracy() == 0) {
                    tuneInfo.setWrongNoteInaccuracy((int) (10 * Math.pow(noteInfo1.getNoteIndex(),0.4)));
                } else {
                    tuneInfo.setWrongNoteInaccuracy((int) (50 * Math.pow(noteInfo1.getNoteIndex(),0.4)));
                }
            }
        }
        return tuneInfo;
    }

}
