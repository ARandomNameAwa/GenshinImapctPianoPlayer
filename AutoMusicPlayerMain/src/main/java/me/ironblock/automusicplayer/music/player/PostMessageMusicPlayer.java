package me.ironblock.automusicplayer.music.player;

import com.sun.jna.WString;
import me.ironblock.automusicplayer.nativeInvoker.WindowsMessage;
import me.ironblock.automusicplayer.note.KeyAction;

/**
 * @author :Iron__Block
 * @Date :2022/2/13 1:17
 */
public class PostMessageMusicPlayer extends AbstractMusicPlayer{

    private String windowTitle;

    @Override
    public void playNote(KeyAction note) {
        if (!WindowsMessage.INSTANCE.sendKeyBoardMessageToWindow(new WString(windowTitle), note.getKey(), (note.getCommand() ? 1 : 0))){
            AbstractMusicPlayer.LOGGER.error("Failed to post message to the window:"+windowTitle);
        }
    }

    public void setPostMessageWindow(String windowTitle){
        this.windowTitle = windowTitle;
    }
}
