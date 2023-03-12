package demo.xdSpringMVC;

import demo.Common.Utility;
import demo.xdServer.XdRequest;
import demo.xdServer.XdResponse;
import demo.xdSpring.BeanRegistry;
import demo.xdSpring.Controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class Dispatcher {
    HashMap<String, Method> urlMethod;
    HashMap<String, Object> urlBean;


    public  Dispatcher() {
        this.urlMethod = new HashMap<>();
        this.urlBean = new HashMap<>();

        List<Object> controllers = BeanRegistry.getBeansByAnnotation(Controller.class);
        for (Object controller:controllers) {
            Method[] methods = controller.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(GET.class)){
                    GET annotation = method.getAnnotation(GET.class);
                    String url = annotation.value();
                    Utility.log("scan url %s %s", url, method);
                    urlMethod.put(url, method);
                    urlBean.put(url, controller);
                }
            }
        }
    }


    public void doDispatch(XdRequest xdRequest, XdResponse xdResponse) {
        Method method = urlMethod.get(xdRequest.path);
        Object object = urlBean.get(xdRequest.path);

        try {
            method.invoke(object, xdRequest, xdResponse);
            Utility.log("response: <%s>", xdResponse);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
