package uz.pdp.spring_boot_security_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.model.dto.LanguageRequestDTO;
import uz.pdp.spring_boot_security_web.service.LanguageService;
import uz.pdp.spring_boot_security_web.service.TaskService;
import uz.pdp.spring_boot_security_web.service.TopicService;

@Controller
@RequestMapping("/admin/lang")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public  ModelAndView langPage(ModelAndView model){
        model.addObject("subjectList", languageService.languageEntityList());
        model.setViewName("language");
        return model;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize(value = "hasRole('ADMIN') and hasAuthority('ADD')")
    public String addLanguage(
            @ModelAttribute LanguageRequestDTO languageRequestDTO
    ) {
        languageService.addLanguage(languageRequestDTO);
        return "redirect:/admin/lang";
    }

    @GetMapping("/del/{id}")
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE')")
    public String deleteLanguage(
            @PathVariable int id
    ) {
        languageService.delete(id);
        return "redirect:/admin/lang";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasRole('ADMIN') and hasAuthority('UPDATE')")
    public String updateLanguage(
            @PathVariable int id,
            @ModelAttribute LanguageRequestDTO title
    ) {
        languageService.update(id, title);
        return "redirect:/admin/lang";
    }
}
