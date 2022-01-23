package me.ironblock.automusicplayer.note;

import java.util.Objects;

/**
 * @author :Iron__Block
 * @Date :2022/1/15 21:52
 */
public class KeyAction {
    /**
     * the key is pressed or released
     */
    private boolean command;
    /**
     * the key's vk_code
     */
    private int key;

    public KeyAction(boolean command, int key) {
        this.command = command;
        this.key = key;
    }

    public boolean getCommand() {
        return command;
    }

    public void setCommand(boolean command) {
        this.command = command;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyAction keyAction = (KeyAction) o;
        return command == keyAction.command && key == keyAction.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, key);
    }

    @Override
    public String toString() {
        return "KeyAction{" +
                "command=" + command +
                ", key=" + key +
                '}';
    }
}
