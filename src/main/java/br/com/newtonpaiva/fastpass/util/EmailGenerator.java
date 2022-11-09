package br.com.newtonpaiva.fastpass.util;

import br.com.newtonpaiva.fastpass.enums.EmailType;
import br.com.newtonpaiva.fastpass.model.Email;
import br.com.newtonpaiva.fastpass.model.User;
import org.springframework.stereotype.Component;

@Component
public class EmailGenerator {

    public static final String WELCOME_TO_FAST_PASS = "Welcome to Fast-Pass %s,\n\nClick in link to activate our account\n\n%s/%s!!";
    private static final String SYSTEM_URL = "http://localhost:8080/rest/login/active";

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
        return Email.builder().build();
    }

    private Email buildRegisterEmail(User subject) {
        return Email.builder()
                .to(subject)
                .type(EmailType.REGISTER)
                .text(String.format(WELCOME_TO_FAST_PASS, subject.getFullName(), SYSTEM_URL, subject.getId().toString()))
                .subject(EmailType.REGISTER.getSubject())
                .build();
    }

}
