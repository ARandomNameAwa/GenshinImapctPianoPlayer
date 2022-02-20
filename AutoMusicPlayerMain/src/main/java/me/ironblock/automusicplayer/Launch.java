package me.ironblock.automusicplayer;

import com.alee.laf.WebLookAndFeel;
import me.ironblock.automusicplayer.nativeInvoker.WindowsMessage;
import me.ironblock.automusicplayer.playcontroller.MusicParserRegistry;
import me.ironblock.automusicplayer.resource.ExternalResourceLoaderController;
import me.ironblock.automusicplayer.ui.ControllerFrame;
import me.ironblock.automusicplayer.ui.frames.MainFrame;
import me.ironblock.automusicplayer.ui.loader.UILoader;
import me.ironblock.automusicplayer.utils.UnhandledExceptionHandler;

import javax.swing.*;


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

        SwingUtilities.invokeLater (() -> {
            UnhandledExceptionHandler.setHandler();
            long start1 = System.currentTimeMillis();
            // Install WebLaF as application LaF
            WebLookAndFeel.install ();
            long start2 = System.currentTimeMillis();
            MainFrame.LOGGER.info("Loading look and feel took "+(start2-start1)+" ms.");

            // You can also configure other WebLaF managers as you like now
            // StyleManager
            // SettingsManager
            // LanguageManager
            // ...
            try {
                //Load resources
                ExternalResourceLoaderController.getInstance().loadAll();
                //Load parsers
                MusicParserRegistry.init();
                //Init controller frame
                UnhandledExceptionHandler.setHandler();
                UILoader.loadUIFromPackage("me.ironblock.automusicplayer.ui.frames");
                long start3 = System.currentTimeMillis();
                MainFrame.LOGGER.info("Loading UI took "+(start3-start2)+" ms.");
                UILoader.UI.getFrameFromName("mainFrame").setVisible(true);
            } catch (Exception e) {
                MainFrame.LOGGER.fatal("Unhandled exception:",e);
            }
        });
    }


}
