package br.com.newtonpaiva.fastpass.config;

import br.com.newtonpaiva.fastpass.model.User;
import br.com.newtonpaiva.fastpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Configuration
public class DevConfiguration {

    @Autowired
    public UserRepository userRepository;

    @PostConstruct
    private void initDevConfiguration() {
    }

    private void insertUsers() {
        User user = User.builder()
                .id(1)
                .fullName("Luiz Flavio de Souza Sales Filho")
                .email("ngpbrasil@gmail.com")
                .password("admin")
                .enabled(Boolean.TRUE)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.saveAndFlush(user);
    }

}
