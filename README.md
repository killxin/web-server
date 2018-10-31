本项目使用Spring框架实现了一个基本的登录注册模块，其基本功能如下：

Home
```
currentUser = session.get("currentUser")
if(currentUser != null) then 
  display("welcome {currentUser}","logout")
else
  display("welcome","login","register")
```
Login
```
user = find(email)
if(user != null) then
  if(user.pwd != pwd) then
    error = "error password"
    gotoPage("login")
  else 
    session.add("currentUser",user)
    gotoPage("home")
else
  error = "user not exists"
  gotoPage("login")
```
Register
```
if(find(email) != null) then
  error = "user already exists"
  gotoPage("register")
else
  addUser(email,pwd)
  session.add("currentUser",user)
  gotoPage("home")
```
Logout
```
session.remove("currentUser")
gotoPage("home")
```

## Spring code
```
@RequestMapping(value = "/login", method = POST)
public String login(
    @RequestParam("email") String email,
    @RequestParam("pwd") String pwd,
    ModelMap modelMap,
    HttpSession session
) {
    ......
    return "login";
}
```

## Servlet code

MyServlet.java
```
@Override
public void doPost(HttpServletRequest request, HttpServletResponse response){
    String email = request.getParameter("email");
    String pwd = request.getParameter("pwd");
    HttpSession session = request.getSession();
    ......
    HttpServletRequest model = createHttpRequestFromModelMap(modelMap);
    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
    dispatcher.forward(model, response);
}
```

web.xml
```
<web-app>
   <servlet>
      <servlet-name>MyServlet</servlet-name>
      <servlet-class>mypkg.MyServlet</servlet-class>
   </servlet>
   <servlet-mapping>
      <servlet-name>MyServlet</servlet-name>
      <url-pattern>/login</url-pattern>
   </servlet-mapping>
</web-app>
```

## Specification

```
@RequestParam(String param) Object:
context: HttpServletRequest request
precondition: request.paramaters.containsKey(param)
body: request.paramaters.get(param)
```

<a href="http://www.ntu.edu.sg/home/ehchua/programming/java/JavaServlets.html">
Servlet Tutorial</a>