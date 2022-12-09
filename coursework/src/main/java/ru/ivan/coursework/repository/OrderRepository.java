package ru.ivan.coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ivan.coursework.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}