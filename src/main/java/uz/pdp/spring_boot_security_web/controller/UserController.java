package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.model.dto.receive.UserRegisterDTO;
import uz.pdp.spring_boot_security_web.service.EmailService;
import uz.pdp.spring_boot_security_web.service.ImageService;
import uz.pdp.spring_boot_security_web.service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final EmailService emailService;
    private final UserService userService;
    private final ImageService imageService;


    @PostMapping("/add")
    public String addUser(
            @ModelAttribute UserRegisterDTO userRegisterDTO
    ) throws IOException {
        boolean isSuccess;

//        MultipartFile image = userRegisterDTO.getImage();
//           imageService.uploadImage(userRegisterDTO.getImage(),userRegisterDTO.getUsername());
            isSuccess = userService.addUser(userRegisterDTO);

        if(isSuccess) {
            return "redirect:/";
        } else {
            return "redirect:/register";
        }

    }
//    @GetMapping("/verifyEmail/{email}/{emailCode}")
//    public String verifyEmail(@PathVariable String email,@PathVariable String emailCode){
//        boolean isSuccess = userService.verifyEmail(email, emailCode);
//        if (isSuccess){
//            return "";
//        }
//        return "";
//    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email) {
        String message = "<a href='http://localhost:8080/api/user/password/reset/" + email + "'>hello</a>";
        emailService.sendMessage(email,message);
        return "redirect:/";
    }
    @GetMapping("/password/reset/{email}")
    public ModelAndView passwordReset(@PathVariable String email, ModelAndView modelAndView) {
        UserEntity byEmail = userService.findByEmail(email);
        modelAndView.addObject("currentUser",byEmail );
        modelAndView.setViewName("updateUser2");
        return modelAndView;
    }

//    @GetMapping("/password/reset/{email}")
//    public String passwordReset(@PathVariable String email, Model modelAndView) {
//        modelAndView.addAttribute("currentUser", userService.findByEmail(email));
//        return "updateUser2";
//    }

    @GetMapping("/update")
    public String updateUser(Model model) {
//        UserEntity currentUser = userService.getCurrentUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = userService.getAuthenticationUser(authentication);
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
        }
        return "updateUser2";
    }

    @PostMapping("/update/{username}")
    public String updateUser(@PathVariable("username") String username,
                             @ModelAttribute UserRegisterDTO userRegisterDTO,
                             Model model) {
        UserEntity userEntity = userService.updateUser(username, userRegisterDTO);
//        UserEntity currentUser = userService.getCurrentUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = userService.getAuthenticationUser(authentication);
        if (currentUser != null) {
            userService.updateUserContextHolder(userEntity);
            UserEntity serviceCurrentUser = userService.getCurrentUser();
            if (serviceCurrentUser != null) {
                model.addAttribute("currentUser", serviceCurrentUser);
            }
            return "redirect:/";
        } else {
            return "register:/update/" + username;
        }
    }


}
