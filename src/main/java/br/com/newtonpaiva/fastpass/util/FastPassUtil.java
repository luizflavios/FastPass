package br.com.newtonpaiva.fastpass.util;

import br.com.newtonpaiva.fastpass.dto.UserResponseDTO;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class FastPassUtil {

    private final HttpSession session;

    public FastPassUtil(HttpSession session) {
        this.session = session;
    }

    public UserResponseDTO getLoggedUser() {
        return (UserResponseDTO) session.getAttribute("user");
    }


}
