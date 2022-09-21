package br.com.newtonpaiva.fastpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class FastPassApplication extends WebMvcAutoConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(FastPassApplication.class, args);
    }

}
