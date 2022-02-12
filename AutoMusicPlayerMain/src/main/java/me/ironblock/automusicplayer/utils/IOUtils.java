package me.ironblock.automusicplayer.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.rmi.runtime.Log;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class IOUtils {
    public static final Logger LOGGER = LogManager.getLogger(IOUtils.class);
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
            LOGGER.error("",e);
        }
        return "";

    }

    public static InputStream openStream(String in) {
        try {
            InputStream inputStream = new FileInputStream(in);
            inputStreams.add(inputStream);
            return inputStream;
        } catch (FileNotFoundException e) {
            LOGGER.error("",e);
        }
        return null;

    }

    public static void closeAllStreams() {
        for (InputStream inputStream : inputStreams) {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.error("",e);
            }
        }
    }
}
