package me.ironblock.mousemappingmaker.files;

public class FileManager {
    private static FileManager instance;

    public static FileManager getInstance() {
        if (instance == null)
            instance = new FileManager();
        return instance;
    }

    public void saveCurrentFile(){

    }
}
