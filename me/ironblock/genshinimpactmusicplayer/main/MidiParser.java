package me.ironblock.genshinimpactmusicplayer.main;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class MidiParser {
    public static int TIMEDIVIDEBY = 100;
    public Music parseMidi(String fileName){
        try {
            Music music = new Music();

            Sequence sequence = MidiSystem.getSequence(new File(fileName));
            music.tps = sequence.getResolution();
            music.length = sequence.getTickLength()/30;
            int trackNumber = 0;
            for (Track track : sequence.getTracks()) {
                trackNumber++;
                System.out.println("Track " + trackNumber + ": size = " + track.size());
                System.out.println();
                MusicTrack theTrack = new MusicTrack();
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    System.out.print("@" + event.getTick() + " ");
                    MidiMessage message = event.getMessage();
                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        System.out.print("Channel: " + sm.getChannel() + " ");
                        KeyMessage keyMessage = new KeyMessage();
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        int velocity = sm.getData2();
                        keyMessage.command = sm.getCommand();
                        keyMessage.key = key;
                        keyMessage.octave = octave;
                        keyMessage.note = note;
                        keyMessage.velocity = velocity;
                        theTrack.addKeyMessage(event.getTick()/TIMEDIVIDEBY, keyMessage);
                        System.out.println("Note on, "  + octave + " key=" + key + " velocity: " + velocity);

                    } else {
                        System.out.println("Other message: " + message.getClass());
                    }
                }
                music.musicTrackList.add(theTrack);

                System.out.println();
            }
            return music;
        } catch (InvalidMidiDataException|IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
