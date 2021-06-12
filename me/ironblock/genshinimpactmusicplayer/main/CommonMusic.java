//package me.ironblock.genshinimpactmusicplayer.main;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class CommonMusic {
//    public int tps;
//    public long length;
//    public int thisTick;
//    public List<CommonMusicTrack> musicTrackList = new ArrayList<>();
//    public List<KeyMessage> getNextTickKeyMessages(){
//        List<KeyMessage> keyMessages = new ArrayList<>();
//        while (keyMessages.isEmpty()){
//            for (CommonMusicTrack musicTrack : musicTrackList) {
//                keyMessages.addAll(musicTrack.getNextTickKeys());
//            }
//            thisTick++;
//        }
//        System.out.println(toString(keyMessages.toArray(new KeyMessage[0])));
//        return keyMessages;
//    }
//    public void resetCurrent(){
//        thisTick = 0;
//        musicTrackList.forEach(CommonMusicTrack::resetCurrent);
//    }
//    public String toString(KeyMessage... keyMessages){
//        StringBuilder sb = new StringBuilder();
//        for (KeyMessage keyMessage : keyMessages) {
//            sb.append((char) keyMessage.key).append(",");
//        }
//        return sb.toString();
//    }
//}
