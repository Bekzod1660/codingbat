package uz.pdp.spring_boot_security_web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AuthControllerTest extends BaseTest {
    private final static String PATH_ADD = "/api/user/add";

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    public void registerPageIsExist() throws Exception {
        final MockHttpServletRequestBuilder request = get("/register");
        mockMvc.perform(request).andExpect(view().name("createAccount"));
    }

    @Test
    public void addUserShouldThrowUserExist() throws Exception {
        final MockHttpServletRequestBuilder request = get("/forget-password");
        mockMvc.perform(request).andExpect(view().name("forgetPassword"));
    }
}