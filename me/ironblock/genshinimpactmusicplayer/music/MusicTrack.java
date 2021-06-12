//package me.ironblock.genshinimpactmusicplayer.music;
//
//import me.ironblock.genshinimpactmusicplayer.main.KeyMessage;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MusicTrack {
//    private long currentTick = -1;
//
//    public Map<Long,List<KeyMessage>> noteMap = new LinkedHashMap<>();
//    public List<KeyMessage> getNextTickKeys(){
//        currentTick++;
//        List<KeyMessage> list = noteMap.get(currentTick);
//        if (list==null||list.isEmpty()){
//            return new ArrayList<>();
//
//        }else{
//            return list;
//        }
//    }
//    public void addKeyMessage(long tick,KeyMessage keyMessage){
//        if (!noteMap.containsKey(tick)){
//            noteMap.put(tick, new ArrayList<>());
//        }
//        List<KeyMessage> list = noteMap.get(tick);
//        list.add(keyMessage);
//        noteMap.put(tick, list);
//
//    }
//
//}
