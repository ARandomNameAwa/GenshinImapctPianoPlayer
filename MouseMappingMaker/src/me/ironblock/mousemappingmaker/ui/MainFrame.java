package me.ironblock.mousemappingmaker.ui;

import me.ironblock.mousemappingmaker.files.FileManager;
import me.ironblock.mousemappingmaker.ui.components.KeyPosComponent;
import me.ironblock.mousemappingmaker.ui.components.SettingsComponent;
import me.ironblock.mousemappingmaker.ui.components.UIComponents;
import sun.tools.tree.ShiftLeftExpression;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 覆盖全屏的Frame
 */
public class MainFrame extends JFrame {
    private static MainFrame instance;
    {
      instance = this;
    }
    private List<KeyPosComponent> componentsList = new ArrayList<>();

    private int selectedItem = -1;
    public static final Font font = new Font("微软雅黑", Font.PLAIN, 25);
    private final SettingsComponent settingsComponent = new SettingsComponent();

    /**
     * 构造器
     */
    public MainFrame() {
        new Thread(() -> {
            while (true) {
                repaint();
                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FileManager.getInstance().saveCurrentFile(settingsComponent.getString());
                System.exit(0);
            }
        });
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension dimension = kit.getScreenSize();
        KeyPosComponent.IMAGE.getWidth();//碰一下加载它
        this.setAlwaysOnTop(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setBounds(0, 0, dimension.width, dimension.height);
        settingsComponent.setX(dimension.width / 2);
        settingsComponent.setY(SettingsComponent.HEIGHT/2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0, 20));
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                MainFrame.this.keyTyped(e.getKeyCode(),e.getKeyChar());
            }
        });
        this.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainFrame.this.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                MainFrame.this.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                MainFrame.this.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        this.addMouseMotionListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                MainFrame.this.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

                MainFrame.this.mouseMoved(e);
            }
        });

        this.setVisible(true);


    }

    public void keyTyped(int vk_code,char keyChar){
        if (vk_code==KeyEvent.VK_ESCAPE){
            System.exit(0);
        }else if (selectedItem!=-1){
            componentsList.get(selectedItem).onKeyTyped(vk_code, keyChar);
        }else if (settingsComponent.isSelected()){
            settingsComponent.onKeyTyped(vk_code,keyChar );
        }
    }

    public void mouseClicked(MouseEvent e) {

        //如果点到了设置窗口
        if (checkSettingComponentSelected(e.getXOnScreen(), e.getYOnScreen())) {
            settingsComponent.setSelected(true);
            if (selectedItem!=-1){
                componentsList.get(selectedItem).setSelected(false);
                selectedItem = -1;
            }
            settingsComponent.onClicked(e.getXOnScreen() - settingsComponent.getDrawX(), e.getYOnScreen() - settingsComponent.getDrawY(), e.getButton());
            //直接返回
            return;
        }
        //如果点到了其他东西
        int index = checkKeyPosComponentSelected(e.getXOnScreen(), e.getYOnScreen());
        if (index != -1) {
            if (e.getButton()== MouseEvent.BUTTON1){
                if (selectedItem != -1) {
                    componentsList.get(selectedItem).setSelected(false);
                }
                this.selectedItem = index;
                componentsList.get(selectedItem).setSelected(true);

            }else if (e.getButton()== MouseEvent.BUTTON3){
                componentsList.remove(index);
                if (index<selectedItem){//TODO: 写的有问题 之后要改
                    //TODO:测试
                    selectedItem--;
                    System.out.println("deleted selected:"+selectedItem);
                }else if(index==selectedItem){
                    selectedItem = -1;
                }else{
                    System.out.println("not deleted selected:"+selectedItem);

                }


            }
        } else {   //啥也没点到
            KeyPosComponent newComponent = new KeyPosComponent();
            newComponent.setX(e.getXOnScreen());
            newComponent.setY(e.getYOnScreen());
            newComponent.setWidth(KeyPosComponent.WIDTH);
            newComponent.setHeight(KeyPosComponent.HEIGHT);
            componentsList.add(newComponent);
            if (selectedItem!=-1){
                componentsList.get(selectedItem).setSelected(false);
            }
            newComponent.setSelected(true);
            selectedItem = componentsList.size()-1;
        }

    }

    private int lastX, lastY;
    private int offsetX, offsetY;
    private UIComponents draggingIem;

    private void mousePressed(MouseEvent e) {
        if (e.getButton()==MouseEvent.BUTTON1) {
            lastX = e.getXOnScreen();
            lastY = e.getYOnScreen();
            if (selectedItem != -1&&selectedItem<componentsList.size()) {
                componentsList.get(selectedItem).setSelected(false);
            }
            //如果点到了设置窗口
            if (checkSettingComponentSelected(e.getXOnScreen(), e.getYOnScreen())) {
                settingsComponent.setSelected(true);
                draggingIem = settingsComponent;
                //            settingsComponent.setX(e.getXOnScreen()-calcOffsetX(settingsComponent,lastX));
                //            settingsComponent.setY(e.getYOnScreen()-calcOffsetY(settingsComponent,lastY));
            }
            //如果点到了其他东西
            int index = checkKeyPosComponentSelected(e.getXOnScreen(), e.getYOnScreen());
            if (index != -1) {
                this.selectedItem = index;
                draggingIem = componentsList.get(selectedItem);
                componentsList.get(selectedItem).setSelected(true);
                //            componentsList.get(selectedItem).setX(e.getXOnScreen() - calcOffsetX(componentsList.get(selectedItem), lastX));
                //            componentsList.get(selectedItem).setY(e.getYOnScreen() - calcOffsetY(componentsList.get(selectedItem), lastY));
            }
            if (draggingIem != null) {
                offsetX = calcOffsetX(draggingIem, e.getXOnScreen());
                offsetY = calcOffsetY(draggingIem, e.getYOnScreen());
            }
        }
    }

    private void mouseReleased(MouseEvent e) {
        draggingIem = null;
    }

    private void mouseDragged(MouseEvent e) {
        if (e.getButton()==MouseEvent.NOBUTTON) {
            if (draggingIem != null) {
                draggingIem.setX(lastX - offsetX);
                draggingIem.setY(lastY - offsetY);
            }
            lastX = e.getXOnScreen();
            lastY = e.getYOnScreen();
        }

    }

    private void mouseMoved(MouseEvent e) {

    }

    private int calcOffsetX(UIComponents components, int x) {
        return x - (components.getX());
    }

    private int calcOffsetY(UIComponents components, int y) {
        return y - (components.getY());
    }

    /**
     * 判断一个坐标是否在指定的点里
     *
     * @param components 组件
     * @param x          x
     * @param y          y
     * @return 是否包含
     */
    private boolean contains(UIComponents components, int x, int y) {
        return new Rectangle(components.getX() - components.getWidth() / 2, components.getY() - components.getHeight() / 2, components.getWidth(), components.getHeight()).contains(x, y);
    }

    /**
     * 判断一个点上是否有组件
     *
     * @return 有的话返回索引, 没有返回-1
     */
    private int checkKeyPosComponentSelected(int x, int y) {
        for (int i = 0; i < componentsList.size(); i++) {
            if (contains(componentsList.get(i), x, y)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 判断设置窗口是否在这个点上
     *
     * @param x x
     * @param y y
     * @return 是或否
     */
    private boolean checkSettingComponentSelected(int x, int y) {
        return contains(settingsComponent, x, y);
    }









    @Override
    public void paint(Graphics g) {
       Graphics2D tmp = ((Graphics2D) g);
        Graphics2D backup = tmp;

        tmp.setComposite(AlphaComposite.SrcOver.derive(AlphaComposite.CLEAR));
        tmp.setColor(new Color(0,0,0,255));
        tmp.fillRect(0, 0, this.getWidth(), this.getHeight());
        tmp.setComposite(AlphaComposite.SrcOver.derive(AlphaComposite.SRC));
        tmp.setColor(new Color(0,0,0,10));
        tmp.fillRect(0, 0, this.getWidth(), this.getHeight());
        tmp.setComposite(AlphaComposite.SrcOver.derive(AlphaComposite.SRC_OVER));
        settingsComponent.paint(tmp);
        for (KeyPosComponent keyPosComponent : componentsList) {
            tmp = backup;
            keyPosComponent.paint(tmp);
        }
        g.dispose();


    }

    public List<KeyPosComponent> getComponentsList() {
        return componentsList;
    }

    public void setComponentsList(List<KeyPosComponent> list){
        this.componentsList = list;
    }

    public static MainFrame getInstance() {
        return instance;
    }
}
