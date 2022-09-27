package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.dto.UserRequestDTO;
import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.enums.PageConstants;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.model.User;
import br.com.newtonpaiva.fastpass.service.IndexService;
import br.com.newtonpaiva.fastpass.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    public static final String ERROR_MSG = "errorMsg";

    private final User user;

    @Autowired
    private GenericMapper genericMapper;
    @Autowired
    private LoginService loginService;
    @Autowired
    private IndexService indexService;
    @Autowired
    private IndexController indexController;

    public LoginController() {
        this.user = new User();
    }

    @GetMapping
    public ModelAndView loadLoginPage(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (session.getAttribute("user") != null) {
            modelAndView = indexController.loadIndexPage();
        } else {
            modelAndView.setViewName(PageConstants.LOGIN_FILE);
            modelAndView.addObject(ERROR_MSG, "disabled");
        }
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(UserRequestDTO userRequestDTO, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            UserResponseDTO userResponseDTO = loginService.login((User) genericMapper.toEntity(userRequestDTO, user.getClass()));
            indexController.setIndexObjects(modelAndView, userResponseDTO);
            session.setAttribute("user", userResponseDTO);
        } catch (EntityNotFoundException e) {
            modelAndView.setViewName(PageConstants.LOGIN_FILE);
            modelAndView.addObject(ERROR_MSG, "enabled");
            e.printStackTrace();
        }
        return modelAndView;
    }

    @GetMapping("/sign-up")
    public ModelAndView loadSignUpPage() {
        return new ModelAndView(PageConstants.SIGN_UP_FILE);
    }

    @PostMapping("/sign-up")
    public ModelAndView register(UserRequestDTO userRequestDTO) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            UserResponseDTO userResponseDTO = loginService.register((User) genericMapper.toEntity(userRequestDTO, user.getClass()));
            modelAndView.setViewName(PageConstants.LOGIN_FILE);
            modelAndView.addObject("signUpMsg", "enabled");
            modelAndView.addObject("userResponseDTO", userResponseDTO);
        } catch (EntityNotFoundException e) {
            modelAndView.setViewName(PageConstants.SIGN_UP_FILE);
            modelAndView.addObject(ERROR_MSG, e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

    @GetMapping("/forgot-password")
    public ModelAndView loadForgotPasswordPage() {
        return new ModelAndView(PageConstants.FORGOT_PASSWORD_FILE);
    }

    @PostMapping("/forgot-password")
    public ModelAndView forgotPassword(String email) {
        return new ModelAndView(PageConstants.LOGIN_FILE);
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.removeAttribute("user");
        return new ModelAndView(PageConstants.LOGIN_FILE);
    }

}
