package ostro.veda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
