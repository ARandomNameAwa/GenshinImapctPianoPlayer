package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.music.Music;
import me.ironblock.genshinimpactmusicplayer.note.KeyAction;
import me.ironblock.genshinimpactmusicplayer.note.NoteInfo;

import javax.sound.midi.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
//I:\midiMusics\【原神】雪山bgm.mid

/**
 * midi音乐解析器
 */
public class MidiMusicParser extends AbstractMusicParser {

    private static final int tickDivision = 10;
    private static final int noteDelay = 10;

    /**
     * 解析音乐
     *
     * @param musicStream midi文件的路径
     * @return 解析出的音乐
     * @throws Exception 抛出的异常
     */
    @Override
    public Music parseMusic(InputStream musicStream, KeyMap keyMap, int tune) throws Exception {
        Music music = new Music();

        Sequence sequence = MidiSystem.getSequence(musicStream);
        music.realDuration = ((double) sequence.getMicrosecondLength()) / 1_000_000;
        music.length = sequence.getTickLength() / tickDivision;
        music.tpsReal = (int) (music.length / music.realDuration);
        System.out.println("realDuration:" + music.realDuration);
        System.out.println("tpsReal:" + music.tpsReal);
        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    int key = sm.getData1();
                    int octave = (key / 12) - 1;
                    int note = key % 12;
                    if (sm.getCommand() == 0x90) {
                        NoteInfo noteInfo = new NoteInfo(octave, note);
                        noteInfo.addKey(tune);
                        int key1 = keyMap.getNoteKey(noteInfo.octave, noteInfo.note);
                        if (key1 != -1) {
                            KeyAction keyAction = new KeyAction(true, key1);
                            KeyAction keyAction1 = new KeyAction(false, key1);
                            music.addNoteToTick(event.getTick() / tickDivision, keyAction);
                            music.addNoteToTick(event.getTick() / tickDivision + noteDelay, keyAction1);
                            music.addNoteToTick(event.getTick() / tickDivision - noteDelay / 2, keyAction1);
                        }
                    }
                }
            }
        }
        return music;

    }

    @Override
    public String getMusicFileSuffix() {
        return "mid";
    }

    @Override
    public String getMusicFileTypeName() {
        return "MIDI";
    }

    @Override
    public int totalNoteInaccuracy(InputStream musicStream, KeyMap keyMap, int tune) throws Exception {
        int totalInaccuracy = 0;
        Sequence sequence = MidiSystem.getSequence(musicStream);
        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    int key = sm.getData1();
                    int octave = (key / 12) - 1;
                    int note = key % 12;
                    if (sm.getCommand() == 0x90) {
                        NoteInfo noteInfo = new NoteInfo(octave, note);
                        noteInfo.addKey(tune);
                        totalInaccuracy+= keyMap.getNoteInaccuracy(noteInfo);
                    }
                }
            }
        }
        return totalInaccuracy;
    }


}
