package br.com.newtonpaiva.fastpass.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Integer id;
    private String fullName;
    private String email;
}
