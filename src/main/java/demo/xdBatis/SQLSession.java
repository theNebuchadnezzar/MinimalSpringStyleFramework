package demo.xdBatis;

import com.mysql.cj.jdbc.MysqlDataSource;
import demo.Common.Utility;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class SQLSession {
    public static String databaseName = "java";
    private Connection connection;

    public SQLSession() {
        // 取一个数据库连接
        try {
            this.connection = getDataSourceWithUrl().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static MysqlDataSource getDataSourceWithUrl() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(String.format("jdbc:mysql://127.0.0.1:3306/%s?serverTimezone=Asia/Shanghai", databaseName));
        dataSource.setUser("root");
        dataSource.setPassword("12345");
        return  dataSource;
    }

    public <T> T getMapper(Class<T> clazz) {
        String fileName = String.format("%s.xml", clazz.getSimpleName());
        Map<Method, String> SQLMap = XMLParser.getSQLMap(fileName);
        MapperInvocationHandler handler = new MapperInvocationHandler(connection, SQLMap);

        // 库里面就是 unchecked
        @SuppressWarnings("unchecked")
        T mapper = (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[] {clazz},
                handler
        );
        return mapper;
    }



}
