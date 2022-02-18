package me.ironblock.automusicplayer.ui.annotations;

import javax.swing.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :Iron__Block
 * @Date :2022/2/17 2:06
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WindowFrame {
    String name();
    String title();
    int x() default 0;
    int y() default 0;
    int width();
    int height();
    boolean autoCenter();
    int defaultCloseOperation() default WindowConstants.EXIT_ON_CLOSE;
    String initializer() default "";
    String[] listeners() default {};
    String background() default "";

}
