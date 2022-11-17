package br.com.newtonpaiva.fastpass.config;

import br.com.newtonpaiva.fastpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DevConfiguration {

    @Autowired
    public UserRepository userRepository;

    @PostConstruct
    private void initDevConfiguration() {
        insertUsers();
    }

    private void insertUsers() {
        //Build new user - dev configuration
    }

}
