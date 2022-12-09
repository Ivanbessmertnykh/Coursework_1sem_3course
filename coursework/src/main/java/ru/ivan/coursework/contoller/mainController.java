package ru.ivan.coursework.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.ivan.coursework.service.ResService;

@Controller
public class mainController {
    @Autowired
    private ResService resService;

    @GetMapping({"/index", "/"})
    public String getIndex(){return "index";}
    @GetMapping("/about-us")
    public String getAbout(){return "about-us";}
    @GetMapping("/contact")
    public String getContact(){return "contact";}
    @GetMapping("/service")
    public String getService(){return "service";}
    @GetMapping("/team")
    public String getTeam(){return "team";}
    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }
    @GetMapping("/reserve")
    public String getReserve(Model model){
        model.addAllAttributes(resService.checkTime());
        return "reserve";
    }
}
