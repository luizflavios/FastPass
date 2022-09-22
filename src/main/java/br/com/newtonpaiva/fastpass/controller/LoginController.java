package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.dto.UserRequestDTO;
import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import br.com.newtonpaiva.fastpass.generic.GenericMapper;
import br.com.newtonpaiva.fastpass.model.User;
import br.com.newtonpaiva.fastpass.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

@Controller
public class LoginController {

    public static final String ERROR_MSG = "errorMsg";
    public static final String LOGIN = "login";
    private final User user;

    @Autowired
    private GenericMapper genericMapper;
    @Autowired
    private LoginService loginService;

    public LoginController() {
        this.user = new User();
    }

    @GetMapping
    public ModelAndView loadLoginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(LOGIN);
        modelAndView.addObject(ERROR_MSG, "disabled");
        return new ModelAndView(LOGIN);
    }

    @PostMapping("/login")
    public ModelAndView login(UserRequestDTO userRequestDTO) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            UserResponseDTO userResponseDTO = loginService.login((User) genericMapper.toEntity(userRequestDTO, user.getClass()));
            modelAndView.setViewName("index");
            modelAndView.addObject("userResponseDTO", userResponseDTO);
        } catch (EntityNotFoundException e) {
            modelAndView.setViewName(LOGIN);
            modelAndView.addObject(ERROR_MSG, "enabled");
            e.printStackTrace();
        }
        return modelAndView;
    }

    @GetMapping("/sign-up")
    public ModelAndView loadSignUpPage() {
        return new ModelAndView("sign-up");
    }

    @PostMapping("/sign-up")
    public ModelAndView register(UserRequestDTO userRequestDTO) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            UserResponseDTO userResponseDTO = loginService.register((User) genericMapper.toEntity(userRequestDTO, user.getClass()));
            modelAndView.setViewName(LOGIN);
            modelAndView.addObject("userResponseDTO", userResponseDTO);
        } catch (EntityNotFoundException e) {
            modelAndView.setViewName("sign-up");
            modelAndView.addObject(ERROR_MSG, e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
