package me.ironblock.genshinimpactmusicplayer.music;

import com.sun.org.apache.xalan.internal.lib.NodeInfo;
import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.note.KeyAction;
import me.ironblock.genshinimpactmusicplayer.note.NoteInfo;

import java.util.*;

/**
 * @author :Iron__Block
 * @Date :2022/1/15 21:51
 */
public class KeyActionMusic {
    /**
     * 音乐时长
     */
    public long length;


    /**
     * 键盘操作列表
     */
    public Map<Long, Set<KeyAction>> keyActionMap = new HashMap<>();
    /**
     * 当前播放的进度
     */
    protected long currentTick;

    /**
     * 获取指定tick的键盘操作
     * @param tick 指定tick
     * @return 键盘操作
     */
    public Set<KeyAction> getSpecificTickNoteSet(int tick){
        return keyActionMap.get(currentTick);
    }

    /**
     * 获取下一tick的所有音符
     *
     * @return 下一tick的所有音符
     */
    public Set<KeyAction> getNextTickNote() {
        currentTick++;
        return keyActionMap.get(currentTick);
    }

    /**
     * 判断音乐是否播放完了
     *
     * @return 是或否
     */
    public boolean isMusicFinished() {
        return length <= currentTick;
    }


    /**
     * 向指定音轨的指定位置添加指定的音符
     *
     * @param tick    位置
     * @param message 音符
     */
    public void addNoteToTick(long tick, KeyAction... message) {
        if (!keyActionMap.containsKey(tick)) { //在这个音轨的tick位置没有找到其他音符
            //添加一个只有一个音符的List
            Set<KeyAction> set = new HashSet<>(Arrays.asList(message));
            keyActionMap.put(tick, set);
        } else {    //有其他音符
            //向已有的List添加音符
            keyActionMap.get(tick).addAll(new ArrayList<>(Arrays.asList(message)));
        }
    }

    /**
     * 重新开始这段音乐
     */
    public void reset() {
        jumpToTick(0);
    }

    /**
     * 调整当前音乐的进度
     *
     * @param tick 音乐的进度
     */
    public void jumpToTick(long tick) {
        currentTick = tick;
    }

    /**
     * 获取现在播放的进度
     *
     * @return 现在播放的进度
     */
    public long getCurrentTick() {
        return currentTick;
    }

    private static final int noteDelay = 10;

    public static KeyActionMusic getFromTrackMusic(TrackMusic trackMusicIn, KeyMap keyMap,int tune) {
        KeyActionMusic keyActionMusic = new KeyActionMusic();
        keyActionMusic.length = trackMusicIn.length;

        Map<Integer,Map<Integer, Set<NoteInfo>>> tracks = trackMusicIn.getTracks();

        for (int i : tracks.keySet()) {
            Map<Integer, Set<NoteInfo>> track = tracks.get(i);
            if (!trackMusicIn.isTrackMuted(i)) {
                track.forEach((tick, nodeInfoSet) -> {
                    for (NoteInfo noteInfo : nodeInfoSet) {
                        if (noteInfo.isVKCode()){
                            KeyAction keyOn = new KeyAction(true,noteInfo.getVk_Code());
                            KeyAction keyOff = new KeyAction(false,noteInfo.getVk_Code());
                            keyActionMusic.addNoteToTick(tick,keyOn);
                            keyActionMusic.addNoteToTick(tick+noteDelay,keyOff);
                            keyActionMusic.addNoteToTick(tick-noteDelay/2,keyOff);
                        }else{
                            NoteInfo noteInfo1 = new NoteInfo(noteInfo.getNoteIndex());
                            noteInfo1.addKey(tune);
                            int key = keyMap.getNoteKey(noteInfo);
                            if (key!=-1){
                                KeyAction keyOn = new KeyAction(true,keyMap.getNoteKey(noteInfo1));
                                KeyAction keyOff = new KeyAction(false, keyMap.getNoteKey(noteInfo1));
                                keyActionMusic.addNoteToTick(tick,keyOn);
                                keyActionMusic.addNoteToTick(tick+noteDelay,keyOff);
                                keyActionMusic.addNoteToTick(tick-noteDelay/2,keyOff);
                            }
                        }

                    }
                });
            }
        }

        return keyActionMusic;

    }
}
