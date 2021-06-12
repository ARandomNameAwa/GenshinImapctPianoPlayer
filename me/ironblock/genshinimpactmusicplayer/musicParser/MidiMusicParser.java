package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.music.AbstractMusic;
import me.ironblock.genshinimpactmusicplayer.music.MidiMusic;
import me.ironblock.genshinimpactmusicplayer.note.MidiNoteMessage;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class MidiMusicParser extends AbstractMusicParser<String,MidiNoteMessage> {
    @Override
    public AbstractMusic<MidiNoteMessage> parseMusic(String musicFile) {
        try {
            MidiMusic music = new MidiMusic();

            Sequence sequence = MidiSystem.getSequence(new File(musicFile));
            music.length = sequence.getTickLength()/30;
            int trackNumber = 0;
            for (Track track : sequence.getTracks()) {
                trackNumber++;
                System.out.println("Track " + trackNumber + ": size = " + track.size());
                System.out.println();
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    System.out.print("@" + event.getTick() + " ");
                    MidiMessage message = event.getMessage();
                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        System.out.print("Channel: " + sm.getChannel() + " ");
                        MidiNoteMessage keyMessage = new MidiNoteMessage();
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        int velocity = sm.getData2();
                        keyMessage.command = sm.getCommand();
                        keyMessage.key = key;
                        keyMessage.octave = octave;
                        keyMessage.note = note;
                        keyMessage.velocity = velocity;
                        music.addNoteToTrack(trackNumber,event.getTick(),keyMessage);
                        System.out.println("Note on, "  + octave + " key=" + key + " velocity: " + velocity);

                    } else {
                        System.out.println("Other message: " + message.getClass());
                    }
                }

                System.out.println();
            }
            return music;
        } catch (InvalidMidiDataException|IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
