package me.ironblock.automusicplayer.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :Iron__Block
 * @Date :2022/2/17 2:10
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WindowComponent {
    String name();
    int x();
    int y();
    int width();
    int height();
    String initPara() default "";
    String parent() default "";
    String initializer() default "";
    String[] listeners() default {};
    String background() default "";

}
