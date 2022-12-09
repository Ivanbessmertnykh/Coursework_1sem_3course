package ru.ivan.coursework.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_items")
public class OrderItemEnroll implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "itemId")
    private Item item;

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "orderId")
    private Order order;

    @JoinColumn
    private int count;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
