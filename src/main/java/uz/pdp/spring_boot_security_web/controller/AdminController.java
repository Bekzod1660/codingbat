package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.model.dto.AdminRequestDto;
import uz.pdp.spring_boot_security_web.repository.UserRepository;
import uz.pdp.spring_boot_security_web.service.UserService;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/add")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String addUser(
            @ModelAttribute AdminRequestDto adminRequestDto
    ) {

        userService.addAdmin(adminRequestDto);
        return "redirect:/api/admin";
    }

    @GetMapping("")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ModelAndView getAdmins(
            ModelAndView modelAndView
    ) {

        modelAndView.setViewName("CrudAdmin");
        modelAndView.addObject("adminList", userService.getAdminList());
        return modelAndView;

    }

    @PostMapping("/update/{username}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String update(@PathVariable String username,
                         @ModelAttribute AdminRequestDto updateAdmin
    ) {
        userService.updateAdmin(username, updateAdmin);
        return "redirect:/api/admin";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String delete(@PathVariable int id) {
        userService.delete(id);
        return "redirect:/api/admin";
    }
}
