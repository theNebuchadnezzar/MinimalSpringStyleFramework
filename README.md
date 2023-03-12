一. String style 框架部分
Server 类，HTTP 服务器，接受请求和响应
xdSpring，扫描载入所有类，生成实例并保存
xdSpringMVC，扫描类的方法，保存 url 和 路由函数的对应关系

二. xdBatis ORM 部分
XMLParser，根据 XML 文件获取 HashMap，key 为 Mapper 的方法，value 为 SQL 语句
Session，和数据库建立链接，发送 SQL 语句和接受结果
MapperInvocationHandler 解析器，把数据库返回结果生成对象对应 Model 对象或者对象的列表

三. 运行方法
1. 初始化数据库
   执行 src\main\resources\schema.sql 脚本

2. 运行 Main 类的 main 函数
   浏览器访问 http://localhost:9000/, 会显示 index
   访问 http://localhost:9000/login, 会显示 hello zxd
   访问 http://localhost:9000/session, 会显示 testSession01