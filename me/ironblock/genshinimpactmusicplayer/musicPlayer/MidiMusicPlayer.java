package me.ironblock.genshinimpactmusicplayer.musicPlayer;

import me.ironblock.genshinimpactmusicplayer.music.MidiMusic;
import me.ironblock.genshinimpactmusicplayer.note.MidiNoteMessage;
import me.ironblock.genshinimpactmusicplayer.utils.KeyMap;

import java.awt.*;

public class MidiMusicPlayer extends AbstractMusicPlayer<MidiMusic, MidiNoteMessage>{
    private Robot robot;
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    @Override
    public void playNote(MidiNoteMessage note) {
        switch (note.command){
            case NOTE_ON: //note_on
                robot.keyPress(KeyMap.getVKCodeFromNoteOctaveAndName(note));
                break;
            case NOTE_OFF: //note off
                robot.keyRelease(KeyMap.getVKCodeFromNoteOctaveAndName(note));
                break;
        }


    }

    @Override
    public void preTick() {
        if (robot==null){
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }
}
