package br.com.newtonpaiva.fastpass.config;

import br.com.newtonpaiva.fastpass.model.User;
import br.com.newtonpaiva.fastpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DevConfiguration {

    @Autowired
    public UserRepository userRepository;

    @PostConstruct
    public void initDevConfiguration() {
        insertUsers();
    }

    public void insertUsers() {
        User user = User.builder()
                .fullName("Luiz Flavio de Souza Sales Filho")
                .email("ngpbrasil@gmail.com")
                .password("admin")
                .enabled(Boolean.TRUE)
                .build();
        userRepository.saveAndFlush(user);
    }
}
