package ru.ivan.coursework.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private Date time;
    private String comment;
    private Integer done;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user_id;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderItemEnroll> orderItemEnrolls = new ArrayList<>();

    public Order(Date time, String comment, Integer done, User user_id) {
        this.time = time;
        this.comment = comment;
        this.done = done;
        this.user_id = user_id;
    }

    public List<OrderItemEnroll> getOrderDishEnrolls() {
        return orderItemEnrolls;
    }

    public void setOrderDishEnrolls(List<OrderItemEnroll> orderDishEnrolls) {
        this.orderItemEnrolls = orderDishEnrolls;
    }

    public Order() {

    }

    public OrderItemEnroll findEnrollByDishId(Long ItemID){
        for(OrderItemEnroll enroll: orderItemEnrolls){
            if (Objects.equals(enroll.getItem().getItemID(), ItemID) && Objects.equals(enroll.getOrder().getOrderId(), orderId))
                return enroll;
        }
        return null;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public List<OrderItemEnroll> getOrderItemEnrolls() {
        return orderItemEnrolls;
    }

    public void setOrderItemEnrolls(List<OrderItemEnroll> orderItemEnrolls) {
        this.orderItemEnrolls = orderItemEnrolls;
    }
}

