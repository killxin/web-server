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
