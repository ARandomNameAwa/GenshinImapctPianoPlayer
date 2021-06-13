package me.ironblock.genshinimpactmusicplayer;

import me.ironblock.genshinimpactmusicplayer.externalResourceLoader.ExternalResourceLoaderController;
import me.ironblock.genshinimpactmusicplayer.playController.MusicParserAndPlayerRegistry;
import me.ironblock.genshinimpactmusicplayer.ui.ControllerFrame;

public class Launch {
    public static void main(String[] args) {
        ExternalResourceLoaderController.getInstance().loadAll();
        MusicParserAndPlayerRegistry.init();
        ControllerFrame.init();
    }





}
