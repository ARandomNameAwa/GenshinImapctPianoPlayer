package me.ironblock.automusicplayer.resource;

import me.ironblock.automusicplayer.keymap.KeyMapLoader;

import java.io.File;


public class ExternalResourceLoaderController {

    public static final File RUN_PATH;
    public static final File KEY_MAP_PATH;

//    public static final File mouseMapPath;

    public static final String[] PREINSTALLED_KEY_MAPS = new String[]{"nbsKeyMap.txt", "GenshinImpactKeyMap.txt", "everyonePiano.txt"};
    public static final String[] PREINSTALLED_KEY_MAP_NAMES = new String[]{"NbsEditor", "GenshinImpact", "everyonePiano"};
    private static ExternalResourceLoaderController instance;

    static {
        RUN_PATH = new File(ExternalResourceLoaderController.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile();
        KEY_MAP_PATH = new File(RUN_PATH, "keyMaps");
//        mouseMapPath = new File(runPath, "mouseMaps");
    }

    public static ExternalResourceLoaderController getInstance() {
        if (instance == null) {
            instance = new ExternalResourceLoaderController();
        }
        return instance;
    }

    /**
     * Load all
     */
    public void loadAll() {
        loadKeyMaps();
        loadMouseMaps();
    }

    /**
     * Load keyMap
     */
    private void loadKeyMaps() {
        System.out.println("Loading default keyMap....");
        for (int i = 0; i < PREINSTALLED_KEY_MAPS.length; i++) {
            try {
                KeyMapLoader.getInstance().loadKeyMapFromFile(ExternalResourceLoaderController.class.getClassLoader().getResourceAsStream(PREINSTALLED_KEY_MAPS[i]), PREINSTALLED_KEY_MAP_NAMES[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("KeyMap Path:" + KEY_MAP_PATH);
        if (!KEY_MAP_PATH.exists()) {
            KEY_MAP_PATH.mkdirs();
        }
        File[] keyMaps = KEY_MAP_PATH.listFiles();

        if (keyMaps != null && keyMaps.length > 0) {
            for (File keyMap : keyMaps) {
                if (keyMap.getName().endsWith("txt")) {
                    System.out.println("Loading KeyMap " + keyMap);
                    KeyMapLoader.getInstance().loadKeyMapFromFile(keyMap.getAbsolutePath());
                }
            }
        }
    }

    /**
     * TODO:load mouse maps
     */
    private void loadMouseMaps() {

    }

}
