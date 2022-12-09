package ru.ivan.coursework.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemID;

    @OneToMany(mappedBy = "item")
    private List<OrderItemEnroll> orderDishEnrolls = new ArrayList<>();

    public Item(String name, Integer cost, byte[] image) {
        this.name = name;
        this.cost = cost;
        this.image = image;
    }

    private String name;
    private Integer cost;
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] image;

    public Item() {

    }

    public Long getItemID() {
        return itemID;
    }

    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getImage() {
        return new String(image, StandardCharsets.UTF_8);
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<OrderItemEnroll> getOrderDishEnrolls() {
        return orderDishEnrolls;
    }

    public void setOrderDishEnrolls(List<OrderItemEnroll> orderDishEnrolls) {
        this.orderDishEnrolls = orderDishEnrolls;
    }
}
