package me.ironblock.genshinimpactmusicplayer.main;

public class Timer {
    static long startTime;
    public static void start(){
        startTime = System.currentTimeMillis();
    }
    public static long end(){
        return System.currentTimeMillis()-startTime;
    }
}
