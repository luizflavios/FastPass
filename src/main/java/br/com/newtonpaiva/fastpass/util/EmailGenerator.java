package br.com.newtonpaiva.fastpass.util;

import br.com.newtonpaiva.fastpass.enums.EmailType;
import br.com.newtonpaiva.fastpass.model.Email;
import br.com.newtonpaiva.fastpass.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailGenerator {

    public static final String WELCOME_TO_FAST_PASS = "Welcome to Fast-Pass %s,\n\nYour activation code: %s\n\n%s!!";
    private static final String ACTIVATE_URI = "/active-account";
    private static final String FORGOT_PASSWORD = "Hello, %s!\nThis is a security email, sent by Fast-Pass!\nYour new temporary password is: %s.";

    @Value("${api.base.url}")
    private String API_BASE_URL;

    public Email generate(User subject, EmailType type) {

        return switch (type) {
            case REGISTER -> buildRegisterEmail(subject);
            case FORGOT_PASSWORD -> buildForgotPasswordEmail(subject);
            case WELCOME -> buildWelcomeEmail(subject);
        };

    }

    private Email buildWelcomeEmail(User subject) {
        return Email.builder().build();
    }

    private Email buildForgotPasswordEmail(User subject) {
        return Email.builder()
                .to(subject)
                .type(EmailType.FORGOT_PASSWORD)
                .text(String.format(FORGOT_PASSWORD, subject.getFullName(), subject.getPassword()))
                .subject(EmailType.FORGOT_PASSWORD.getSubject())
                .build();
    }

    private Email buildRegisterEmail(User subject) {
        return Email.builder()
                .to(subject)
                .type(EmailType.REGISTER)
                .text(String.format(WELCOME_TO_FAST_PASS, subject.getFullName(), subject.getCode(), API_BASE_URL + ACTIVATE_URI))
                .subject(EmailType.REGISTER.getSubject())
                .build();
    }

}
