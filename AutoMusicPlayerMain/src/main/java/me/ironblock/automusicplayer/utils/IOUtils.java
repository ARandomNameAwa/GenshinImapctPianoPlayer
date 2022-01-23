package me.ironblock.automusicplayer.utils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class IOUtils {

    private static final Set<InputStream> inputStreams = new HashSet<>();

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
            InputStream inputStream = new FileInputStream(in);
            inputStreams.add(inputStream);
            return inputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void closeAllStreams() {
        for (InputStream inputStream : inputStreams) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
