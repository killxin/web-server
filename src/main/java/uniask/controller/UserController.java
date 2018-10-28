package uniask.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import uniask.data.api.UserRepository;
import uniask.model.User;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/register", method = GET)
    @Transactional
    public String register(ModelMap modelMap) {
        modelMap.addAttribute(new UserForm());
        return "registerForm";
    }

    @RequestMapping(value = "/login", method = GET)
    @Transactional
    public String login(ModelMap modelMap) {
        modelMap.addAttribute(new User());
        return "loginForm";
    }

    // 用户注册
    @RequestMapping(value = "/register", method = POST)
    @Transactional
    public String register(
        @Valid UserForm userForm,
        Errors errors,
        ModelMap modelMap,
        HttpSession session
    ) {
        if (errors.hasErrors()) {
            return "registerForm";
        }
        if (userRepository.findByEmail(userForm.getEmail())!=null) {
            modelMap.addAttribute(userForm);
            modelMap.addAttribute("userExistsError","User already exists!");
            return "registerForm";
        } else {
            User user = userForm.toUser();
            userRepository.save(user);
            session.setAttribute("user", user);
            return "redirect:/";
        }
    }

    // 用户登陆
    @RequestMapping(value = "/login", method = POST)
    @Transactional
    public String login(
        @RequestParam("email") String email,
        @RequestParam("pwd") String pwd,
        ModelMap modelMap,
        HttpSession session
    ) {
        logger.info("login(email,pwd):" + email + "," + pwd);
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if(user.getPwd().equals(pwd)) {
                //登录成功
                logger.info("User Login: " + user.getId());
                session.setAttribute("user", user);
                return "redirect:/";
            } else {
                modelMap.addAttribute("user", new User(email,pwd));
                modelMap.addAttribute("error","Error password!");
                return "loginForm";
            }
        } else {
            modelMap.addAttribute("user", new User(email,pwd));
            modelMap.addAttribute("error","User not found!");
            return "loginForm";
        }
    }

    // 用户注销
    @RequestMapping(value = "/logout", method = GET)
    @Transactional
    public String logout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            logger.info("User Logout:" + user.getId());
            session.removeAttribute("user");
        }
        return "redirect:/";
    }
}
