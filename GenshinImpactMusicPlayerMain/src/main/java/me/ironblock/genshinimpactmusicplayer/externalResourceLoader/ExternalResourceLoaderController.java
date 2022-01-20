package me.ironblock.genshinimpactmusicplayer.externalResourceLoader;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMapLoader;

import java.io.File;

/**
 * 外部资源加载器
 */
public class ExternalResourceLoaderController {
    /**
     * 目前只有keyMapPath和runPath有用
     */
    public static final File runPath;
    public static final File keyMapPath;
    public static final File mouseMapPath;
    public static final String[] preinstalledKeyMaps = new String[]{"nbsKeyMap.txt", "GenshinImpactKeyMap.txt"};
    public static final String[] preinstalledKeyMapNames = new String[]{"NbsEditor", "GenshinImpact"};
    private static ExternalResourceLoaderController instance;

    static {
        runPath = new File(ExternalResourceLoaderController.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile();
        keyMapPath = new File(runPath, "keyMaps");
        mouseMapPath = new File(runPath, "mouseMaps");
    }

    public static ExternalResourceLoaderController getInstance() {
        if (instance == null) {
            instance = new ExternalResourceLoaderController();
        }
        return instance;
    }

    /**
     * 加载全部外部资源
     */
    public void loadAll() {
        loadKeyMaps();
        loadMouseMaps();
    }

    /**
     * 加载keyMap
     */
    private void loadKeyMaps() {
        System.out.println("Loading default keyMap....");
        for (int i = 0; i < preinstalledKeyMaps.length; i++) {
            try {
                KeyMapLoader.getInstance().loadKeyMapFromFile(ExternalResourceLoaderController.class.getClassLoader().getResourceAsStream(preinstalledKeyMaps[i]), preinstalledKeyMapNames[i]);
            } catch (Exception e) {


                e.printStackTrace();
            }
        }
        System.out.println("KeyMap Path:" + keyMapPath);
        if (!keyMapPath.exists()) {
            keyMapPath.mkdirs();
        }
        File[] keyMaps = keyMapPath.listFiles();

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
     * 以后做
     */
    private void loadMouseMaps() {

    }

    /**
     * 以后做
     */
    private void loadPlugin() {

    }

}
