package me.ironblock.mousemappingmaker.ui.components;

import me.ironblock.mousemappingmaker.ui.MainFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * 代表一个文字对应一个坐标的窗口组件
 */
public class KeyPosComponent extends UIComponents{
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public static BufferedImage IMAGE;

    private final int shiningTimer = 50;
    private int shiningTime = 0;
    private boolean shining = false;
    private int cursor = 0;
    static {
        try {
            IMAGE = ImageIO.read(Objects.requireNonNull(KeyPosComponent.class.getClassLoader().getResourceAsStream("resources/ComponentBackgroundIcon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String string = "";


    @Override
    public void paint(Graphics2D graphics) {

        graphics.setColor(new Color(200,200,200,150));
        graphics.drawImage(IMAGE, getDrawX(), getDrawY(), WIDTH, HEIGHT, null);
        graphics.setFont(MainFrame.font);
        graphics.setColor(new Color(52, 158, 232,255));
        shiningTime++;
        if (shiningTime>shiningTimer){
            shining = !shining;
            shiningTime = 0;
        }
        graphics.drawString(string.substring(0,cursor)+(isSelected()&&shining?"|":"")+string.substring(cursor),getStringRenderX(graphics,string.substring(0,cursor)+(isSelected()&&shining?"|":"")+string.substring(cursor)),getStringRenderY(graphics,string));
    }

    @Override
    public void onKeyTyped(int vk_code, char keyChar) {
        System.out.println(vk_code);
        if (vk_code== KeyEvent.VK_BACK_SPACE){
            if (string.length()>0&&cursor>0)
            string = string.substring(0, cursor-1)+string.substring(cursor);
            if (cursor>0)
            cursor--;
        }else if (vk_code==KeyEvent.VK_LEFT){
            if (cursor>0)cursor--;
        }else if (vk_code==KeyEvent.VK_RIGHT){
            if (cursor<string.length())cursor++;
        }else if(vk_code!=KeyEvent.VK_CAPS_LOCK&&vk_code!=KeyEvent.VK_SHIFT&&vk_code!=KeyEvent.VK_CONTROL&&vk_code!=KeyEvent.VK_ALT){//控制键都给刨了
            string = string.substring(0,cursor) + keyChar+string.substring(cursor);
            cursor++;
        }
    }




    @Override
    public void onClicked(int x, int y, int button) {

    }


}
