package br.com.newtonpaiva.fastpass.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserRequestDTO {
    private Integer id;
    private String fullName;
    private String email;
    private String password;
    private MultipartFile userImage;
}
