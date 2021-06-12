package me.ironblock.genshinimpactmusicplayer.note;

public class MidiNoteMessage extends AbstractNoteMessage{
    public int command;   //0 = note_on,1 = note_off
    public int key;
    public int octave;
    public int note;
    public int velocity;
}
