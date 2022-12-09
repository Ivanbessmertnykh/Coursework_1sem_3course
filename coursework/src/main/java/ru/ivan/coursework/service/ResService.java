package ru.ivan.coursework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivan.coursework.entity.Res;
import ru.ivan.coursework.entity.User;
import ru.ivan.coursework.repository.ResRepository;
import ru.ivan.coursework.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResService {

    @Autowired
    private ResRepository resRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;

    @Transactional
    public boolean addRes(int numberOfTime){
        Res res = resRepository.findByNumberOfTime(numberOfTime);
        if (res == null){
            res = new Res(numberOfTime);
            resRepository.save(res);
            return true;
        }
        return false;
    }

    @Async
    public void sendRes(User user, int numberOfTime){
        String email = user.getEmail();
        int time = 1000 + 100 * ((numberOfTime - 1) / 2) + 30 * ((numberOfTime - 1) % 2);
        String timeS = String.valueOf(time);
        System.out.println(timeS);
        String mailBody = "Здравствуйте, " + user.getUsername() + ". Вы успешно записались в Rezka Barber на время - "
                + timeS.charAt(0) + timeS.charAt(1) + ":" + timeS.charAt(2) + timeS.charAt(3);
        mailService.sendEmail(user.getEmail(), "Запись оформлена!", mailBody);
    }

    @Transactional
    public void nextDay(){
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 100;
        Res res = new Res(day);
        resRepository.deleteAll();
        resRepository.save(res);
        List<User> users = userRepository.findAllBy();
        for (User user: users){
            user.setReserved(false);
        }
    }

    @Transactional
    public Map<String, Boolean> checkTime(){
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        for(int i = 1; i < 9; i++){
            map.put("c" + i, resRepository.findByNumberOfTime(i) == null);
        }
        return map;
    }

}
