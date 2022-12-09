package ru.ivan.coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivan.coursework.entity.User;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    List<User> findAllBy();

    @Transactional
    @Modifying
    @Query(value = "UPDATE order_items SET item_id = ?1, order_id = ?2, count = ?3 where item_id = ?1 and order_id = ?2", nativeQuery = true)
    public void updateEnroll(Long ItemId, Long OrderId, int newCount);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO order_items(item_id, order_id, count) VALUES(?1,?2, 1)", nativeQuery = true)
    public void createEnroll(Long ItemId, Long OrderId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM order_items where item_id = ?1 and order_id = ?2", nativeQuery = true)
    public void deleteEnroll(Long ItemId, Long OrderId);
}
