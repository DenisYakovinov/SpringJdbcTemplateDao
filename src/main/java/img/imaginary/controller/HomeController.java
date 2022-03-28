package img.imaginary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String helloWorld(Model model) {
        model.addAttribute("message", "home university!");
        return "home";
    }
}
