package me.ironblock.automusicplayer.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

/**
 * @author :Iron__Block
 * @Date :2022/2/18 14:30
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
    String name();
    Class<? extends EventListener> parent();
}
