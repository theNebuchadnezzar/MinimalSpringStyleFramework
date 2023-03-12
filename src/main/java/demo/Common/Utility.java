package demo.Common;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Utility {
    static public void log(String format, Object...args) {
        System.out.println(String.format(format, args));
    }

        public static InputStream fileStream(String path) {
        String resource = String.format("%s.class", Utility.class.getSimpleName());
        Utility.log("resource %s", resource);
        Utility.log("resource path %s", Utility.class.getResource("/"));
        var res = Utility.class.getResource(resource);
        if (res != null && res.toString().contains("WEB-INF")) {
            path = String.format("/%s", path);
            InputStream is = Utility.class.getResourceAsStream(path);
            if (is == null) {
                throw new RuntimeException(String.format("在 jar 里面找不到 %s", path));
            } else {
                return is;
            }
        } else {
            path = String.format("src/main/resources/%s", path);
            try {
                return new FileInputStream(path);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(String.format("在项目里里面找不到 %s", path));
            }
        }
    }
}
