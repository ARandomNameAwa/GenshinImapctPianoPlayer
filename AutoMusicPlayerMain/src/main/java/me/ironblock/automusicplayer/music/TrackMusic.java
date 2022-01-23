package me.ironblock.automusicplayer.music;

import me.ironblock.automusicplayer.Launch;
import me.ironblock.automusicplayer.keyMap.KeyMap;
import me.ironblock.automusicplayer.note.NoteInfo;
import me.ironblock.automusicplayer.utils.KeyMapUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类似midi结构的music类
 *
 * @author :Iron__Block
 * @Date :2022/1/18 20:42
 */
public class TrackMusic {
    private final Map<Integer, Map<Integer, Set<NoteInfo>>> tracks = new HashMap<>();
    private final Map<Integer, Boolean> tracksMuted = new HashMap<>();
    /**
     * 音乐时长
     */
    public long length;
    /**
     * 真实的音乐长度
     */
    public double realDuration;
    /**
     * 真实的音乐tps
     */
    public int tpsReal;


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
     * 计算在keyMap下,调音后整个曲子的不精确度
     *
     * @param keyMap keyMap
     * @param tune   调音
     * @return 具体的不精确度信息
     */
    public TuneInfo totalInaccuracy(KeyMap keyMap, int tune) {
        TuneInfo tuneInfoTotal = new TuneInfo();
        for (int i : getTracks().keySet()) {
            if (!isTrackMuted(i)) {
                Map<Integer, Set<NoteInfo>> track = getTracks().get(i);
                for (Set<NoteInfo> value : track.values()) {
                    for (NoteInfo noteInfo : value) {
                        TuneInfo tuneInfo1 = keyMap.getNoteInaccuracy(noteInfo, tune);
                        tuneInfoTotal.setWrongNoteInaccuracy(tuneInfoTotal.getWrongNoteInaccuracy() + tuneInfo1.getWrongNoteInaccuracy());
                        tuneInfoTotal.setBelowLowestPitchInaccuracy(tuneInfoTotal.getBelowLowestPitchInaccuracy() + tuneInfo1.getBelowLowestPitchInaccuracy());
                        tuneInfoTotal.setOverHighestPitchInaccuracy(tuneInfoTotal.getOverHighestPitchInaccuracy() + tuneInfo1.getOverHighestPitchInaccuracy());
                    }
                }
            }
        }


        return tuneInfoTotal;
    }

    /**
     * 计算在keyMap下,调音后整个曲子的不精确度,每个轨道不一样
     *
     * @param keyMap   keyMap
     * @param tuneStep 调音信息
     * @return 具体的不精确度信息
     */
    private TuneInfo totalInaccuracyTracksNotSame(KeyMap keyMap, TuneStep tuneStep) {
        TuneInfo tuneInfoTotal = new TuneInfo();
        for (int i : getTracks().keySet()) {
            if (!isTrackMuted(i)) {
                Map<Integer, Set<NoteInfo>> track = getTracks().get(i);
                int tune = tuneStep.trackPitch.get(i) * 12 + tuneStep.tune;
                for (Set<NoteInfo> value : track.values()) {
                    for (NoteInfo noteInfo : value) {
                        TuneInfo tuneInfo1 = keyMap.getNoteInaccuracy(noteInfo, tune);
                        tuneInfoTotal.setWrongNoteInaccuracy(tuneInfoTotal.getWrongNoteInaccuracy() + tuneInfo1.getWrongNoteInaccuracy());
                        tuneInfoTotal.setBelowLowestPitchInaccuracy(tuneInfoTotal.getBelowLowestPitchInaccuracy() + tuneInfo1.getBelowLowestPitchInaccuracy());
                        tuneInfoTotal.setOverHighestPitchInaccuracy(tuneInfoTotal.getOverHighestPitchInaccuracy() + tuneInfo1.getOverHighestPitchInaccuracy());
                    }
                }
            }
        }


        return tuneInfoTotal;
    }


    /**
     * 自动调音
     *
     * @param keyMap          使用的keyMap
     * @param minPitch        调音扫描范围最小的八度
     * @param maxPitch        调音扫描范围最大的八度
     * @param tracksPitchSame 不同轨道的升降八度是否一致
     * @return 如果tracksPitchSame为true 返回一个元素的Map,这个元素的key为114514,内容为pitch*12+tune 否则返回一个的Map
     * 其中的元素的key为轨道编号,value为轨道的pitch,key为114514的value为所有轨道的tune
     */
    public TuneStep autoTune(KeyMap keyMap, int minPitch, int maxPitch, boolean tracksPitchSame) {
        if (tracksPitchSame) {
            return autoTuneTrackPitchSame(keyMap, minPitch, maxPitch);
        } else {
            return autoTunePitchNotSame(keyMap, minPitch, maxPitch);
        }
    }

    /**
     * 每个轨道的pitch都相同的自动调音
     *
     * @param keyMap   使用的keyMap
     * @param minPitch 调音扫描范围最小的八度
     * @param maxPitch 调音扫描范围最大的八度
     * @return pitch*12+tune
     */
    private TuneStep autoTuneTrackPitchSame(KeyMap keyMap, int minPitch, int maxPitch) {
        Map<Integer, TuneInfo> tuneInaccuracyMap = new HashMap<>();
        for (int i = minPitch * 12; i <= maxPitch * 12; i++) {
            TuneInfo tuneInfo = totalInaccuracy(keyMap, i);
            tuneInfo.setTuneInaccuracy(Math.abs(i));
            tuneInaccuracyMap.put(i, tuneInfo);

        }
        if (Launch.DEBUG_MODE) {
            System.out.println("======pitchSame====");
            tuneInaccuracyMap.forEach(((integer, tuneInfo) -> System.out.println(integer + "---->" + tuneInfo.toString())));
            System.out.println("===================");

        }
        TuneStep tuneStep = new TuneStep();
        tuneStep.tracksSame = true;
        tuneStep.tune = Objects.requireNonNull(tuneInaccuracyMap.entrySet().stream()
                .min(Comparator.comparingInt(tune -> tune.getValue().getInaccuracy())).orElse(null)).getKey();
        return tuneStep;
    }

    /**
     * 每个轨道的pitch都不同的自动调音
     *
     * @param keyMap   使用的keyMap
     * @param minPitch 调音扫描范围最小的八度
     * @param maxPitch 调音扫描范围最大的八度
     * @return 一个的Map, 其中的元素的key为轨道编号, value为轨道的pitch, key为114514的value为所有轨道的tune
     */
    private TuneStep autoTunePitchNotSame(KeyMap keyMap, int minPitch, int maxPitch) {
        List<TuneStep> tuneSteps = new ArrayList<>();
        for (int i = -6; i <= 6; i++) {
            Set<Integer> trackSetNotMuted = tracks.keySet().stream().filter(track -> !isTrackMuted(track)).collect(Collectors.toSet());
            int[] pitchArray = new int[trackSetNotMuted.size()];
            Arrays.fill(pitchArray, minPitch);
            while (pitchArray[pitchArray.length - 1] <= maxPitch) {
                TuneStep tuneStep = new TuneStep();
                tuneStep.tracksSame = false;
                tuneStep.tune = i;
                int loopTime = 0;
                for (Integer integer :trackSetNotMuted) {
                    tuneStep.trackPitch.put(integer, pitchArray[loopTime]);
                    loopTime++;
                }
                tuneSteps.add(tuneStep);

                pitchArray[0]++;
                for (int i1 = 0; i1 < pitchArray.length - 1; i1++) {
                    if (pitchArray[i1] > maxPitch) {
                        pitchArray[i1] = minPitch;   //{Inaccuracy=299826,overHighestPitchInaccuracy=48, belowLowestPitchInaccuracy=147501, wrongNoteInaccuracy=152270, tuneInaccuracy=7}

                        pitchArray[i1 + 1]++;
                    }
                }
            }
        }

        tuneSteps = tuneSteps.stream().filter(tuneStep->{
            int max = tuneStep.trackPitch.values().stream().max(Comparator.comparingInt(i->i)).orElse(0);
            int min = tuneStep.trackPitch.values().stream().min(Comparator.comparingInt(i->i)).orElse(0);
            return (max - min) <= 1;
        }).collect(Collectors.toList());
        Map<TuneStep, TuneInfo> tuneInfoList = tuneSteps.stream().collect(Collectors.toMap(tuneStep -> tuneStep, tuneStep -> {
                    TuneInfo tuneInfo = totalInaccuracyTracksNotSame(keyMap, tuneStep);
                    int tuneValue = Math.abs(tuneStep.trackPitch.values().stream().mapToInt(value -> value).sum() / tuneStep.trackPitch.values().size()) * 12 + tuneStep.tune;
                    tuneInfo.setTuneInaccuracy(tuneValue);
                    return tuneInfo;
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
     * 静音一个轨道
     *
     * @param track 轨道
     */
    public void muteTrack(int track) {
        tracksMuted.put(track, true);
    }

    /**
     * 给一个轨道接触静音
     *
     * @param track 轨道
     */
    public void dismuteTrack(int track) {
        tracksMuted.put(track, false);
    }

    /**
     * 一个轨道是否被静音
     *
     * @param track 轨道
     * @return 是否被静音
     */
    public boolean isTrackMuted(int track) {
        return tracksMuted.getOrDefault(track, false);
    }

    private final Map<Integer, String> trackInfoMap = new HashMap<>();

    public String getTrackInfo(int track) {
        if (trackInfoMap.containsKey(track)) {
            return trackInfoMap.get(track);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("最高音:").append(KeyMapUtils.getFullNameFromNoteIndex(trackMaxNote(track))).append(",");
            sb.append("最低音:").append(KeyMapUtils.getFullNameFromNoteIndex(trackMinNote(track))).append(",");
            sb.append("平均:").append(KeyMapUtils.getFullNameFromNoteIndex(trackAvgNote(track))).append(",");
            sb.append("总音符数:").append(trackTotalNotes(track));
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
