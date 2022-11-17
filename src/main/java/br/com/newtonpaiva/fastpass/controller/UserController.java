package br.com.newtonpaiva.fastpass.controller;

import br.com.newtonpaiva.fastpass.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private LoginService loginService;

    @GetMapping(value = "/email-verified/{email}")
    public ResponseEntity<String> uniqueEmail(@PathVariable String email) {
        return ResponseEntity.ok(loginService.existsByEmail(email));
    }

}
