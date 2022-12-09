package ru.ivan.coursework.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.ivan.coursework.entity.User;
import ru.ivan.coursework.repository.UserRepository;
import ru.ivan.coursework.service.ResService;

@Controller
public class resController {

    @Autowired
    private ResService resService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/a_{number:\\d+}")
    public String addB(@PathVariable int number, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if (user.isReserved()) {
            model.addAttribute("errorSetting", true);
            model.addAttribute("errorMessage", "Вы уже записались!");
        } else {
            boolean flag = resService.addRes(number);
            if (flag) {
                model.addAttribute("infoSetting", true);
                model.addAttribute("infoMessage", "Вы успешно записались! Информация отправлена на почту");
                user.setReserved(true);
                userRepository.save(user);
                resService.sendRes(user, number);
            } else {
                model.addAttribute("errorSetting", true);
                model.addAttribute("errorMessage", "Данное время уже занято!");
            }
        }
        model.addAllAttributes(resService.checkTime());
        return "reserve";
    }



}
