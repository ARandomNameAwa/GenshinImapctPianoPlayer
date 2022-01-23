package me.ironblock.automusicplayer.music.parser;

import me.ironblock.automusicplayer.music.TrackMusic;

import java.io.InputStream;


public abstract class AbstractMusicParser {
    /**
     * Parse music
     *
     * @param musicStream music file stream
     * @return TrackMusic from the musicStream
     */
    public abstract TrackMusic parseMusic(InputStream musicStream);


    /**
     * Get the suffix of the file which the parser parses
     *
     * @return suffix
     */
    public abstract String getMusicFileSuffix();


    /**
     * Get the file type name which the parser parses
     *
     * @return file type name
     */
    public abstract String getMusicFileTypeName();

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
