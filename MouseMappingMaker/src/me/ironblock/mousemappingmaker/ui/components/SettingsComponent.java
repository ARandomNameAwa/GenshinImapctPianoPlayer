package me.ironblock.mousemappingmaker.ui.components;

import java.awt.*;

public class SettingsComponent extends UIComponents{
    public static final int WIDTH = 500;
    public static final int HEIGHT = 45;

    private static final int ta_left = -150;
    private static final int ta_up = -15;
    private static final int ta_width = 230;
    private static final int ta_height = 30;

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

    }

    @Override
    public void onKeyTyped(int vk_code, char keyChar) {

    }

    @Override
    public void onClicked(int x, int y, int button) {

    }
}
