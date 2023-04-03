package uz.pdp.spring_boot_security_web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;
import uz.pdp.spring_boot_security_web.model.dto.LanguageRequestDTO;
import uz.pdp.spring_boot_security_web.service.LanguageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class LanguageControllerTest extends BaseTest {
    private static final String PATH_ADD = "/admin/lang/add";
    private static final String PATH_DEL = "/admin/lang/delete";

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
    void afterEach() {
        languageRepository.deleteAll();
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void addLanguageISSuccessful() throws Exception {
        addLanguage().andExpectAll(view().name("redirect:/admin/lang"));
    }
    private ResultActions addLanguage() throws Exception {
        final MockHttpServletRequestBuilder request =
                post(PATH_ADD).param("title","Go");
        return mockMvc.perform(request);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteLanguageShouldReturnOKStatus() throws Exception {
        callAdd();
        ResultActions perform = mockMvc.perform(get("/admin/lang/del/{id}", 2));
        perform.andExpect(view().name("redirect:/admin/lang"));
    }
     ResultActions callAdd() throws Exception {
        final MockHttpServletRequestBuilder request =
                post(PATH_ADD)
                        .param("title", "Java");
        return mockMvc.perform(request);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void updateLanguage() throws Exception {
        callAdd();
        ResultActions request = mockMvc.perform(post("/admin/lang/update/{id}", 1)
                .param("title", "Java"));
        request.andExpect(view().name("redirect:/admin/lang"));

    }
}