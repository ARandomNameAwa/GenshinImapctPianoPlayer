package me.ironblock.mousemappingmaker.files;

import me.ironblock.mousemappingmaker.ui.MainFrame;
import me.ironblock.mousemappingmaker.ui.components.KeyPosComponent;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static FileManager instance;

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    //TODO:写完
    public boolean saveCurrentFile(String path) {
        List<KeyPosComponent> list = MainFrame.getInstance().getComponentsList();
        StringBuilder sb = new StringBuilder();
        for (KeyPosComponent keyPosComponent : list) {
            sb.append(keyPosComponent.getString()).append(":").append(keyPosComponent.getX()).append(",").append(keyPosComponent.getY()).append("\n");
        }
        try {
            new File(path).getParentFile().mkdirs();
            OutputStream outputStream = new FileOutputStream(path);
            byte[] b = sb.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(b, 0, b.length);
            outputStream.close();
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
        return true;

    }

    //TODO:写完
    public boolean loadCurrentFile(String path) {
        List<KeyPosComponent> pos = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                String[] keyValue = buffer.split(":");
                String[] pos0 = keyValue[1].split(",");
                KeyPosComponent posComponent = new KeyPosComponent();
                posComponent.setWidth(KeyPosComponent.WIDTH);
                posComponent.setHeight(KeyPosComponent.HEIGHT);
                posComponent.setX(Integer.parseInt(pos0[0]));
                posComponent.setY(Integer.parseInt(pos0[1]));
                posComponent.setContent(keyValue[0]);
                pos.add(posComponent);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        MainFrame.getInstance().setComponentsList(pos);
        return true;
    }

    //TODO:写完
    public void openResourceSelector() {

    }
}
