package br.com.newtonpaiva.fastpass.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    private String fullName;
    private String email;
    private String password;
}
