package me.ironblock.automusicplayer.music;

import me.ironblock.automusicplayer.Launch;
import me.ironblock.automusicplayer.keymap.KeyMap;
import me.ironblock.automusicplayer.note.NoteInfo;
import me.ironblock.automusicplayer.utils.KeyMapUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Music which has the similar structure of the midi file
 *
 * @author :Iron__Block
 * @Date :2022/1/18 20:42
 */
public class TrackMusic {
    private final Map<Integer, Map<Integer, Set<NoteInfo>>> tracks = new HashMap<>();
    private final Map<Integer, Boolean> tracksMuted = new HashMap<>();
    private final Map<Integer, String> trackInfoMap = new HashMap<>();
    /**
     * the length of the music (in ticks)
     */
    public long length;
    /**
     * the real time of the music (in second)
     */
    public double realDuration;
    /**
     * the origin tps of the music
     */
    public int tpsReal;

    /**
     * Put some notes in the specific track and specific tick
     *
     * @param track    the specific track
     * @param tick     the specific tick
     * @param noteInfo the notes
     */
    public void putNode(int track, int tick, NoteInfo... noteInfo) {
        if (!tracks.containsKey(track)) {
            tracks.put(track, new HashMap<>());
        }
        if (!tracks.get(track).containsKey(tick)) {
            tracks.get(track).put(tick, new HashSet<>());
        }
        tracks.get(track).get(tick).addAll(Arrays.asList(noteInfo));
        tracksMuted.put(track, false);
    }

    public Map<Integer, Map<Integer, Set<NoteInfo>>> getTracks() {
        return tracks;
    }

    /**
     * Calculate the total inaccuracy with the specific keyMap,every track has the same octave
     *
     * @param keyMap keyMap
     * @param tune   tune
     * @return the information of the inaccuracy
     */
    public TuneInaccuracy totalInaccuracy(KeyMap keyMap, int tune) {
        TuneInaccuracy tuneInaccuracyTotal = new TuneInaccuracy();
        for (int i : getTracks().keySet()) {
            if (!isTrackMuted(i)) {
                Map<Integer, Set<NoteInfo>> track = getTracks().get(i);
                for (Set<NoteInfo> value : track.values()) {
                    for (NoteInfo noteInfo : value) {
                        TuneInaccuracy tuneInaccuracy1 = keyMap.getNoteInaccuracy(noteInfo, tune);
                        tuneInaccuracyTotal.setWrongNoteInaccuracy(tuneInaccuracyTotal.getWrongNoteInaccuracy() + tuneInaccuracy1.getWrongNoteInaccuracy());
                        tuneInaccuracyTotal.setBelowLowestPitchInaccuracy(tuneInaccuracyTotal.getBelowLowestPitchInaccuracy() + tuneInaccuracy1.getBelowLowestPitchInaccuracy());
                        tuneInaccuracyTotal.setOverHighestPitchInaccuracy(tuneInaccuracyTotal.getOverHighestPitchInaccuracy() + tuneInaccuracy1.getOverHighestPitchInaccuracy());
                    }
                }
            }
        }


        return tuneInaccuracyTotal;
    }

    /**
     * Calculate the total inaccuracy with the specific keyMap,every track don't need to have the same octave
     *
     * @param keyMap   keyMap
     * @param tuneStep tune
     * @return the information of the inaccuracy
     */
    private TuneInaccuracy totalInaccuracyTracksNotSame(KeyMap keyMap, TuneStep tuneStep) {
        TuneInaccuracy tuneInaccuracyTotal = new TuneInaccuracy();
        for (int i : getTracks().keySet()) {
            if (!isTrackMuted(i)) {
                Map<Integer, Set<NoteInfo>> track = getTracks().get(i);
                int tune = tuneStep.trackOctave.get(i) * 12 + tuneStep.tune;
                for (Set<NoteInfo> value : track.values()) {
                    for (NoteInfo noteInfo : value) {
                        TuneInaccuracy tuneInaccuracy1 = keyMap.getNoteInaccuracy(noteInfo, tune);
                        tuneInaccuracyTotal.setWrongNoteInaccuracy(tuneInaccuracyTotal.getWrongNoteInaccuracy() + tuneInaccuracy1.getWrongNoteInaccuracy());
                        tuneInaccuracyTotal.setBelowLowestPitchInaccuracy(tuneInaccuracyTotal.getBelowLowestPitchInaccuracy() + tuneInaccuracy1.getBelowLowestPitchInaccuracy());
                        tuneInaccuracyTotal.setOverHighestPitchInaccuracy(tuneInaccuracyTotal.getOverHighestPitchInaccuracy() + tuneInaccuracy1.getOverHighestPitchInaccuracy());
                    }
                }
            }
        }


        return tuneInaccuracyTotal;
    }

    /**
     * Auto tune
     *
     * @param keyMap           keyMap used
     * @param minOctave        min Octave
     * @param maxOctave        max Octave
     * @param tracksOctaveSame if every track must have the same octave
     * @return the best tune
     */
    public TuneStep autoTune(KeyMap keyMap, int minOctave, int maxOctave, boolean tracksOctaveSame) {
        if (tracksOctaveSame) {
            return autoTuneTrackPitchSame(keyMap, minOctave, maxOctave);
        } else {
            return autoTunePitchNotSame(keyMap, minOctave, maxOctave);
        }
    }

    /**
     * Auto tune,every track have the same octave
     *
     * @param keyMap    min Octave
     * @param minOctave max Octave
     * @param maxOctave if every track must have the same octave
     * @return the best tune
     */
    private TuneStep autoTuneTrackPitchSame(KeyMap keyMap, int minOctave, int maxOctave) {
        Map<Integer, TuneInaccuracy> tuneInaccuracyMap = new HashMap<>();
        for (int i = minOctave * 12; i <= maxOctave * 12; i++) {
            TuneInaccuracy tuneInaccuracy = totalInaccuracy(keyMap, i);
            tuneInaccuracy.setTuneInaccuracy(Math.abs(i));
            tuneInaccuracyMap.put(i, tuneInaccuracy);

        }
        if (Launch.DEBUG_MODE) {
            System.out.println("======pitchSame====");
            tuneInaccuracyMap.forEach(((integer, tuneInaccuracy) -> System.out.println(integer + "---->" + tuneInaccuracy.toString())));
            System.out.println("===================");

        }
        TuneStep tuneStep = new TuneStep();
        tuneStep.tracksSame = true;
        tuneStep.tune = Objects.requireNonNull(tuneInaccuracyMap.entrySet().stream()
                .min(Comparator.comparingInt(tune -> tune.getValue().getInaccuracy())).orElse(null)).getKey();
        return tuneStep;
    }

    /**
     * Auto tune,every track doesn't need to have the same octave
     *
     * @param keyMap    min Octave
     * @param minOctave max Octave
     * @param maxOctave if every track must have the same octave
     * @return the best tune
     */
    private TuneStep autoTunePitchNotSame(KeyMap keyMap, int minOctave, int maxOctave) {
        List<TuneStep> tuneSteps = new ArrayList<>();
        for (int i = -6; i <= 6; i++) {
            Set<Integer> trackSetNotMuted = tracks.keySet().stream().filter(track -> !isTrackMuted(track)).collect(Collectors.toSet());
            int[] pitchArray = new int[trackSetNotMuted.size()];
            Arrays.fill(pitchArray, minOctave);
            while (pitchArray[pitchArray.length - 1] <= maxOctave) {
                TuneStep tuneStep = new TuneStep();
                tuneStep.tracksSame = false;
                tuneStep.tune = i;
                int loopTime = 0;
                for (Integer integer : trackSetNotMuted) {
                    tuneStep.trackOctave.put(integer, pitchArray[loopTime]);
                    loopTime++;
                }
                tuneSteps.add(tuneStep);

                pitchArray[0]++;
                for (int i1 = 0; i1 < pitchArray.length - 1; i1++) {
                    if (pitchArray[i1] > maxOctave) {
                        pitchArray[i1] = minOctave;

                        pitchArray[i1 + 1]++;
                    }
                }
            }
        }

        tuneSteps = tuneSteps.stream().filter(tuneStep -> {
            int max = tuneStep.trackOctave.values().stream().max(Comparator.comparingInt(i -> i)).orElse(0);
            int min = tuneStep.trackOctave.values().stream().min(Comparator.comparingInt(i -> i)).orElse(0);
            return (max - min) <= 1;
        }).collect(Collectors.toList());
        Map<TuneStep, TuneInaccuracy> tuneInfoList = tuneSteps.stream().collect(Collectors.toMap(tuneStep -> tuneStep, tuneStep -> {
                    TuneInaccuracy tuneInaccuracy = totalInaccuracyTracksNotSame(keyMap, tuneStep);
                    int tuneValue = Math.abs(tuneStep.trackOctave.values().stream().mapToInt(value -> value).sum() / tuneStep.trackOctave.values().size()) * 12 + tuneStep.tune;
                    tuneInaccuracy.setTuneInaccuracy(tuneValue);
                    return tuneInaccuracy;
                }
        ));
        TuneStep best = Objects.requireNonNull(tuneInfoList.entrySet().stream().min(Comparator.comparingInt(node -> node.getValue().getInaccuracy())).orElse(null)).getKey();
        if (Launch.DEBUG_MODE) {
            System.out.println("=======Track Not Same======");
            tuneInfoList.forEach((key, value) -> {
                System.out.println(key + "---->" + value);
            });
            System.out.println("===========================");

        }
        return best;


    }

    /**
     * Mute a track
     *
     * @param track the specific track
     */
    public void muteTrack(int track) {
        tracksMuted.put(track, true);
    }

    /**
     * Unmute a track
     *
     * @param track the specific track
     */
    public void unmuteTrack(int track) {
        tracksMuted.put(track, false);
    }

    public boolean isTrackMuted(int track) {
        return tracksMuted.getOrDefault(track, false);
    }

    public String getTrackInfo(int track) {
        if (trackInfoMap.containsKey(track)) {
            return trackInfoMap.get(track);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Highest note:").append(KeyMapUtils.getFullNameFromNoteIndex(trackMaxNote(track))).append(",");
            sb.append("Lowest note:").append(KeyMapUtils.getFullNameFromNoteIndex(trackMinNote(track))).append(",");
            sb.append("Average note:").append(KeyMapUtils.getFullNameFromNoteIndex(trackAvgNote(track))).append(",");
            sb.append("Count:").append(trackTotalNotes(track));
            trackInfoMap.put(track, sb.toString());
            return sb.toString();
        }

    }


    private int trackMinNote(int track) {
        if (tracks.containsKey(track)) {
            return tracks.get(track).values().stream().mapToInt(noteSet -> noteSet.stream().mapToInt(NoteInfo::getNoteIndex).min().orElse(0)).min().orElse(0);
        } else {
            return 0;
        }
    }

    private int trackMaxNote(int track) {
        if (tracks.containsKey(track)) {
            return tracks.get(track).values().stream().mapToInt(noteSet -> noteSet.stream().mapToInt(NoteInfo::getNoteIndex).max().orElse(0)).max().orElse(0);
        } else {
            return 0;
        }

    }

    private int trackAvgNote(int track) {
        if (tracks.containsKey(track)) {
            return tracks.get(track).values().stream().mapToInt(noteInfoSet ->
                    noteInfoSet.stream().mapToInt(NoteInfo::getNoteIndex).sum()
            ).sum() / trackTotalNotes(track);
        } else {
            return 0;
        }
    }

    private int trackTotalNotes(int track) {
        if (tracks.containsKey(track)) {
            return tracks.get(track).values().stream().mapToInt(Set::size
            ).sum();

        } else {
            return 0;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackMusic that = (TrackMusic) o;
        return length == that.length && Double.compare(that.realDuration, realDuration) == 0 && tpsReal == that.tpsReal && Objects.equals(tracks, that.tracks) && Objects.equals(tracksMuted, that.tracksMuted) && Objects.equals(trackInfoMap, that.trackInfoMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tracks, tracksMuted, length, realDuration, tpsReal, trackInfoMap);
    }
}
