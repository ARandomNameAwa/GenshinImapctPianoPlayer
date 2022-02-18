package me.ironblock.automusicplayer.ui.loader;

import me.ironblock.automusicplayer.ui.annotations.Initializer;
import me.ironblock.automusicplayer.ui.annotations.Listener;
import me.ironblock.automusicplayer.ui.annotations.WindowComponent;
import me.ironblock.automusicplayer.ui.annotations.WindowFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
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

    public static void loadUIFromPackage(String packageName) throws Exception{
        UIContext uiContext = new UIContext();
        Reflections reflections = new Reflections(new ConfigurationBuilder().
                forPackages(packageName)
                .addScanners(Scanners.TypesAnnotated,Scanners.MethodsAnnotated,Scanners.FieldsAnnotated));

        //Initializers
        Map<String, Method> initializerMap = new HashMap<>();
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(Initializer.class);
        for (Method method : methodsAnnotatedWith) {
            Initializer initializerAnnotation = method.getAnnotation(Initializer.class);
            initializerMap.put(initializerAnnotation.name(),method);
        }

        //Listeners
        Map<String, Object> listenersMap = new HashMap<>();
        Map<String, Class<?>> listenersTypeMap = new HashMap<>();
        Set<Class<?>> listeners = reflections.getTypesAnnotatedWith(Listener.class);
        for (Class<?> listener : listeners) {
            Listener listenerAnnotation = listener.getAnnotation(Listener.class);
            listenersMap.put(listenerAnnotation.name(),listener.newInstance());
            listenersTypeMap.put(listenerAnnotation.name(),listenerAnnotation.parent());
        }

        //Window frames
        Set<Class<?>> windowFrames = reflections.getTypesAnnotatedWith(WindowFrame.class);
        for (Class<?> windowFrame : windowFrames) {
            //Load Frames
            WindowFrame windowFrameAnnotation =  windowFrame.getAnnotation(WindowFrame.class);
            Frame frame = ((Frame) windowFrame.newInstance());
            frame.setTitle(windowFrameAnnotation.title());
            if (windowFrameAnnotation.autoCenter()){
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                double width = screenSize.getWidth();
                double height = screenSize.getHeight();
                double dpi = Toolkit.getDefaultToolkit().getScreenResolution();
                double w = width / dpi;
                double h = height / dpi;
                frame.setBounds(((int) (windowFrameAnnotation.width() / 2 - w / 2)), ((int) (windowFrameAnnotation.height() / 2 - h / 2)), windowFrameAnnotation.width(), windowFrameAnnotation.height());
            }else{
                frame.setBounds(windowFrameAnnotation.x(),windowFrameAnnotation.y(),windowFrameAnnotation.width(),windowFrameAnnotation.height());
            }
            if (frame instanceof JFrame){
                ((JFrame) frame).setDefaultCloseOperation(windowFrameAnnotation.defaultCloseOperation());
            }else{
                LOGGER.warn("Window Frame "+ windowFrameAnnotation.name()+" is not a JFrame so didn't set default close operation for it.");
            }
            if (!windowFrameAnnotation.initializer().isEmpty()) {
                Method init = initializerMap.get(windowFrameAnnotation.initializer());
                if (init!=null){
                    if (init.getParameterCount() == 1){
                        init.invoke(null,frame);
                    }
                }else{
                    LOGGER.error("Frame "+windowFrameAnnotation.name()+"'s initializer: "+windowFrameAnnotation.initializer()+" doesn't exists.");
                }
            }

            if (windowFrameAnnotation.listeners().length>0){
                for (String listener : windowFrameAnnotation.listeners()) {
                    Object listenerInstance = listenersMap.get(listener);
                    Class<?> type = listenersTypeMap.get(listener);
                    Method addListenerMethod = frame.getClass().getMethod("add"+type.getSimpleName(),type);
                    addListenerMethod.invoke(frame,listenerInstance);
                }
            }
            uiContext.addWindowFrame(windowFrameAnnotation.name(),frame);



            //Load components( setBounds,Listeners)
            for (Field field : windowFrame.getFields()) {
                WindowComponent windowComponentAnnotation = field.getAnnotation(WindowComponent.class);
                if (windowComponentAnnotation != null) {
                    Component componentInstance;
                    Class<?> classType = Class.forName(field.getGenericType().getTypeName());
                    if (windowComponentAnnotation.initPara().isEmpty()){
                        componentInstance = (Component)classType.newInstance();
                    }else{
                        componentInstance = (Component) classType.getConstructor(String.class).newInstance(windowComponentAnnotation.initPara());
                    }
                    uiContext.addComponents(windowComponentAnnotation.name(),componentInstance);
                    componentInstance.setBounds(windowComponentAnnotation.x(),windowFrameAnnotation.y(),windowComponentAnnotation.width(),windowComponentAnnotation.height());
                    if (!windowComponentAnnotation.initializer().isEmpty()) {
                        Method init = initializerMap.get(windowComponentAnnotation.initializer());
                        if (init!=null){
                            if (init.getParameterCount() == 1){
                                init.invoke(null,componentInstance);
                            }else{
                                LOGGER.warn("The initializer " +windowComponentAnnotation.initializer()+"of "+windowComponentAnnotation.name()+" doesn't have one parameter,can't invoke it.");
                            }
                        }else{
                            LOGGER.error("Component "+windowComponentAnnotation.name()+"'s initializer: "+windowComponentAnnotation.initializer()+" doesn't exists.");
                        }
                    }
                    if (windowComponentAnnotation.listeners().length>0){
                        for (String listener : windowComponentAnnotation.listeners()) {
                            Object listenerInstance = listenersMap.get(listener);
                            Class<?> type = listenersTypeMap.get(listener);
                            Method addListenerMethod = componentInstance.getClass().getMethod("add"+type.getSimpleName(),type);
                            addListenerMethod.invoke(componentInstance,listenerInstance);
                        }
                    }
                    frame.add(componentInstance);
                }
            }




            //Load Components(Add)
            for (Field field : windowFrame.getFields()) {
                WindowComponent windowComponentAnnotation = field.getAnnotation(WindowComponent.class);
                if (windowComponentAnnotation!=null) {
                    if (!windowComponentAnnotation.parent().isEmpty()){
                        Component componentFromName = uiContext.getComponentFromName(windowComponentAnnotation.parent());
                        Component componentFromName1 = uiContext.getComponentFromName(windowComponentAnnotation.name());
                        if (componentFromName instanceof Container){
                            ((Container) componentFromName).add(componentFromName1);
                        }else{
                            LOGGER.warn("Component" +componentFromName+"is not a container,but "+componentFromName1+"is its child.");
                        }
                    }
                }
            }

        }
        UI = uiContext;
    }
}
