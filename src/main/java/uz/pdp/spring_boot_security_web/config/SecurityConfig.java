package uz.pdp.spring_boot_security_web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import uz.pdp.spring_boot_security_web.service.AuthService;

import java.util.Properties;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;
    @Lazy
    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    private static final String[] WHITE_LIST = new String[]{
//            "/login",
//            "/register",
//            "/auth/sign_in",
//            "/api/user/add"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers( "/*").permitAll()
                .requestMatchers( "/component/**").permitAll()
                .requestMatchers( "/view/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/user/add").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/user/update/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/Java/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/Python/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/user/add").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/user/reset-password").permitAll()
                .requestMatchers( "/api/user/password/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .oauth2Login()
                .successHandler((request, response, authentication) -> {
                    System.out.println(request.getContextPath());
                    DefaultOAuth2User principal =
                            (DefaultOAuth2User) authentication.getPrincipal();
                    authService.processOAuthPostLogin(principal);

                    response.sendRedirect("/");
                })
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID");

        return http.build();
    }
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("bekzodsadriddinov92@gmail.com");
        javaMailSender.setPassword("jodjyjphsxfckksi");
        Properties properties=javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.debug","true");
        return javaMailSender;
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(authService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
