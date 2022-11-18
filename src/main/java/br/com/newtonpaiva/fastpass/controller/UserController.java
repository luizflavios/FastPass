package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.dto.UserRequestDTO;
import br.com.newtonpaiva.fastpass.enums.PageConstants;
import br.com.newtonpaiva.fastpass.service.LoginService;
import br.com.newtonpaiva.fastpass.service.UserService;
import br.com.newtonpaiva.fastpass.util.FastPassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    private FastPassUtil fastPassUtil;

    @GetMapping
    public ModelAndView loadUserPage() {
        ModelAndView modelAndView = new ModelAndView();
        setNativeUserPageObjects(modelAndView);
        return modelAndView;
    }

    @GetMapping(value = "/email-verified/{email}")
    public ResponseEntity<String> uniqueEmail(@PathVariable String email) {
        return ResponseEntity.ok(loginService.existsByEmail(email));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModelAndView updateUser(UserRequestDTO userRequestDTO, HttpSession session) {
        session.setAttribute("user", userService.update(userRequestDTO));
        return loadUserPage();
    }

    private void setNativeUserPageObjects(ModelAndView modelAndView) {
        modelAndView.setViewName(PageConstants.USER_FILE);
        modelAndView.addObject("userResponseDTO", fastPassUtil.getLoggedUser());
        modelAndView.addObject(PageConstants.NAME_PAGE, PageConstants.USER_NAME);
        modelAndView.addObject(PageConstants.REASON_PAGE, PageConstants.USER_REASON);
    }

}
