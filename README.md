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

<a href="http://www.ntu.edu.sg/home/ehchua/programming/java/JavaServlets.html">
Servlet Tutorial</a>

## Spring code
```
public class User {
    private String email;
    private String pwd;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPwd() { return pwd; }
    public void setPwd(String pwd) { this.pwd = pwd; }
}

@RequestMapping(value = "/register", method = POST)
public String register(
    @ReuqestBody User user,
    ModelMap modelMap,
    HttpSession session
){
    ......
    return "registerForm";
}
```
## Specification
```
@RequestMapping(String value, MethodEnum method)
context: java.lang.reflect.Method (marked as "this")
postcondition: 
  1. this.url = value 
  2. and this.requestType = method
  3. and this.returnValue = viewSolver(modelMap, this.returnValue@pre)
  4. and this.params->forAll(param|param.isNotAnnotated implies createIfNotExists(param))

@RequestBody() <T>:
context: HttpServletRequest (marked as "request")
body: 
  5. T.getFields()->forAll(field
      |field.equals(field.class.newInstance(request.getParameter(field.getName())))
```
## Servlet code
```
  1. <web-app>
        <servlet>
           <servlet-name>MyServlet</servlet-name>
           <servlet-class>mypkg.MyServlet</servlet-class>
        </servlet>
        <servlet-mapping>
           <servlet-name>MyServlet</servlet-name>
           <url-pattern>/register</url-pattern>
        </servlet-mapping>
     </web-app>
  2. @Override
     public void doPost(HttpServletRequest request, HttpServletResponse response){}
  3. HttpServletRequest model = createHttpRequestFromModelMap(modelMap);
     RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
     dispatcher.forward(model, response);
  4. HttpSession session = request.getSession();
  5. User user = new User();
     for(Method m : user.getClass().getMethods()){
         if(m.getName().startsWith("set")){
             String param = m.getName().substring("set".length());
             m.invoke(user,request.getParamater(param));
         }
     }
```