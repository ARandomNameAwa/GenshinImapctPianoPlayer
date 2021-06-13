package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.music.AbstractMusic;
import me.ironblock.genshinimpactmusicplayer.music.MidiMusic;
import me.ironblock.genshinimpactmusicplayer.note.MidiNoteMessage;

import javax.sound.midi.*;
import java.io.File;

/**
 * midi音乐解析器
 */
public class MidiMusicParser extends AbstractMusicParser<String, MidiNoteMessage> {
    /**
     * 解析音乐
     *
     * @param musicFile midi文件的路径
     * @return 解析出的音乐
     * @throws Exception 抛出的异常
     */
    @Override
    public AbstractMusic<MidiNoteMessage> parseMusic(String musicFile) throws Exception {
        MidiMusic music = new MidiMusic();

        Sequence sequence = MidiSystem.getSequence(new File(musicFile));
        music.length = sequence.getTickLength();
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            music.newTrack();
            trackNumber++;
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
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
                    music.addNoteToTrack(trackNumber - 1, event.getTick(), keyMessage);
                }
            }

            System.out.println();
        }
        return music;

    }
}
