package me.ironblock.automusicplayer.nativeInvoker;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.WString;
import me.ironblock.automusicplayer.resource.ExternalResourceLoaderController;


/**
 * @author :Iron__Block
 * @Date :2022/2/12 19:57
 */
public interface WindowsMessage extends Library{
    WindowsMessage INSTANCE = ExternalResourceLoaderController.loadDll("WindowMessageInvoke",WindowsMessage.class);
//    WindowsMessage INSTANCE = Native.load("WindowMessageInvoke",WindowsMessage.class);

    boolean sendKeyBoardMessageToWindow(WString windowName, int key, int state);
    WString listWindows();

}

