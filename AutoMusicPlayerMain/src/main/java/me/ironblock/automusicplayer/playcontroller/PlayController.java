package me.ironblock.automusicplayer.playcontroller;

import me.ironblock.automusicplayer.keymap.KeyMap;
import me.ironblock.automusicplayer.music.KeyActionMusic;
import me.ironblock.automusicplayer.music.TrackMusic;
import me.ironblock.automusicplayer.music.TuneStep;
import me.ironblock.automusicplayer.music.parser.AbstractMusicParser;
import me.ironblock.automusicplayer.music.player.MusicPlayer;

import java.io.InputStream;


public class PlayController {
    private final MusicPlayer player = new MusicPlayer();
    private TrackMusic trackMusic;
    private KeyMap activeKeyMap;
    private String currentMusicName = "";

    public void prepareMusicPlayed(InputStream file, AbstractMusicParser parser, String name) {
        if (!currentMusicName.equals(name)) {
            trackMusic = parser.parseMusic(file);
            currentMusicName = name;
        }
    }


    public void startPlay(TuneStep tune) {
        KeyActionMusic keyActionMusic = KeyActionMusic.getFromTrackMusic(trackMusic, activeKeyMap, tune);
        this.player.playMusic(keyActionMusic);
    }


    public void stopPlay() {
        player.stop();
    }


    public void switchPause() {
        player.switchPause();
    }

    /**
     * Auto tune
     *
     * @param minOctave        min octave
     * @param maxOctave        max octave
     * @param tracksOctaveSame if every track has the same octave
     * @return the best tune
     */
    public TuneStep autoTune(int minOctave, int maxOctave, boolean tracksOctaveSame) {
        return trackMusic.autoTune(activeKeyMap, minOctave, maxOctave, tracksOctaveSame);
    }

    public void setActiveKeyMap(KeyMap activeKeyMap) {
        this.activeKeyMap = activeKeyMap;
    }

    public void jumpToTick(int tick) {
        player.getKeyActionMusicPlayed().jumpToTick(tick);
    }

    public int getTotalTick() {
        return (int) player.getKeyActionMusicPlayed().length;
    }

    public int getSpeed() {
        return player.getSpeed();
    }

    public void setSpeed(double speed) {
        player.setSpeed((int) (speed * trackMusic.tpsReal));
    }

    public boolean isPlaying() {
        return trackMusic != null && player.getKeyActionMusicPlayed() != null;
    }

    public TrackMusic getTrackMusic() {
        return trackMusic;
    }

    public boolean trackMusicLoaded() {
        return trackMusic != null;
    }

}
