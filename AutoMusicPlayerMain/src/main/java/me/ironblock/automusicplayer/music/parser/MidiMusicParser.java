package me.ironblock.automusicplayer.music.parser;

import me.ironblock.automusicplayer.music.TrackMusic;
import me.ironblock.automusicplayer.note.NoteInfo;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * midi music parser
 */
public class MidiMusicParser extends AbstractMusicParser {
    /**
     * Combine every tickDivision midi ticks to 1 TrackMusic tick
     */
    private static final int TICK_DIVISION = 10;

    /**
     * Parse the music
     *
     * @param musicStream midi file stream
     * @return music from the stream
     */
    @Override
    public TrackMusic parseMusic(InputStream musicStream) {
        try {
            TrackMusic trackMusic = new TrackMusic();

            Sequence sequence = MidiSystem.getSequence(musicStream);
            trackMusic.realDuration = ((double) sequence.getMicrosecondLength()) / 1_000_000;
            trackMusic.length = sequence.getTickLength() / TICK_DIVISION;
            trackMusic.tpsReal = (int) (trackMusic.length / trackMusic.realDuration);
            System.out.println("realDuration:" + trackMusic.realDuration);
            System.out.println("tpsReal:" + trackMusic.tpsReal);
            for (int j = 0; j < sequence.getTracks().length; j++) {
                Track track = sequence.getTracks()[j];
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
                            trackMusic.putNode(j, (int) (event.getTick() / TICK_DIVISION), noteInfo);
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


}
