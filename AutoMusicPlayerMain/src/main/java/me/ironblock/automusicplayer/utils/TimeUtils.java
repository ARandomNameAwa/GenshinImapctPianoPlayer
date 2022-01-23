package me.ironblock.automusicplayer.utils;

public class TimeUtils {
    /**
     * 把秒换成分秒(mm:ss)形式
     *
     * @param seconds 输入的秒数
     * @return 分秒形式的字符串
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
     * 返回一个字符串形式的进度条
     *
     * @param progress 目前的进度(从0~1)
     * @param length   进度条的长度
     * @return 进度条的字符串形式
     */
    public static String progressBar(double progress, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        int now = (int) (length * progress - 1);
        for (int i = 0; i < now; i++) {
            stringBuilder.append("+");
        }
        stringBuilder.append("◆");
        for (int i = 0; i < length - now; i++) {
            stringBuilder.append("--");
        }
        return stringBuilder.toString();
    }

    /**
     * 把MMSS变成S
     *
     * @param mmss MMSS
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
