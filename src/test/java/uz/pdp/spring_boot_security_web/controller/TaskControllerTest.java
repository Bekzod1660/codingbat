//package uz.pdp.spring_boot_security_web.controller;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.testcontainers.lifecycle.Startables;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//
//class TaskControllerTest extends BaseTest {
//
//    @BeforeAll
//    static void beforeAll() {
//        Startables.deepStart(postgres).join();
//    }
//
//    @AfterEach
//    void afterAll() {
//        taskRepository.deleteAll();
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void topicHomePageIsSuccess() throws Exception {
//
//        callAddTopic().andExpect(view().name("task"));
//    }
//
//    private ResultActions callAddTopic() throws Exception {
//        final MockHttpServletRequestBuilder request=
//                get("/admin/{language}/{topic}/{id}","java","topic",1);
//        return mockMvc.perform(request);
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void getTopicQuestionIsSuccessful() throws Exception {
//        callTask();
//        ResultActions resultActions = mockMvc.perform(get("/task/{id}", 1));
//        resultActions.andExpect(view().name("compiler"));
//    }
//
//
//    @Test
//    void addTask() throws Exception {
//        callTask().andExpect(view().name("redirect:/admin/lang"));
//    }
//
//    @Test
//    void addTask2() throws Exception {
//        callTask();
//        callTask().andExpect(view().name("redirect:/admin/lang"));
//    }
//
//    private ResultActions callTask() throws Exception {
//
//        mockMvc.perform(post("/admin/topic/add")
//                .param("name", "hhhh")
//                .param("content", "hhh")
//                .param("language", "Java"));
//        final MockHttpServletRequestBuilder request =
//                post("/admin/task/add")
//                        .param("name", "aaa")
//                        .param("title", "aa")
//                        .param("topicId", "1");
//        return mockMvc.perform(request);
//    }
//
//    //////////////////////////////////////////////////////////////////////////////////
//    @Test
//    void deleteTask() throws Exception {
//        callTask();
//        ResultActions perform = mockMvc.perform(get("/admin/task/del/{id}", 1));
//        perform.andExpect(view().name("redirect:/admin/lang"));
//    }
//
//    ////////////////////////////////////////////////////
//    @Test
//    void updateTask() throws Exception {
//        callTask();
//        ResultActions perform = mockMvc.perform(post("/admin/task/update/{id}", 1)
//                .param("name", "bbb")
//                .param("title", "bb")
//                .param("topicId", "1"));
//        perform.andExpect(view().name("redirect:/admin/lang"));
//
//    }
//}