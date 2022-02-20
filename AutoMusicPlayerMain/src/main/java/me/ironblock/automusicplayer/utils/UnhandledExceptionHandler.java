package me.ironblock.automusicplayer.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author :Iron__Block
 * @Date :2022/2/20 13:02
 */
public class UnhandledExceptionHandler implements Thread.UncaughtExceptionHandler{
    public static final Logger LOGGER = LogManager.getLogger(UnhandledExceptionHandler.class);
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.fatal("Unhandled exception thrown:",e);
    }

    public static void setHandler(){
        Thread.setDefaultUncaughtExceptionHandler(new UnhandledExceptionHandler());
    }

}
