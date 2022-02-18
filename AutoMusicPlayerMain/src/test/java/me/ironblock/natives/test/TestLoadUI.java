package me.ironblock.natives.test;

import me.ironblock.automusicplayer.ui.loader.UIContext;
import me.ironblock.automusicplayer.ui.loader.UILoader;

/**
 * @author :Iron__Block
 * @Date :2022/2/18 13:13
 */
public class TestLoadUI {
    public static void main(String[] args) {
        try {
            UILoader.loadUIFromPackage("me.ironblock.automusicplayer.ui.frames");
            UILoader.UI.getFrameFromName("mainFrame").setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
