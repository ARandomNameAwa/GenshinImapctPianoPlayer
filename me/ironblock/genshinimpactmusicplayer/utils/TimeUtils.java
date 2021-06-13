package me.ironblock.genshinimpactmusicplayer.utils;

public class TimeUtils {
    public static String getMMSSFromS(int seconds){
        StringBuilder sb = new StringBuilder();
        int minutes = seconds/60;
        int second = seconds%60;
        if (minutes<10){
            sb.append("0");
        }
        sb.append(minutes).append(":");
        if (second<10){
            sb.append("0");
        }
        sb.append(second);
        return sb.toString();

    }
    public static String progressBar(double progress,int length){
        StringBuilder stringBuilder = new StringBuilder();
        int now = (int) (length*progress-1);
        for (int i = 0; i < now; i++) {
            stringBuilder.append("+");
        }
        stringBuilder.append("◆");
        for (int i = 0; i < length-now; i++) {
            stringBuilder.append("--");
        }
        return stringBuilder.toString();
    }
}
