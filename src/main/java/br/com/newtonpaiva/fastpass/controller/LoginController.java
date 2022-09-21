package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.dto.UserRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @GetMapping
    public ModelAndView loadLoginPage() {
        return new ModelAndView("login");
    }

    @PostMapping("/login")
    public ModelAndView login(UserRequestDTO userRequestDTO) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("name", userRequestDTO.getEmail());
        return modelAndView;
    }

}
