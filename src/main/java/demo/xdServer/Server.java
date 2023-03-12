package demo.xdServer;

import demo.Common.Utility;
import demo.xdSpringMVC.Dispatcher;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
     Dispatcher servlet;

    public  void main(String[] args) {
        run(9000);
    }

    public Server(Dispatcher servlet) {
        this.servlet = servlet;
    }

    public  void run(int port) {
        // 监听请求
        // 获取请求数据
        // 发送响应数据
        Utility.log("服务器启动, 访问 http://localhost:%s", port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    // 客户端连接上来了
                    Utility.log("client 连接成功");
                    // 读取客户端请求数据
                    String request = SocketOperator.socketReadAll(socket);
                    byte[] response;
                    if (request.length() > 0) {
                        // 输出响应的数据
                        Utility.log("请求:\n%s", request);
                        // 解析 request 得到 path
                        XdRequest r = new XdRequest(request);

                        // 根据 path 来判断要返回什么数据
                        response = responseForPath(r);
                    } else {
                        response = new byte[1];
                        Utility.log("接受到了一个空请求");
                    }
                    SocketOperator.socketSendAll(socket, response);
                }
            }
        } catch (IOException ex) {
            System.out.println("exception: " + ex.getMessage());
        }
    }

    private  byte[] responseForPath(XdRequest xdRequest) {
        XdResponse xdResponse = new XdResponse();
        this.servlet.doDispatch(xdRequest, xdResponse);
        return xdResponse.raw();
    }
}
