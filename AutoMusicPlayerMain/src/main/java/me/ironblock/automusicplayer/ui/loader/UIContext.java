package me.ironblock.automusicplayer.ui.loader;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :Iron__Block
 * @Date :2022/2/17 2:28
 */
public class UIContext {
    private final Map<String, Frame> windowFrames = new HashMap<>();
    private final Map<String, Component> components = new HashMap<>();
//    private final Map<String, Component> components = new HashMap<>();

    public void addWindowFrame(String name,Frame windowFrame){
        windowFrames.put(name,windowFrame);
    }
    public void addComponents(String name,Component component){
        components.put(name,component);
    }

    public Frame getFrameFromName(String name){
        return windowFrames.get(name);
    }

    public Component getComponentFromName(String name){
        return components.get(name);
    }

    public Map<String, Frame> getWindowFrames() {
        return windowFrames;
    }

    public Map<String, Component> getComponents() {
        return components;
    }
}
