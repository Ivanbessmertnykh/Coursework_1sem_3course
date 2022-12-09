package ru.ivan.coursework.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivan.coursework.entity.Item;
import ru.ivan.coursework.entity.User;
import ru.ivan.coursework.repository.ItemRepository;
import ru.ivan.coursework.service.MailService;
import ru.ivan.coursework.service.UserService;

import java.io.IOException;
import java.util.Base64;

@Controller
public class excludeController {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MailService mailService;


    @GetMapping("/adding_{number:\\d+}")
    public String addingItem(@PathVariable Long number){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        User customer = (User) auth.getPrincipal();
        userService.addItemToCustomer(customer, number);
        return "redirect:/shop#item_" + number;
    }

    @GetMapping("/removing_{number:\\d+}")
    public String removingItem(@PathVariable Long number){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        User customer = (User) auth.getPrincipal();
        userService.removeDishToCustomer(customer, number);
        return "redirect:/shop#item_" + number;
    }

    @GetMapping("/shop")
    public String shop(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            User customer = (User) auth.getPrincipal();
            model.addAttribute("user", customer);
        }
        model.addAttribute("items", itemRepository.findAllBy());
        return "shop";
    }


    @GetMapping("/admin")
    public String getAdminPanel(Model model){
        return "admin";
    }

    @GetMapping("/admin-item")
    public String dish(Model model){
        model.addAttribute("addItem", true);
        return "admin";
    }

    @PostMapping("/admin-item")
    public String getDish(Model model,
                          @ModelAttribute("name") String name,
                          @ModelAttribute("cost") Integer cost,
                          @RequestParam("image") MultipartFile file) throws IOException {
        byte[] image = Base64.getEncoder().encode(file.getBytes());
        Item dish = new Item(name, cost, image);
        itemRepository.save(dish);
        model.addAttribute("errorSetting", "true");
        model.addAttribute("message", "Предмет сохранен");
        return "admin";
    }

    @GetMapping("/cart")
    public String getCart(Model model){
        User current_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("dishes", userService.getCustomerDishes(current_user));
        model.addAttribute("totalCost", userService.getTotalCost(current_user));
        return "cart";
    }

    @GetMapping("/cart-clear")
    public String clearCart(Model model){
        User current_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("infoSetting", true);
        model.addAttribute("infoMessage", "Ваша корзина успешно очищена");
        userService.removeDishes(current_user);
        return "cart";
    }

    @PostMapping("/cart")
    public String orderCart(Model model, @ModelAttribute("comment") String comment){
        User current_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.makeOrder(current_user, comment);
        model.addAttribute("infoSetting", true);
        model.addAttribute("infoMessage", "Ваш заказ оформлен");
        mailService.sendEmail(current_user.getEmail(), "ЗАКАЗ ОФОРМЛЕН", "Ваш заказ оформлен, информацию о нем можно посмотреть на вкладке Заказы на сайте");
        return "cart";
    }

    @GetMapping("/orders")
    public String orders(Model model){
        User current_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("orders", current_user.getOrders(false));
        return "orders";
    }

    @GetMapping("/lk")
    public String lk(){
        return "lk";
    }

    public Model addInfoAboutSession(Model model){
        model.addAttribute("infoSetting", true);
        model.addAttribute("infoMessage", "После смены данных вы выйдете из своего личного кабинета");
        return model;
    }

    @GetMapping("/lk-username")
    public String chUser(Model model){
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("changeUsername", true);
        model = addInfoAboutSession(model);
        return "lk";
    }

    @PostMapping("/lk-username")
    public String confUser(@ModelAttribute("username") String phoneNumber, Model model){
        if (phoneNumber.length() < 5){
            model.addAttribute("errorSetting", true);
            model.addAttribute("message", "Имя пользователя должно быть не менее 5 символов");
            return "lk";
        }
        else if (userService.findUserByUsername(phoneNumber) != null){
            model.addAttribute("errorSetting", true);
            model.addAttribute("message", "Пользователь с таким ником уже существует");
            return "lk";
        }
        User changed_user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        changed_user.setUsername(phoneNumber);
        userService.rootReSave(changed_user, false);
        return "redirect:/logout";
    }

    @GetMapping("/lk-password")
    public String chPass(Model model){
        model.addAttribute("changePassword", true);
        model = addInfoAboutSession(model);
        return "lk";
    }

    @PostMapping("/lk-password")
    public String confPass(@ModelAttribute("newPassword") String newPassword, Model model){
        User current_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (newPassword.length() < 5){
            model.addAttribute("errorSetting", true);
            model.addAttribute("message", "Новый пароль должен быть не менее 5 символов");
            return "lk";
        }
        current_user.setPassword(newPassword);
        userService.rootReSave(current_user, true);
        return "redirect:/logout";
    }

    @GetMapping("/lk-email")
    public String chEmail(Model model){
        User current_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("changeEmail", true);
        model.addAttribute("email", current_user.getEmail());
        model = addInfoAboutSession(model);
        return "lk";
    }

    @PostMapping("/lk-email")
    public String chEmail(@ModelAttribute("email") String email, Model model){
        User current_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current_user.setEmail(email);
        userService.rootReSave(current_user, false);
        return "redirect:/logout";
    }
}
