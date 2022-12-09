package ru.ivan.coursework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivan.coursework.entity.*;
import ru.ivan.coursework.repository.ItemRepository;
import ru.ivan.coursework.repository.OrderRepository;
import ru.ivan.coursework.repository.RoleRepository;
import ru.ivan.coursework.repository.UserRepository;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    @Transactional
    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void saveUser(User user){
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null){
            return;
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findRoleByName("ROLE_USER");
        Order order = new Order();
        order.setDone(0);
        order.setUser_id(user);
        user.getRoles().add(role);
        user.getOrders(true).add(order);
        userRepository.save(user);
    }

    @Transactional
    public void addItemToCustomer(User user, Long ItemID){
        int count = user.getItemCountByItemId(ItemID);
        Order actual = user.getActualOrder();
        Item item = itemRepository.findItemByItemID(ItemID);
        List<OrderItemEnroll> enrolls = actual.getOrderDishEnrolls();
        if (count == 0){
            OrderItemEnroll newEnroll = new OrderItemEnroll();
            newEnroll.setItem(item);
            newEnroll.setCount(1);
            newEnroll.setOrder(actual);
            enrolls.add(newEnroll);
            userRepository.createEnroll(ItemID, actual.getOrderId());
        } else {
            OrderItemEnroll currentEnroll = actual.findEnrollByDishId(ItemID);
            currentEnroll.setCount(count + 1);
            userRepository.updateEnroll(ItemID, actual.getOrderId(), count + 1);
        }
        actual.setOrderDishEnrolls(enrolls);
        item.setOrderDishEnrolls(enrolls);
        userRepository.save(user);
    }

    @Transactional
    public void removeDishToCustomer(User user, Long dishId){
        int count = user.getItemCountByItemId(dishId);
        Order actual = user.getActualOrder();
        Item dish = itemRepository.findItemByItemID(dishId);
        List<OrderItemEnroll> enrolls = actual.getOrderDishEnrolls();
        if (count == 0){
            return;
        }
        OrderItemEnroll currentEnroll = actual.findEnrollByDishId(dishId);
        if (count == 1){
            enrolls.remove(currentEnroll);
            userRepository.deleteEnroll(dishId, actual.getOrderId());
        } else {
            currentEnroll.setCount(currentEnroll.getCount() - 1);
            userRepository.updateEnroll(dishId, actual.getOrderId(), count - 1);
        }
        actual.setOrderDishEnrolls(enrolls);
        dish.setOrderDishEnrolls(enrolls);
        userRepository.save(user);
    }

    @Transactional
    public void rootReSave(User user, boolean chpass){
        if (chpass){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Transactional
    public List<Item> getCustomerDishes(User user){
        Order actual = user.getActualOrder();
        List<Item> dishes = new ArrayList<>();
        for(OrderItemEnroll enroll: actual.getOrderDishEnrolls()){
            dishes.add(enroll.getItem());
        }
        return dishes;
    }

    @Transactional
    public double getTotalCost(User user){
        double result = 0;
        Order actual = user.getActualOrder();
        for(OrderItemEnroll enroll: actual.getOrderDishEnrolls()){
            result += enroll.getItem().getCost() * enroll.getCount();
        }
        return result;
    }

    @Transactional
    public void removeDishes(User user){
        Order deletingOrder = user.getActualOrder();
        deletingOrder.getOrderDishEnrolls().clear();
        userRepository.save(user);
    }

    @Transactional
    public void makeOrder(User user, String comment){
        Order makingOrder = user.getActualOrder();
        makingOrder.setDone(1);
        makingOrder.setComment(comment);
        makingOrder.setTime(new Date());
        Order order = new Order();
        order.setDone(0);
        order.setUser_id(user);
        order = orderRepository.save(order);
        user.getOrders(true).add(order);
        userRepository.save(user);
    }

}
