package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.service.LanguageService;
import uz.pdp.spring_boot_security_web.service.TopicService;
import uz.pdp.spring_boot_security_web.service.UserService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping()
public class HomeController {

    private final UserService userService;
    private final LanguageService languageService;
    private final TopicService topicService;

    @GetMapping("/")
    public ModelAndView home(
            ModelAndView modelAndView
    ) {
        modelAndView.addObject("subjectList", languageService.languageEntityList());
        modelAndView.addObject("lang", "Java");
        modelAndView.addObject("topicList", languageService.getLanguage("Java").getTopicEntities());
//        UserEntity currentUser = userService.getCurrentUser();
        //if (currentUser != null) {
//            modelAndView.addObject("currentUser", currentUser);
        //}
        modelAndView.setViewName("index");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userService.getAuthenticationUser(authentication);
        if (userEntity != null) {
            modelAndView.addObject("isUser", "yes");
            modelAndView.addObject("user", userEntity);
            modelAndView.addObject("ADMIN", userService.getAdmin_In_Roles(userEntity));
        } else {
            modelAndView.addObject("isUser", "not");
        }

        return modelAndView;

    }

    @GetMapping("/{language}")
    public ModelAndView getTopicList(
            ModelAndView model,
            @PathVariable String language
    ) {
        model.addObject("subjectList", languageService.languageEntityList());
        model.addObject("lang", language);
        model.addObject("topicList", topicService.getList(language));
//        UserEntity currentUser = userService.getCurrentUser();
//        if (currentUser != null) {
//            model.addObject("currentUser", currentUser);
//        }
        model.setViewName("index");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userService.getAuthenticationUser(authentication);
        if (userEntity != null) {
            model.addObject("isUser", "yes");
            model.addObject("user", userEntity);
            model.addObject("ADMIN", userService.getAdmin_In_Roles(userEntity));
        } else {
            model.addObject("isUser", "not");
        }
        return model;
    }


}
