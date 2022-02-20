package me.ironblock.automusicplayer.config;

import com.google.gson.Gson;
import me.ironblock.automusicplayer.music.TuneStep;
import me.ironblock.automusicplayer.resource.ExternalResourceLoaderController;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author :Iron__Block
 * @Date :2022/2/20 22:20
 */
public class TuneStepConfig {
    public static final File tuneStepConfigFolder = new File(ExternalResourceLoaderController.CONFIG_PATH, "tuneSteps");
    private final Gson gson = new Gson();
    public TuneStep getTuneStepFromConfig(String name)throws IOException{
        tuneStepConfigFolder.mkdirs();
        File file = new File(tuneStepConfigFolder, name + ".json");
        if (file.exists()){
            String jsonContent = IOUtils.toString(file.toURI(), StandardCharsets.UTF_8);
            return gson.fromJson(jsonContent, TuneStep.class);
        }else{
            return null;
        }
    }

    public void writeTuneStepToConfig(String name,TuneStep tuneStep) throws IOException{
        tuneStepConfigFolder.mkdirs();
        File file = new File(tuneStepConfigFolder, name + ".json");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        IOUtils.write(gson.toJson(tuneStep),fileOutputStream,StandardCharsets.UTF_8);
        IOUtils.closeQuietly(fileOutputStream);
    }

}
