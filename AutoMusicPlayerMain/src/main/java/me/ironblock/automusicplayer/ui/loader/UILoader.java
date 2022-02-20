package me.ironblock.automusicplayer.ui.loader;

import me.ironblock.automusicplayer.ui.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author :Iron__Block
 * @Date :2022/2/17 2:23
 */
public class UILoader {
    public static final Logger LOGGER = LogManager.getLogger(UILoader.class);
    public static UIContext UI;

    public static void loadUIFromPackage(String packageName) throws Exception {
        UIContext uiContext = new UIContext();
        Reflections reflections = new Reflections(new ConfigurationBuilder().
                forPackages(packageName)
                .addScanners(Scanners.TypesAnnotated, Scanners.MethodsAnnotated, Scanners.FieldsAnnotated));

        //Initializers
        Map<String, Method> initializerMap = new HashMap<>();
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(Initializer.class);
        Set<Method> postProcessors = reflections.getMethodsAnnotatedWith(PostProcessor.class);

        for (Method method : methodsAnnotatedWith) {
            Initializer initializerAnnotation = method.getAnnotation(Initializer.class);
            initializerMap.put(initializerAnnotation.name(), method);
        }

        //Listeners
        Map<String, Object> listenersMap = new HashMap<>();
        Map<String, Class<?>> listenersTypeMap = new HashMap<>();
        Set<Class<?>> listeners = reflections.getTypesAnnotatedWith(Listener.class);
        for (Class<?> listener : listeners) {
            Listener listenerAnnotation = listener.getAnnotation(Listener.class);
            listenersMap.put(listenerAnnotation.name(), listener.newInstance());
            listenersTypeMap.put(listenerAnnotation.name(), listenerAnnotation.parent());
        }

        //Window frames
        Set<Class<?>> windowFrames = reflections.getTypesAnnotatedWith(WindowFrame.class);
        for (Class<?> windowFrame : windowFrames) {
            //Load Frames
            WindowFrame windowFrameAnnotation = windowFrame.getAnnotation(WindowFrame.class);
            Frame frame = ((Frame) windowFrame.newInstance());

            if (!windowFrameAnnotation.background().isEmpty()) {
                trySetIcon(frame, windowFrameAnnotation.background());
            }

            frame.setTitle(windowFrameAnnotation.title());
            if (windowFrameAnnotation.autoCenter()) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                double width = screenSize.getWidth();
                double height = screenSize.getHeight();
                double dpi = Toolkit.getDefaultToolkit().getScreenResolution();
                frame.setBounds(((int) (width / 2 - windowFrameAnnotation.width() / 2)), ((int) (height / 2 - windowFrameAnnotation.height() / 2)), windowFrameAnnotation.width(), windowFrameAnnotation.height());
            } else {
                frame.setBounds(windowFrameAnnotation.x(), windowFrameAnnotation.y(), windowFrameAnnotation.width(), windowFrameAnnotation.height());
            }
            if (frame instanceof JFrame) {
                ((JFrame) frame).setDefaultCloseOperation(windowFrameAnnotation.defaultCloseOperation());
            } else {
                LOGGER.warn("Window Frame " + windowFrameAnnotation.name() + " is not a JFrame so didn't set default close operation for it.");
            }
            if (!windowFrameAnnotation.initializer().isEmpty()) {
                Method init = initializerMap.get(windowFrameAnnotation.initializer());
                if (init != null) {
                    if (init.getParameterCount() == 1) {
                        init.invoke(null, frame);
                    }
                } else {
                    LOGGER.error("Frame " + windowFrameAnnotation.name() + "'s initializer: " + windowFrameAnnotation.initializer() + " doesn't exists.");
                }
            }

            if (windowFrameAnnotation.listeners().length > 0) {
                for (String listener : windowFrameAnnotation.listeners()) {
                    Object listenerInstance = listenersMap.get(listener);
                    Class<?> type = listenersTypeMap.get(listener);
                    Method addListenerMethod = frame.getClass().getMethod("add" + type.getSimpleName(), type);
                    addListenerMethod.invoke(frame, listenerInstance);
                }
            }
            frame.setLayout(null);
            uiContext.addWindowFrame(windowFrameAnnotation.name(), frame);


            //Load components( setBounds,Listeners)
            for (Field field : windowFrame.getFields()) {
                WindowComponent componentAnnotation = field.getAnnotation(WindowComponent.class);
                if (componentAnnotation != null) {
                    Component componentInstance;
                    Class<?> classType = Class.forName(field.getGenericType().getTypeName());
                    if (componentAnnotation.initPara().isEmpty()) {
                        componentInstance = (Component) classType.newInstance();
                    } else {
                        componentInstance = (Component) classType.getConstructor(String.class).newInstance(componentAnnotation.initPara());
                    }
                    uiContext.addComponents(componentAnnotation.name(), componentInstance);

                    if (!componentAnnotation.background().isEmpty()) {
                        trySetIcon(componentInstance, componentAnnotation.background());
                    }

                    componentInstance.setBounds(componentAnnotation.x(), componentAnnotation.y(), componentAnnotation.width(), componentAnnotation.height());
                    if (componentAnnotation.listeners().length > 0) {
                        for (String listener : componentAnnotation.listeners()) {
                            Object listenerInstance = listenersMap.get(listener);
                            Class<?> type = listenersTypeMap.get(listener);
                            Method addListenerMethod = componentInstance.getClass().getMethod("add" + type.getSimpleName(), type);
                            addListenerMethod.invoke(componentInstance, listenerInstance);
                        }
                    }

                    if (!componentAnnotation.initializer().isEmpty()) {
                        Method init = initializerMap.get(componentAnnotation.initializer());
                        if (init != null) {
                            if (init.getParameterCount() == 1) {
                                Object ret = init.invoke(null, componentInstance);
                                if (ret instanceof Boolean){
                                    if (ret != Boolean.TRUE)
                                    continue;
                                }
                            }else if(init.getParameterCount() == 2){
                                Object ret = init.invoke(null, componentInstance,frame);
                                if (ret instanceof Boolean){
                                    if (ret != Boolean.TRUE)
                                    continue;
                                }
                            } else {
                                LOGGER.warn("The initializer " + componentAnnotation.initializer() + "of " + componentAnnotation.name() + " doesn't have one parameter,can't invoke it.");
                            }
                        } else {
                            LOGGER.error("Component " + componentAnnotation.name() + "'s initializer: " + componentAnnotation.initializer() + " doesn't exists.");
                        }
                    }
                    frame.add(componentInstance);


                }
            }


            //Load Components(Add)
            for (Field field : windowFrame.getFields()) {
                WindowComponent windowComponentAnnotation = field.getAnnotation(WindowComponent.class);
                if (windowComponentAnnotation != null) {
                    if (!windowComponentAnnotation.parent().isEmpty()) {
                        Component componentFromName = uiContext.getComponentFromName(windowComponentAnnotation.parent());
                        Component componentFromName1 = uiContext.getComponentFromName(windowComponentAnnotation.name());
                        if (componentFromName instanceof Container) {
                            ((Container) componentFromName).add(componentFromName1);
                        } else {
                            LOGGER.warn("Component" + componentFromName + "is not a container,but " + componentFromName1 + "is its child.");
                        }
                    }
                }

            }
        }
        UI = uiContext;
        for (Method postProcessor : postProcessors) {
            if (postProcessor.getParameterCount() == 0){
                postProcessor.invoke(null);
            }else{
                LOGGER.warn(postProcessor.getName()+" has more than 0 paras.");
            }
        }
    }

    public static void trySetIcon(Component component, String resource) {
        Image image;
        try {
            InputStream resourceAsStream = UILoader.class.getResourceAsStream(resource);
             image = ImageIO.read(resourceAsStream);
             resourceAsStream.close();
        } catch (IOException e) {
            LOGGER.error("Resource "+resource+" doesn't exists.",e);
            return;
        }

        ImageIcon imageIcon = new ImageIcon(image);
        if (component instanceof Frame) {
            ((Frame) component).setIconImage(imageIcon.getImage());
        } else {
            Method method = null;
            try {
                method = component.getClass().getMethod("setIcon", Icon.class);
            } catch (NoSuchMethodException e) {
               LOGGER.warn("Failed to set icon for "+component.getName(),e);
            }
            if (method!=null){
                try {
                    method.invoke(component,imageIcon);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOGGER.error("Failed to invoke method "+method.getName(),e);
                }
            }


        }
    }
}
//+