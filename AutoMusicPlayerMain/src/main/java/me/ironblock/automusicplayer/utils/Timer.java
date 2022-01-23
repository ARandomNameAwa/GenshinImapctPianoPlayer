package me.ironblock.automusicplayer.utils;

/**
 * Timer for loops
 */
public class Timer {

    private long sleepNanos;

    private long lastResetTime;


    public Timer(int tps) {
        sleepNanos = 1000000000 / tps;
    }


    public boolean update() {
        long now = System.nanoTime();
        if (now - lastResetTime >= sleepNanos) {
            lastResetTime = now;
            return true;
        } else {
            return false;
        }

    }


    public void setTps(int tps) {
        this.sleepNanos = 1000000000 / tps;
    }

}
