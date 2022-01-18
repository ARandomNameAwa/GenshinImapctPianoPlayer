package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.music.TrackMusic;
import me.ironblock.genshinimpactmusicplayer.note.NoteInfo;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;
//I:\midiMusics\千本樱.mid

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
    public TrackMusic parseMusic(InputStream musicStream) {
        try {
            TrackMusic trackMusic = new TrackMusic();

            Sequence sequence = MidiSystem.getSequence(musicStream);
            trackMusic.realDuration = ((double) sequence.getMicrosecondLength()) / 1_000_000;
            trackMusic.length = sequence.getTickLength() / tickDivision;
            trackMusic.tpsReal = (int) (trackMusic.length / trackMusic.realDuration);
            System.out.println("realDuration:" + trackMusic.realDuration);
            System.out.println("tpsReal:" + trackMusic.tpsReal);
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
                            trackMusic.putNode(i, (int) (event.getTick() / tickDivision), noteInfo);
                        }
                    }
                }
            }
            return trackMusic;
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
        return null;

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
    public int totalNoteInaccuracy(InputStream musicStream, KeyMap keyMap, int tune) {
        try {
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
                            totalInaccuracy += keyMap.getNoteInaccuracy(noteInfo);
                        }
                    }
                }
            }
            return totalInaccuracy;
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
