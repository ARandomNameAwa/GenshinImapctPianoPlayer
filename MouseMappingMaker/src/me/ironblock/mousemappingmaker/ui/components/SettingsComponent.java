package me.ironblock.mousemappingmaker.ui.components;

import java.awt.*;

public class SettingsComponent extends UIComponents{
    public static final int WIDTH = 500;
    public static final int HEIGHT = 100;
    public SettingsComponent() {
        this.setWidth(WIDTH);
        this.setHeight(HEIGHT);
    }

    @Override
    public void paint(Graphics2D graphics) {
        graphics.setColor(new Color(255,255,255,150));
        drawComponentRect(graphics);
    }

    @Override
    public void onKeyTyped(int vk_code, char keyChar) {

    }

    @Override
    public void onClicked(int x, int y, int button) {

    }
}
