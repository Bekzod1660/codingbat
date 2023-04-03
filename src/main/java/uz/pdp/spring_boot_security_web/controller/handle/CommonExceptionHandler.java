package uz.pdp.spring_boot_security_web.controller.handle;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.pdp.spring_boot_security_web.common.exception.RecordAlreadyExist;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;

@Controller
@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(RecordNotFountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String recordNotFound(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "404";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badRequest(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "404";
    }
    @ExceptionHandler(RecordAlreadyExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String recordAlreadyExist(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "404";
    }
}
