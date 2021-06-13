package me.ironblock.genshinimpactmusicplayer.utils;

/**
 * 供循环使用的计时器,使用纳秒计时
 */
public class Timer {
    /**
     * 触发的时间间隔
     */
    private long sleepNanos;
    /**
     * 上一次触发的时间
     */
    private long lastResetTime;

    /**
     * @param tps 一秒触发多少次
     */
    public Timer(int tps) {
        sleepNanos = 1000000000 / tps;
    }

    /**
     * 检查是经过了时间间隔
     *
     * @return 是否经过了
     */
    public boolean update() {
        long now = System.nanoTime();
        if (now - lastResetTime >= sleepNanos) {
            lastResetTime = now;
            return true;
        } else {
            return false;
        }

    }

    /**
     * 设置速度
     *
     * @param tps 速度
     */
    public void setTps(int tps) {
        this.sleepNanos = 1000000000 / tps;
    }

}
