package demo.xdBatis;



import demo.Common.Utility;
import demo.xdSpring.Bean;
import demo.xdSpring.Beans;
import demo.xdSpring.Configuration;

import java.util.ArrayList;
import java.util.HashMap;

@Configuration
public class XdSpringBootAutoConfiguration {

    private SQLSession session;

    @Bean
    public SQLSession getSQLSession(ArrayList<Class<?>> classes) {
        session = new SQLSession();
        return session;
    }

    @Beans
    public HashMap<Class<?>, Object> getMappers(ArrayList<Class<?>> classes) {
        HashMap<Class<?>, Object> beanDefinition = new HashMap<>();
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Mapper.class)) {
                Object mapper = session.getMapper(clazz);
                Utility.log("getMappers %s %s", mapper, clazz);
                beanDefinition.put(clazz, mapper);
            }
        }
        return beanDefinition;
    }
}
