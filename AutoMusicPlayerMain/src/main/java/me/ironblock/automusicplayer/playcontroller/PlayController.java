package me.ironblock.automusicplayer.playcontroller;

import me.ironblock.automusicplayer.keymap.KeyMap;
import me.ironblock.automusicplayer.music.KeyActionMusic;
import me.ironblock.automusicplayer.music.TrackMusic;
import me.ironblock.automusicplayer.music.TuneStep;
import me.ironblock.automusicplayer.music.parser.AbstractMusicParser;
import me.ironblock.automusicplayer.music.player.AbstractMusicPlayer;
import me.ironblock.automusicplayer.music.player.PostMessageMusicPlayer;
import me.ironblock.automusicplayer.music.player.RobotMusicPlayer;

import java.io.InputStream;


public class PlayController {
    private final AbstractMusicPlayer postMessagePlayer = new PostMessageMusicPlayer();
    private final AbstractMusicPlayer robotPlayer = new RobotMusicPlayer();
    private TrackMusic trackMusic;
    private KeyMap activeKeyMap;
    private String currentMusicName = "";
    private boolean usePostMessage = false;
    private TuneStep tuneStep;


    public void loadMusicWithParser(InputStream file, AbstractMusicParser parser, String name) {
        if (!currentMusicName.equals(name)) {
            trackMusic = parser.parseMusic(file);
            currentMusicName = name;
        }
    }
    public void setTuneStep(TuneStep tuneStepIn){
        this.tuneStep = tuneStepIn;
    }


    public void startPlay(TuneStep tune) {
        KeyActionMusic keyActionMusic = KeyActionMusic.getFromTrackMusic(trackMusic, activeKeyMap, tune);
        if (usePostMessage){
            this.postMessagePlayer.playMusic(keyActionMusic);
        }else{
            this.robotPlayer.playMusic(keyActionMusic);
        }
    }

    public void startPlay(){
        KeyActionMusic keyActionMusic = KeyActionMusic.getFromTrackMusic(trackMusic, activeKeyMap, tuneStep);
        if (usePostMessage){
            this.postMessagePlayer.playMusic(keyActionMusic);
        }else{
            this.robotPlayer.playMusic(keyActionMusic);
        }
    }


    public void stopPlay() {
        this.postMessagePlayer.stop();
        this.robotPlayer.stop();
    }


    public void switchPause() {
        if (usePostMessage){
            this.postMessagePlayer.switchPause();
        }else{
            this.robotPlayer.switchPause();
        }
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
        if (usePostMessage){
            this.postMessagePlayer.getKeyActionMusicPlayed().jumpToTick(tick);
        }else{
            this.robotPlayer.getKeyActionMusicPlayed().jumpToTick(tick);
        }
    }

    public int getTotalTick() {
        if (usePostMessage){
           return (int) this.postMessagePlayer.getKeyActionMusicPlayed().length;
        }else{
            return (int) this.robotPlayer.getKeyActionMusicPlayed().length;
        }
    }

    public int getSpeed() {
        if (usePostMessage){
            return this.postMessagePlayer.getSpeed();
        }else{
            return this.robotPlayer.getSpeed();
        }
    }

    public void setSpeed(double speed) {
        if (usePostMessage){
            postMessagePlayer.setSpeed((int) (speed * trackMusic.tpsReal));
        }else{
            robotPlayer.setSpeed((int) (speed * trackMusic.tpsReal));
        }
    }

    public boolean isPlaying() {
        boolean musicPlay = postMessagePlayer.isPlaying() || robotPlayer.isPlaying();
        boolean playerPlaying = postMessagePlayer.getKeyActionMusicPlayed() != null|| robotPlayer.getKeyActionMusicPlayed() != null;
        return trackMusic != null && playerPlaying&&musicPlay;
    }

    public boolean isPaused(){
        if (usePostMessage){
            return postMessagePlayer.isPaused();
        }else{
            return robotPlayer.isPaused();
        }
    }

    public TrackMusic getTrackMusic() {
        return trackMusic;
    }

    public boolean trackMusicLoaded() {
        return trackMusic != null;
    }

    public void setPostMessage(boolean postMessage){
        this.usePostMessage = postMessage;
    }

    public void setPostMessageWindow(String windowTitle){
        ((PostMessageMusicPlayer) postMessagePlayer).setPostMessageWindow(windowTitle);
    }
}
