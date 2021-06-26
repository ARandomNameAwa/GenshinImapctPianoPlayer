package me.ironblock.genshinimpactmusicplayer;

import me.ironblock.genshinimpactmusicplayer.externalResourceLoader.ExternalResourceLoaderController;
import me.ironblock.genshinimpactmusicplayer.playController.MusicParserAndPlayerRegistry;
import me.ironblock.genshinimpactmusicplayer.ui.ControllerFrame;

import java.awt.event.KeyEvent;

/**
 * 主类
 */
public class Launch {
    /**
     * 启动
     */
    public static void main(String[] args) {

        //加载外部资源
        ExternalResourceLoaderController.getInstance().loadAll();
        //加载解析器和演奏器
        MusicParserAndPlayerRegistry.init();
        //初始化控制窗口
        ControllerFrame.init();
    }


}
