package me.ironblock.automusicplayer.music;

import me.ironblock.automusicplayer.keymap.KeyMap;
import me.ironblock.automusicplayer.note.KeyAction;
import me.ironblock.automusicplayer.note.NoteInfo;

import java.util.*;

/**
 * @author :Iron__Block
 * @Date :2022/1/15 21:51
 */
public class KeyActionMusic {
    private static final int NOTE_DELAY = 10;
    /**
     * The length of the music (in ticks)
     */
    public long length;
    /**
     * The map of the time and keyAction
     */
    public Map<Long, Set<KeyAction>> keyActionMap = new HashMap<>();
    protected long currentTick;

    /**
     * Convert a TrackMusic to a KeyActionMusic
     *
     * @param trackMusicIn the trackMusic
     * @param keyMap       the keyMap used in the conversion
     * @param tuneStep     tune
     * @return a KeyActionMusic
     */
    public static KeyActionMusic getFromTrackMusic(TrackMusic trackMusicIn, KeyMap keyMap, TuneStep tuneStep) {
        KeyActionMusic keyActionMusic = new KeyActionMusic();
        keyActionMusic.length = trackMusicIn.length;

        Map<Integer, Map<Integer, Set<NoteInfo>>> tracks = trackMusicIn.getTracks();

        for (int i : tracks.keySet()) {
            Map<Integer, Set<NoteInfo>> track = tracks.get(i);
            if (!trackMusicIn.isTrackMuted(i)) {

                int tune;
                if (tuneStep.tracksSame) {
                    tune = tuneStep.tune;
                } else {
                    tune = tuneStep.trackOctave.get(i) * 12 + tuneStep.tune;
                }
                track.forEach((tick, nodeInfoSet) -> {
                    for (NoteInfo noteInfo : nodeInfoSet) {
                        if (noteInfo.isVKCode()) {
                            KeyAction keyOn = new KeyAction(true, noteInfo.getVk_Code());
                            KeyAction keyOff = new KeyAction(false, noteInfo.getVk_Code());
                            keyActionMusic.addNoteToTick(tick, keyOn);
                            keyActionMusic.addNoteToTick(tick + NOTE_DELAY, keyOff);
                            keyActionMusic.addNoteToTick(tick - NOTE_DELAY / 2, keyOff);
                        } else {
                            NoteInfo noteInfo1 = new NoteInfo(noteInfo.getNoteIndex());
                            noteInfo1.addKey(tune);
                            int key = keyMap.getNoteKey(noteInfo1);
                            if (key != -1) {
                                KeyAction keyOn = new KeyAction(true, keyMap.getNoteKey(noteInfo1));
                                KeyAction keyOff = new KeyAction(false, keyMap.getNoteKey(noteInfo1));
                                keyActionMusic.addNoteToTick(tick, keyOn);
                                keyActionMusic.addNoteToTick(tick + NOTE_DELAY, keyOff);
                                keyActionMusic.addNoteToTick(tick - NOTE_DELAY / 2, keyOff);
                            }
                        }

                    }
                });
            }
        }

        return keyActionMusic;

    }

    /**
     * Get the keyActions of a specific tick
     *
     * @param tick the specific tick
     * @return A set of keyActions
     */
    public Set<KeyAction> getSpecificTickNoteSet(int tick) {
        return keyActionMap.get(currentTick);
    }

    /**
     * Get the keyActions of the nextTick
     *
     * @return A set of keyActions
     */
    public Set<KeyAction> getNextTickNote() {
        currentTick++;
        return keyActionMap.get(currentTick);
    }

    public boolean isMusicFinished() {
        return length <= currentTick;
    }

    /**
     * Add some keyActions to the specific location (in tick)
     *
     * @param tick    location
     * @param message keyActions
     */
    public void addNoteToTick(long tick, KeyAction... message) {
        if (!keyActionMap.containsKey(tick)) {
            Set<KeyAction> set = new HashSet<>(Arrays.asList(message));
            keyActionMap.put(tick, set);
        } else {
            keyActionMap.get(tick).addAll(new ArrayList<>(Arrays.asList(message)));
        }
    }

    public void reset() {
        jumpToTick(0);
    }

    /**
     * Set the current tick of the music
     *
     * @param tick tick
     */
    public void jumpToTick(long tick) {
        currentTick = tick;
    }

    /**
     * Get the current tick of the music
     *
     * @return current tick
     */
    public long getCurrentTick() {
        return currentTick;
    }
}
