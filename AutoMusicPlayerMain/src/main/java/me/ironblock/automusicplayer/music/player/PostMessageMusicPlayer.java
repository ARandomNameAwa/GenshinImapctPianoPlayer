package me.ironblock.automusicplayer.music.player;

import com.sun.jna.WString;
import me.ironblock.automusicplayer.nativeInvoker.WindowsMessage;
import me.ironblock.automusicplayer.note.KeyAction;

/**
 * @author :Iron__Block
 * @Date :2022/2/13 1:17
 */
public class PostMessageMusicPlayer extends AbstractMusicPlayer{



    @Override
    public void playNote(KeyAction note) {
        WindowsMessage.INSTANCE.sendKeyBoardMessageToWindow(new WString("原神"),note.getKey(),(note.getCommand()?1:0));
    }
}
