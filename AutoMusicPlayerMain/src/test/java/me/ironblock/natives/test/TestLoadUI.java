package me.ironblock.natives.test;

import com.alee.laf.WebLookAndFeel;
import me.ironblock.automusicplayer.playcontroller.MusicParserRegistry;
import me.ironblock.automusicplayer.resource.ExternalResourceLoaderController;
import me.ironblock.automusicplayer.ui.ControllerFrame;
import me.ironblock.automusicplayer.ui.frames.MainFrame;
import me.ironblock.automusicplayer.ui.loader.UIContext;
import me.ironblock.automusicplayer.ui.loader.UILoader;
import me.ironblock.automusicplayer.utils.UnhandledExceptionHandler;

import javax.swing.*;

/**
 * @author :Iron__Block
 * @Date :2022/2/18 13:13
 */
public class TestLoadUI {
    public static void main(String[] args) {
        UnhandledExceptionHandler.setHandler();
        SwingUtilities.invokeLater (() -> {
                UnhandledExceptionHandler.setHandler();

                // Install WebLaF as application LaF
                WebLookAndFeel.install ();
                // You can also specify preferred skin right-away
//                WebLookAndFeel.install ( WebDarkSkin.class );
                // You can also do that in one of the old-fashioned ways
                // UIManager.setLookAndFeel ( new WebLookAndFeel () );
                // UIManager.setLookAndFeel ( "com.alee.laf.WebLookAndFeel" );
                // UIManager.setLookAndFeel ( WebLookAndFeel.class.getCanonicalName () );

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
                    UILoader.loadUIFromPackage("me.ironblock.automusicplayer.ui.frames");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UILoader.UI.getFrameFromName("mainFrame").setVisible(true);
                // Initialize your application once you're done setting everything up

                // You can also use Web* components to get access to some extended WebLaF features
                // WebFrame frame = ...
            });
    }
}
