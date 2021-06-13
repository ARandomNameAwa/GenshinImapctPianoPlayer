package me.ironblock.genshinimpactmusicplayer.musicPlayer;

import me.ironblock.genshinimpactmusicplayer.music.MidiMusic;
import me.ironblock.genshinimpactmusicplayer.note.MidiNoteMessage;

import java.awt.*;

public class MidiMusicPlayer extends AbstractMusicPlayer<MidiMusic, MidiNoteMessage> {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    private Robot robot;

    @Override
    public void playNote(MidiNoteMessage note) {
        try {
            switch (note.command) {
                case NOTE_ON: //note_on
                    robot.keyPress(activeKeyMap.getNoteKey(note.octave, note.note));
                    break;
                case NOTE_OFF: //note off
                    robot.keyRelease(activeKeyMap.getNoteKey(note.octave, note.note));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(note);
            System.out.println(activeKeyMap.getNoteKey(note.octave, note.note));
        }


    }

    @Override
    public void preTick() {
        if (robot == null) {
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }
}
