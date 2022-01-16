package me.ironblock.genshinimpactmusicplayer.music;

import me.ironblock.genshinimpactmusicplayer.note.KeyAction;

import java.util.*;

/**
 * @author :Iron__Block
 * @Date :2022/1/15 21:51
 */
public class Music {
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
}
