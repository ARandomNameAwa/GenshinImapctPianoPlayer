//package me.ironblock.genshinimpactmusicplayer.main;
//
//import me.ironblock.genshinimpactmusicplayer.music.Music;
//import me.ironblock.genshinimpactmusicplayer.ui.MainFrame;
//
//public class MidiPlayer {
//
//    public static final int NOTE_ON = 0x90;
//    public static final int NOTE_OFF = 0x80;
////F:\dowlaods\Only my railgun-とある科学の超電磁砲op.mid
//
//    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
//
//    public static void play(String filePath) throws Exception {
//        String string = filePath;
//        Music music = new MidiParser().parseMidi(string);
//        System.out.println("5秒后开始");
//        Thread.sleep(5000);
//        MainFrame.setAsTitle("Playing.....");
//        new MusicPlayer().playMusic(music);
//
//    }
//}
//
//
//
//
