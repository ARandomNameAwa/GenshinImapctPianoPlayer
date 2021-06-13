package me.ironblock.genshinimpactmusicplayer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

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
}
