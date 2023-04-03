package uz.pdp.spring_boot_security_web.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.spring_boot_security_web.repository.TestCaseRepository;
import uz.pdp.spring_boot_security_web.model.dto.TaskRequestDTO;
import uz.pdp.spring_boot_security_web.service.LanguageService;
import uz.pdp.spring_boot_security_web.service.TestCaseService;
import uz.pdp.spring_boot_security_web.service.TaskService;
import uz.pdp.spring_boot_security_web.service.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import uz.pdp.spring_boot_security_web.compiler.CompliedClass;
import uz.pdp.spring_boot_security_web.entity.*;
import org.springframework.security.core.Authentication;
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class TaskController {
    private final LanguageService languageService;
    private final TaskService taskService;
    private final TestCaseService testCaseService;
    private final CompliedClass compliedClass;
    private final TestCaseRepository testCaseRepository;
    private final TopicService topicService;

    @GetMapping("admin/{language}/{topic}/{id}")
    public ModelAndView topicHome(@PathVariable int id, ModelAndView model) {
        TopicEntity byId = topicService.getById(id);
        model.addObject("topic", byId);
        model.addObject("taskList", byId.getTaskEntityList());
        model.setViewName("task");
        return model;
    }

    @PostMapping("/admin/task/add")
    public String addTask(
            @ModelAttribute TaskRequestDTO taskRequestDTO
    ) {
        taskService.addTask(taskRequestDTO);
        return "redirect:/admin/lang";
    }

    @GetMapping("/admin/task/del/{id}")
    public String deleteTask(
            @PathVariable int id
    ) {
        taskService.delete(id);
        return "redirect:/admin/lang";
    }

    @PostMapping("/admin/task/update/{id}")
    public String updateTask(
            @PathVariable int id,
            @ModelAttribute TaskRequestDTO taskRequestDTO
    ) {
        taskService.update(id, taskRequestDTO);
        return "redirect:/admin/lang";
    }
    @PostMapping("task/response/{id}")
    public String checkAnswer(@PathVariable int id, HttpServletRequest request, Model model) {
        Object objectUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!objectUser.equals("anonymousUser")) {
            UserEntity userEntity = (UserEntity) objectUser;
            TaskEntity question = taskService.getById(id);
            String code = request.getParameter("code");
            String[] split = question.getMethodAndParams().split(",");
            List<String> passedTestCases = null;
            if (code != null) {
                List<TestCaseEntity> testCases = testCaseService.testCasesOfQuestion(question.getId());
                passedTestCases = compliedClass.passAllTestCases(testCases, split, code);
                int size = testCaseService.quantityOfSuccessfulTestCases(passedTestCases).size();
                String tick = "❌";
                if (size == testCases.size()) {
                    taskService.makeQuestionSolved(userEntity, question);
                    tick = "✅";
                }
                model.addAttribute("passMessage", tick + " " + size + " test cases out of " + testCases.size() + " passed successfully");
            }
            List<LanguageEntity> list = languageService.languageEntityList();
            model.addAttribute("passedTestCases", passedTestCases);
            model.addAttribute("compile", code);
            model.addAttribute("question", question);
            model.addAttribute("subjects", list);
            String str = taskService.getTopicNameByQuestion(question, list);
            if (str != null) {
                model.addAttribute("topic", str);
            }
        } else {
            return "register";
        }
        return "Question";
    }
    @GetMapping("/task/{id}")
    public String getTopicQuestion(@PathVariable int id, Model model) {
        TaskEntity question = taskService.getById(id);
        List<LanguageEntity> list = languageService.languageEntityList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = null;
        if (!(authentication.getPrincipal() + "").equals("anonymousUser")) {
            user = (UserEntity) authentication.getPrincipal();
            model.addAttribute("users", user);
        }
        if (question.getMethodAndParams() != null) {
            String[] split = question.getMethodAndParams().split(",");
            model.addAttribute("compile",
                    "public class Solution {\n"
                            + "    public " + split[0] + " " + split[1] + "(" + split[2] + " firstParam, " + split[3] + " secondParam) {\n"
                            + "        return ;\n"
                            + "    }\n"
                            + "}");
        }
        model.addAttribute("question", question);
        model.addAttribute("subjects", list);
        String str = taskService.getTopicNameByQuestion(question, list);
        if (str != null) {
            model.addAttribute("topic", str);
        }
        return "Question";
    }
}
