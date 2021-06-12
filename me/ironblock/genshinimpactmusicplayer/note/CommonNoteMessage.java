package me.ironblock.genshinimpactmusicplayer.note;

import java.util.Objects;

public class CommonNoteMessage extends AbstractNoteMessage{
    public char key;
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CommonNoteMessage message = (CommonNoteMessage) o;
        return key == message.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "CommonKeyMessage{" + "key=" + (char)key+'}';
    }
}
