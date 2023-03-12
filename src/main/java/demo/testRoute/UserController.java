package demo.testRoute;


import demo.Common.Utility;
import demo.Model.Session;
import demo.xdServer.XdRequest;
import demo.xdServer.XdResponse;
import demo.xdSpring.Controller;
import demo.xdSpringMVC.GET;
import demo.testService.UserService;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        Utility.log("class %s, 依赖注入 class %s", this.getClass(), userService.getClass());
        this.userService = userService;
    }


    @GET("/")
    public void index(XdRequest xdRequest, XdResponse xdResponse){
        xdResponse.setStatus(200);
        xdResponse.setHeaders("Content-Type", "text/html");
        xdResponse.setBody("<h1>index</h1>");
    }

    @GET("/tomcat")
    public void tomcat(XdRequest xdRequest, XdResponse xdResponse){
        xdResponse.setStatus(200);
        xdResponse.setHeaders("Content-Type", "text/html");
        xdResponse.setBody("<h1>tomcat</h1>");
    }

    @GET("/login")
    public void login(XdRequest xdRequest, XdResponse xdResponse) {
        xdResponse.setStatus(200);
        xdResponse.setHeaders("Content-Type", "text/html");
        xdResponse.setBody("hello zxd");
    }

    @GET("/favicon.ico")
    public void icon(XdRequest xdRequest, XdResponse xdResponse) {
        xdResponse.setStatus(404);
        xdResponse.setHeaders("Content-Type", "text/html");
        xdResponse.setBody("");
    }

    @GET("/session")
    public void session(XdRequest xdRequest, XdResponse xdResponse) {
        Session session = userService.findSession(1);
        String body = session.sessionID;

        xdResponse.setStatus(200);
        xdResponse.setHeaders("Content-Type", "text/html");
        xdResponse.setBody(body);
    }
}
