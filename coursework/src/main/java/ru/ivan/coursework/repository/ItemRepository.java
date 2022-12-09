package ru.ivan.coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ivan.coursework.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findItemByItemID(Long itemID);
    List<Item> findAllBy();
}