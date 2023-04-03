package uz.pdp.spring_boot_security_web.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;
import uz.pdp.spring_boot_security_web.model.dto.AdminRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminControllerTest extends BaseTest {
    private final static String PATH_ADD = "/api/admin/add";
    private final static String PATH_list = "/api/admin";
    private final static String PATH_delete = "/api/admin/delete";
    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    public void addUserShouldReturnOKStatus() throws Exception {
        callAdd().andExpect(view().name("redirect:/api/admin"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    public void addUserShouldThrowUserExist() throws Exception {
        callAdd();
        callAdd().andExpect(view().name("404"));
    }

    private ResultActions callAdd() throws Exception {
        final MockHttpServletRequestBuilder request =
                post(PATH_ADD)

                        .param("name", "5")
                        .param("username", "5")
                        .param("password", "5")
                        .param("logoUrl", "5kkkkkkkk")
                        .param("roles", "ADMIN")
                        .param("permission", "ADD");
        return mockMvc.perform(request);
    }
    @Test
    @SneakyThrows
    @WithMockUser(roles = "SUPER_ADMIN")
    void update() throws Exception {
        callAdd();
        callUpdate("5").andExpect(view().name("redirect:/api/admin"));
    }

    private ResultActions callUpdate(String username) throws Exception{
        final MockHttpServletRequestBuilder request =
                post("/api/admin/update/{username}",username )
                        .param("name", "8")
                        .param("username", "85")
                        .param("password", "58")
                        .param("logoUrl", "7777777777777777777")
                        .param("roles", "ADMIN")
                        .param("permission", "ADD");
        return mockMvc.perform(request);
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void getAdmins() throws Exception {
        callAdd();
        final MockHttpServletRequestBuilder request =
                get(PATH_list);
        mockMvc.perform(request).andExpect(view().name("CrudAdmin"));

    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void delete() throws Exception {
        callAdd();
        ResultActions perform = mockMvc.perform(get("/api/admin/delete/{id}", 1));
        perform.andExpect(view().name("redirect:/api/admin"));
    }

}