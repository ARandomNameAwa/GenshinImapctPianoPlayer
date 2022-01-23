package me.ironblock.automusicplayer;

import me.ironblock.automusicplayer.playcontroller.MusicParserRegistry;
import me.ironblock.automusicplayer.resource.ExternalResourceLoaderController;
import me.ironblock.automusicplayer.ui.ControllerFrame;


/**
 * Main Class
 *
 * @author Administrator
 */
public class Launch {
    public static boolean DEBUG_MODE = false;

    /**
     * Launch
     */
    public static void main(String[] args) {
        //Load resources
        ExternalResourceLoaderController.getInstance().loadAll();
        //Load parsers
        MusicParserRegistry.init();
        //Init controller frame
        ControllerFrame.init();
    }


}
