package me.ironblock.natives.test;

import com.sun.jna.WString;
import me.ironblock.automusicplayer.nativeInvoker.WindowsMessage;
import me.ironblock.automusicplayer.resource.ExternalResourceLoaderController;

import java.io.File;

/**
 * @author :Iron__Block
 * @Date :2022/2/12 20:32
 */
public class TestLoadNative {
    public static void main(String[] args) {
        try {
            WindowsMessage a = WindowsMessage.INSTANCE;
            WString string = a.listWindows();
            String[] tmp = string.toString().split(";");
            for (String s : tmp) {
                System.out.println(s);
            }
        } catch (Exception e) {
        }

    }
}
