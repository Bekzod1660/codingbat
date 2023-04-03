package uz.pdp.spring_boot_security_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringBootSecurityWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityWebApplication.class, args);



    }

}
