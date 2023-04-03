package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(username);
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        return optionalUserEntity.orElseThrow(() -> new UsernameNotFoundException(String.format("username %s not found", username)));
    }

    public void processOAuthPostLogin(DefaultOAuth2User customOAuth2User) {
        String name = customOAuth2User.getAttribute("name");
        String email = customOAuth2User.getAttribute("email");
        String logoUrl = customOAuth2User.getAttribute("picture");
        System.out.println(logoUrl);

        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if (optionalUserEntity.isEmpty()) {
            UserEntity userEntity = UserEntity.from(name, email);
            String userPassword = UUID.randomUUID().toString().substring(0, 8);
            userEntity.setPassword(passwordEncoder.encode(userPassword));
            userEntity.setLogoUrl(logoUrl);
            userEntity.setUsername(email);
            emailService.sendMessage(email, userPassword);
            userRepository.save(userEntity);
            setAuthentication(email);
        }
    }

    private void setAuthentication(String email) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("OIDC_USER"))
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public UserEntity getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            String email = token.getPrincipal().getAttribute("email");
            String name = token.getPrincipal().getAttribute("name");
            UserEntity userEntity = UserEntity.from(name, email);
            setAuthentication(email);
            return userRepository.save(userEntity);
        } catch (Exception e) {
            if (!(authentication.getPrincipal() + "").equals("anonymousUser")) {
                return (UserEntity) authentication.getPrincipal();
            }
        }
        return null;
    }
}
