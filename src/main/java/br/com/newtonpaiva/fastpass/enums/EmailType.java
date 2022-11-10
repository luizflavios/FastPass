package br.com.newtonpaiva.fastpass.enums;

import lombok.Getter;

@Getter
public enum EmailType {

    REGISTER("Activate our Account - Fast Pass"),
    WELCOME("Welcome - Fast Pass"),
    FORGOT_PASSWORD("Password Recover - Fast Pass");

    private final String subject;

    EmailType(String subject) {
        this.subject = subject;
    }
}
