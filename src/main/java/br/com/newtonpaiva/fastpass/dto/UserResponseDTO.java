package br.com.newtonpaiva.fastpass.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
public class UserResponseDTO {
    private Integer id;
    private String fullName;
    private String email;
    private byte[] userImage;
    private String password;
    private String image;

    public String getImage() {
        String encodedString = Base64.getEncoder().encodeToString(getUserImage());
        return "data:@file/jpeg;base64," + encodedString;
    }
}
