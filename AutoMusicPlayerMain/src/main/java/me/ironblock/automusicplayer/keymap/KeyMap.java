package me.ironblock.automusicplayer.keymap;

import me.ironblock.automusicplayer.music.TuneInaccuracy;
import me.ironblock.automusicplayer.note.NoteInfo;

import java.util.HashMap;
import java.util.Map;


public class KeyMap {

    protected final Map<NoteInfo, Integer> noteKeyMap = new HashMap<>();
    protected int minNoteIndex, maxNoteIndex;
    protected int minNoteOctave, maxNoteOctave;


    /**
     * Get the vk code of the specific note
     *
     * @param noteInfo the specific note
     * @return the vk_code of the specific note,if failed,return -1
     */
    public int getNoteKey(NoteInfo noteInfo) {
        //below the min note
        if (noteInfo.getNoteIndex() < minNoteIndex) {
            noteInfo.octave++;
        }
        //over the high note
        if (noteInfo.getNoteIndex() > maxNoteIndex) {
            noteInfo.octave--;
        }
        //if failed,return -1
        if (noteInfo.getNoteIndex() < minNoteIndex || noteInfo.getNoteIndex() > maxNoteIndex) {
            return -1;
        }

        if (noteKeyMap.containsKey(noteInfo)) {
            return noteKeyMap.get(noteInfo);
        } else {
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

    /**
     * Get the inaccuracy of a specific note
     *
     * @param noteInfo the specific note
     * @param tune     how to tune this note
     * @return the inaccuracy of the note
     */
    public TuneInaccuracy getNoteInaccuracy(NoteInfo noteInfo, int tune) {
        NoteInfo noteInfo1 = new NoteInfo(noteInfo.getNoteIndex());
        TuneInaccuracy tuneInaccuracy = new TuneInaccuracy();
        noteInfo1.addKey(tune);
        if (noteInfo1.getNoteIndex() < minNoteIndex) {
            int wrongPitch = (minNoteIndex - noteInfo1.getNoteIndex()) / 12 + 1;
            if (wrongPitch > 1) {
                int inaccuracy = 2 * wrongPitch * noteInfo1.getNoteIndex() * noteInfo1.getNoteIndex() / 10;
                tuneInaccuracy.setBelowLowestPitchInaccuracy(inaccuracy);
                return tuneInaccuracy;
            } else {
                tuneInaccuracy.setBelowLowestPitchInaccuracy(6);
                noteInfo1.addKey(12);
            }

        }
        if (noteInfo1.getNoteIndex() > maxNoteIndex) {
            int wrongPitch = (noteInfo1.getNoteIndex() - maxNoteIndex) / 12 + 1;
            if (wrongPitch > 1) {
                int inaccuracy = 6 * wrongPitch * wrongPitch * wrongPitch * noteInfo1.getNoteIndex() * noteInfo1.getNoteIndex() / 3;
                tuneInaccuracy.setOverHighestPitchInaccuracy(inaccuracy);
                return tuneInaccuracy;
            } else {
                tuneInaccuracy.setOverHighestPitchInaccuracy(6);
                noteInfo1.addKey(-12);
            }
        }

        if (!noteKeyMap.containsKey(noteInfo1)) {
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
                if (tuneInaccuracy.getInaccuracy() == 0) {
                    tuneInaccuracy.setWrongNoteInaccuracy((int) (10 * Math.pow(noteInfo1.getNoteIndex(), 0.4)));
                } else {
                    tuneInaccuracy.setWrongNoteInaccuracy((int) (30 * Math.pow(noteInfo1.getNoteIndex(), 0.4)));
                }
            } else {
                if (tuneInaccuracy.getInaccuracy() == 0) {
                    tuneInaccuracy.setWrongNoteInaccuracy((int) (10 * Math.pow(noteInfo1.getNoteIndex(), 0.4)));
                } else {
                    tuneInaccuracy.setWrongNoteInaccuracy((int) (50 * Math.pow(noteInfo1.getNoteIndex(), 0.4)));
                }
            }
        }
        return tuneInaccuracy;
    }

}
