package me.ironblock.automusicplayer.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :Iron__Block
 * @Date :2022/2/18 13:54
 * Methods annotated by this must be static
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Initializer {
    String name();

}
