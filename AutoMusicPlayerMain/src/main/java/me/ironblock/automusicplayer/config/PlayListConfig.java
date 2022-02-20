package me.ironblock.automusicplayer.config;



import com.google.gson.Gson;
import me.ironblock.automusicplayer.resource.ExternalResourceLoaderController;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :Iron__Block
 * @Date :2022/2/20 21:40
 */
public class PlayListConfig {
    public static final File configFile = new File(ExternalResourceLoaderController.CONFIG_PATH,"PlayListConfig.json");
    public static Map<String, String> fileNameAndPathMap = new HashMap<>();
    private final Gson gson = new Gson();
    public void readFromConfig() throws IOException {
        if (!configFile.exists()){
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
        }else{
            FileInputStream fileInputStream = new FileInputStream(configFile);
            String jsonContent = IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);

            fileNameAndPathMap = gson.fromJson(jsonContent, Map.class);
            IOUtils.closeQuietly(fileInputStream);
        }
    }
    public Map<String, String> getFileNameAndPathMap() {
        return fileNameAndPathMap;
    }
    public void writeToConfig() throws IOException{
        String jsonContent = gson.toJson(fileNameAndPathMap);
        FileOutputStream fileOutputStream = new FileOutputStream(configFile);
        IOUtils.write(jsonContent,fileOutputStream,StandardCharsets.UTF_8);
        IOUtils.closeQuietly(fileOutputStream);
    }
}
