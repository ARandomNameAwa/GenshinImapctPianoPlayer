package me.ironblock.automusicplayer.nativeInvoker;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.WString;


/**
 * @author :Iron__Block
 * @Date :2022/2/12 19:57
 */
public interface WindowsMessage extends Library{
    WindowsMessage INSTANCE = Native.load("",WindowsMessage.class);

    boolean sendKeyBoardMessageToWindow(WString windowName, int key, int state);
    WString[] listWindows();

}
