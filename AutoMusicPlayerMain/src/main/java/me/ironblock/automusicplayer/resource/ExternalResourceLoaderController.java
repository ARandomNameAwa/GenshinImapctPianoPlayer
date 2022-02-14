package me.ironblock.automusicplayer.resource;

import com.sun.jna.Library;
import com.sun.jna.Native;
import me.ironblock.automusicplayer.keymap.KeyMapLoader;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;


public class ExternalResourceLoaderController {
    public static final Logger LOGGER = LogManager.getLogger(ExternalResourceLoaderController.class);

    public static boolean DEVELOPMENT_MODE;
    public static final File RUN_PATH;
    public static final File KEY_MAP_PATH;
    public static final File DLL_PATH;

    public static boolean dll_available;

//    public static final File mouseMapPath;

    public static final String[] PREINSTALLED_KEY_MAPS = new String[]{"nbsKeyMap.txt", "GenshinImpactKeyMap.txt", "everyonePiano.txt"};
    public static final String[] PREINSTALLED_KEY_MAP_NAMES = new String[]{"NbsEditor", "GenshinImpact", "everyonePiano"};
    private static ExternalResourceLoaderController instance;

    static {
        File codeSource = new File(ExternalResourceLoaderController.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        DEVELOPMENT_MODE = codeSource.isDirectory();
        if (DEVELOPMENT_MODE){
            LOGGER.info("Detected in development mode!");
            RUN_PATH = codeSource;
            DLL_PATH = new File(RUN_PATH.getParentFile().getParentFile().getParentFile(),"\\natives\\WindowMessageInvoke\\x64\\Release");
        }else{
            LOGGER.info("Detected in user mode!");
            RUN_PATH = codeSource.getParentFile();
            DLL_PATH = new File(RUN_PATH,"Dll");
        }
        KEY_MAP_PATH = new File(RUN_PATH, "keyMaps");
        LOGGER.info("Setting RUN_PATH to "+ RUN_PATH.getAbsolutePath());
        LOGGER.info("Setting KEY_MAP_PATH to "+ KEY_MAP_PATH.getAbsolutePath());
        LOGGER.info("Setting DLL_PATH to "+ DLL_PATH.getAbsolutePath());

        dll_available = true;
        String os = System.getProperty("os.name");
        if (!os.toLowerCase().startsWith("win")) {
            LOGGER.error("The os is not windows so dll can't be loaded.");
            dll_available = false;
        }
        if (!DLL_PATH.exists()){
            LOGGER.error("Dll path doesn't exist so dll can't be loaded.");
            dll_available = false;
        }

        if (!KEY_MAP_PATH.exists()){
            KEY_MAP_PATH.mkdirs();
            LOGGER.info("KEY_MAP_PATH doesn't exist so created the folder.");
        }



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
        LOGGER.info("Loading default keyMap....");
        for (int i = 0; i < PREINSTALLED_KEY_MAPS.length; i++) {
            try {
                KeyMapLoader.getInstance().loadKeyMapFromFile(ExternalResourceLoaderController.class.getClassLoader().getResourceAsStream(PREINSTALLED_KEY_MAPS[i]), PREINSTALLED_KEY_MAP_NAMES[i]);
            } catch (Exception e) {
                LOGGER.error("Failed to load keyMap:",e);
            }
        }
        if (!KEY_MAP_PATH.exists()) {
            KEY_MAP_PATH.mkdirs();
        }
        File[] keyMaps = KEY_MAP_PATH.listFiles();

        if (keyMaps != null && keyMaps.length > 0) {
            for (File keyMap : keyMaps) {
                if (keyMap.getName().endsWith("txt")) {
                    LOGGER.info("Loading KeyMap " + keyMap);
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

    /**
     * Load Dll
     * Copy the dll from the jar to /dlls directory.
     * @param name dllName
     */
    public static <T extends Library> T loadDll(String name, Class<T> clazz){
        if (dll_available) {
            File dllCopyTo = new File(DLL_PATH,name+".dll");
            if (!dllCopyTo.exists()){
                LOGGER.error("Dll not exists:"+dllCopyTo.getAbsolutePath());
            }
            return Native.load(dllCopyTo.getAbsolutePath(),clazz);
        }else{
            LOGGER.error("Can't load dll for some reason.");
            return null;
        }
    }


    public static void loadJar(File jarfile){
//        ExternalResourceLoaderController.class.getClassLoader()
    }
}
