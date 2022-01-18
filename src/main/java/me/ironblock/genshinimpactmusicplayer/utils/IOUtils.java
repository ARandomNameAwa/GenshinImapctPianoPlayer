package me.ironblock.genshinimpactmusicplayer.utils;

import java.io.*;

public class IOUtils {
    /**
     * 把一个文件按字符串的方式读出来
     *
     * @param inputStream 输入流
     * @return 字符串
     */
    public static String readStringFully(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String tmp;
            StringBuilder sb = new StringBuilder();
            while ((tmp = reader.readLine()) != null) {
                sb.append(tmp).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static InputStream openStream(String in) {
        try {
            return new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
