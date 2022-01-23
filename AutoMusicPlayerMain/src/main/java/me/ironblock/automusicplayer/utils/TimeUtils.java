package me.ironblock.automusicplayer.utils;

public class TimeUtils {
    /**
     * Convert s to mm:ss
     *
     * @param seconds seconds
     * @return mm:ss
     */
    public static String getMMSSFromS(int seconds) {
        StringBuilder sb = new StringBuilder();
        int minutes = seconds / 60;
        int second = seconds % 60;
        if (minutes < 10) {
            sb.append("0");
        }
        sb.append(minutes).append(":");
        if (second < 10) {
            sb.append("0");
        }
        sb.append(second);
        return sb.toString();

    }

    /**
     * convert mm:ss to s
     *
     * @param mmss mm:ss
     * @return S
     */
    public static int getSFromMMSS(String mmss) {
        String[] spl = mmss.split(":");
        int total = 0;
        total += Integer.parseInt(spl[0]) * 60;
        total += Integer.parseInt(spl[1]);
        return total;
    }
}
