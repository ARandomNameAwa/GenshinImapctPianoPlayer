package me.ironblock.mousemappingmaker.ui.components;

import java.awt.*;

public abstract class UIComponents {
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    protected int x;
    protected int y;
    protected int width;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected int height;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getDrawX(){
        return getX()-getWidth()/2;
    }
    public int getDrawY(){
        return getY() - getHeight() / 2;
    }
    public abstract void paint(Graphics2D graphics);
    public abstract void onKeyTyped(int vk_code, char keyChar);
    public abstract void onClicked(int x, int y, int button);
    protected void drawComponentRect(Graphics graphics){
        graphics.fillRect(getDrawX(),getDrawY(),getWidth(),getHeight());
    }
}
