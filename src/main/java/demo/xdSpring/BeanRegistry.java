package demo.xdSpring;



import demo.Common.Utility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeanRegistry {
    private static HashMap<Class<?>, Object> BeanDefinition = new HashMap<>();


    public static Object getBean(Class<?> BeanType) {
        return BeanDefinition.get(BeanType);
    }

    public static void register(Class<?> BeanType, Object Bean) {
        BeanDefinition.put(BeanType, Bean);
    }

    public static List<Object> getBeansByAnnotation(Class<? extends Annotation> annotationClass) {
        List<Object> r = new ArrayList<>();
        for(Class<?> clazz: BeanDefinition.keySet()) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                r.add(getBean(clazz));
            }
        }
        return r;
    }


    public static <T extends Annotation> void scanComponent(Class<?> clazz, Class<T> annotationClass) {
        if (clazz.isAnnotationPresent(annotationClass)) {
            Utility.log("class<%s> 有注解 <%s>", clazz, annotationClass);
            // 只支持一个构造器
            Constructor<?> constructor = clazz.getConstructors()[0];

            // 构造器参数不为 0 时, 去 BeanDefinition 里面找实例
            if (constructor.getParameterCount() > 0) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] args = new Object[constructor.getParameterCount()];
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> type = parameterTypes[i];
                    Object arg = getBean(type);
                    args[i] = arg;
                }
                Object instance;
                try {
                    instance = constructor.newInstance(args);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                register(clazz, instance);
            } else {
                Object instance;
                try {
                    instance = constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                Utility.log("instance %s", instance);
                register(clazz, instance);
            }

        }
    }

    public static void scanConfiguration(List<Class<?>> classes) {
        for (Class<?> clazz : classes)
            if (clazz.isAnnotationPresent(Configuration.class)) {
                Utility.log("scan Configuration %s", clazz);
                Constructor<?> constructor = clazz.getConstructors()[0];
                Object configurationInstance;
                try {
                    configurationInstance = constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                Method[] methods = clazz.getMethods();

                for (Method method : methods) {
                    if (method.isAnnotationPresent(Bean.class)){
                        Utility.log("scan Bean %s", method);
                        Object bean;
                        try {
                            bean = method.invoke(configurationInstance, classes);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        Class<?> beanType = method.getReturnType();
                        register(beanType, bean);
                    }
                }

                for (Method method : methods) {
                    if (method.isAnnotationPresent(Beans.class)){
                        Utility.log("scan Beans %s", method);
                        Object returnValue;
                        try {
                            returnValue =  method.invoke(configurationInstance, classes);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        @SuppressWarnings("unchecked")
                        HashMap<Class<?>, Object> beans = (HashMap<Class<?>, Object>) returnValue;
                        BeanDefinition.putAll(beans);
                    }
                }
            }
    }

    static void scanBean(List<Class<?>> classes) {
        scanConfiguration(classes);


        for (Class<?> clazz : classes) {
            scanComponent(clazz, Service.class);
        }

        for (Class<?> clazz : classes) {
            scanComponent(clazz, Controller.class);
        }

        Utility.log("beans: %s", BeanDefinition);
    }
}
