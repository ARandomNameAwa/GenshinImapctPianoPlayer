package me.ironblock.mousemappingmaker.ui.components;

import me.ironblock.mousemappingmaker.ui.MainFrame;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public abstract class UIComponents {
    private boolean isSelected;
    protected List<UIComponents> componentsList = new ArrayList<>();

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    protected void renderComponents(Graphics2D g){
        componentsList.forEach(c->c.paint(g));
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
    protected int getStringRenderX(Graphics2D g,String stringToRender){
        Rectangle2D r2d = g.getFontMetrics(MainFrame.font).getStringBounds(stringToRender, g);
        return getX()-(int) r2d.getWidth()/2;
    }
    protected int getStringRenderY(Graphics2D g,String string){
        Rectangle2D r2d = g.getFontMetrics(MainFrame.font).getStringBounds(string, g);
        return getY()+(int) r2d.getHeight()/4;
    }
    protected int getStringRenderX(Graphics2D g,String stringToRender,int xYouWantToRender){
        Rectangle2D r2d = g.getFontMetrics(MainFrame.font).getStringBounds(stringToRender, g);
        return getX()+xYouWantToRender-(int) r2d.getWidth()/2;
    }
    protected int getStringRenderY(Graphics2D g,String string,int yYouWantToRender){
        Rectangle2D r2d = g.getFontMetrics(MainFrame.font).getStringBounds(string, g);
        return getY()+yYouWantToRender+(int) r2d.getHeight()/4;
    }
}
