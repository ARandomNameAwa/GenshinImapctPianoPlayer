package me.ironblock.genshinimpactmusicplayer.music;

import me.ironblock.genshinimpactmusicplayer.note.AbstractNoteMessage;

import java.util.*;

/**
 * 抽象的音乐类
 *
 * @param <T> 这个音乐使用的音符
 */
public abstract class AbstractMusic<T extends AbstractNoteMessage> {
    /**
     * 音乐时长
     */
    public long length;
    /**
     * 音轨列表
     */
    public List<Map<Long, List<T>>> tracks = new ArrayList<>();
    /**
     * 当前播放的进度
     */
    protected long currentTick;

    /**
     * 获取下一tick的所有音符
     *
     * @return 下一tick的所有音符
     */
    public List<T> getNextTickNote() {
        currentTick++;
        List<T> toReturn = new ArrayList<>();
        for (Map<Long, List<T>> track : tracks) {
            if (track.containsKey(currentTick))
                toReturn.addAll(track.get(currentTick));
        }
        return toReturn;
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
     * 创建一个新的音轨,并返回新音轨的编号
     *
     * @return 新音轨的编号
     */
    public int newTrack() {
        tracks.add(new HashMap<>());
        return tracks.size() - 1;
    }

    /**
     * 向指定音轨的指定位置添加指定的音符
     *
     * @param track   音轨
     * @param tick    位置
     * @param message 音符
     */
    public void addNoteToTrack(int track, long tick, T... message) {
        if (track >= tracks.size()) {  //不存在这种这个音轨时
            tracks.add(track, new HashMap<>());
        }
        if (!tracks.get(track).containsKey(tick)) { //在这个音轨的tick位置没有找到其他音符
            //添加一个只有一个音符的List
            tracks.get(track).put(tick, new ArrayList<>(Arrays.asList(message)));
        } else {    //有其他音符
            //向已有的List添加音符
            tracks.get(track).get(tick).addAll(new ArrayList<>(Arrays.asList(message)));
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
