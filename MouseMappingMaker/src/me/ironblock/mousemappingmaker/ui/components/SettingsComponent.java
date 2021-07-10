package me.ironblock.mousemappingmaker.ui.components;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SettingsComponent extends UIComponents{
    public static final int WIDTH = 500;
    public static final int HEIGHT = 45;
    //label
    private static final int ta_left = -150;
    private static final int ta_up = -15;
    private static final int ta_width = 230;
    private static final int ta_height = 30;
    //ta
    private final int shiningTimer = 50;
    private int shiningTime = 0;
    private boolean shining = false;
    private int cursor = 0;
    private String string = "qwe";

    public SettingsComponent() {
        this.setWidth(WIDTH);
        this.setHeight(HEIGHT);
    }

    @Override
    public void paint(Graphics2D graphics) {
        graphics.setColor(new Color(200,255,255,150));
        drawComponentRect(graphics);
        //draw label
        graphics.setColor(new Color(0,0,0,255));
        graphics.drawString("Path:", getStringRenderX(graphics, "Path:",-200), getStringRenderY(graphics, "Path:",-5));
        //draw ta
        graphics.setColor(new Color(255,255,255,150));
        graphics.fillRect(getX()+ta_left, getY()+ta_up, ta_width, ta_height);
        graphics.setColor(new Color(52, 158, 232,255));
        shiningTime++;
        if (shiningTime>shiningTimer){
            shining = !shining;
            shiningTime = 0;
        }
        graphics.drawString(string.substring(0,cursor)+(isSelected()&&shining?"|":"")+string.substring(cursor),this.getX()+ta_left,getStringRenderY(graphics,string,ta_up+10));
    }

    @Override
    public void onKeyTyped(int vk_code, char keyChar) {
        System.out.println(vk_code);
        if (vk_code== KeyEvent.VK_BACK_SPACE){
            if (string.length()>0&&cursor>0)
                string = string.substring(0, cursor-1)+string.substring(cursor);
            if (cursor>0)
                cursor--;
        }else if (vk_code== KeyEvent.VK_LEFT){
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
