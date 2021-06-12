package me.ironblock.genshinimpactmusicplayer.musicPlayer;

import me.ironblock.genshinimpactmusicplayer.music.MidiMusic;
import me.ironblock.genshinimpactmusicplayer.note.MidiNoteMessage;
import me.ironblock.genshinimpactmusicplayer.utils.KeyMap;

import java.awt.*;

public class MidiMusicPlayer extends AbstractMusicPlayer<MidiMusic, MidiNoteMessage>{
    private Robot robot;
    @Override
    public void playNote(MidiNoteMessage note) {

        switch (note.command){
            case 0: //note_on
                robot.keyPress(KeyMap.getVKCodeFromNoteOctaveAndName(note));
                break;
            case 1: //note off
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
