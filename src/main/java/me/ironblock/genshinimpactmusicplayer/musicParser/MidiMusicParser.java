package me.ironblock.genshinimpactmusicplayer.musicParser;

import me.ironblock.genshinimpactmusicplayer.keyMap.KeyMap;
import me.ironblock.genshinimpactmusicplayer.music.Music;
import me.ironblock.genshinimpactmusicplayer.note.KeyAction;

import javax.sound.midi.*;
import java.io.InputStream;

/**
 * midi音乐解析器
 */
public class MidiMusicParser extends AbstractMusicParser{
    /**
     * 解析音乐
     *
     * @param musicStream midi文件的路径
     * @return 解析出的音乐
     * @throws Exception 抛出的异常
     */
    @Override
    public Music parseMusic(InputStream musicStream, KeyMap keyMap) throws Exception {
        Music music = new Music();

        Sequence sequence = MidiSystem.getSequence(musicStream);
        music.length = sequence.getTickLength();
        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    int key = sm.getData1();
                    int octave = (key / 12) - 1;
                    int note = key % 12;
                    KeyAction keyAction = new KeyAction(sm.getCommand()==0x90, keyMap.getNoteKey(octave, note));
                    music.addNoteToTick(event.getTick(),keyAction);
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
}
