package demo;

import demo.xdServer.XdRequest;
import demo.xdServer.XdResponse;
import demo.xdSpring.Application;
import demo.xdSpringMVC.Dispatcher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


@WebServlet("/*")
public class ServletForTomcat extends HttpServlet {
    private Dispatcher dispatcher;

    @Override
    public void init()  {
        Application.scan(ServletForTomcat.class);
        this.dispatcher = new Dispatcher();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {

        XdRequest xdRequest = new XdRequest();
        xdRequest.path = request.getRequestURI();

        XdResponse xdResponse = new XdResponse();
        dispatcher.doDispatch(xdRequest, xdResponse);

        for (String key:xdResponse.headers.keySet()) {
            String value = xdResponse.headers.get(key);
            response.setHeader(key, value);
        }

        //设置逻辑实现
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(xdResponse.body);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}