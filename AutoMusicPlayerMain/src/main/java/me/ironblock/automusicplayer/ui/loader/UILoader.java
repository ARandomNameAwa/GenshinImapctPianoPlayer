package me.ironblock.automusicplayer.ui.loader;

import me.ironblock.automusicplayer.ui.annotations.WindowComponent;
import me.ironblock.automusicplayer.ui.annotations.WindowFrame;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author :Iron__Block
 * @Date :2022/2/17 2:23
 */
public class UILoader {
    public static UIContext loadUIFromPackage(String packageName){
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> windowFrames = reflections.getTypesAnnotatedWith(WindowFrame.class);
        Set<Field> windowComponents  = reflections.getFieldsAnnotatedWith(WindowComponent.class);
        return null;
    }
}
