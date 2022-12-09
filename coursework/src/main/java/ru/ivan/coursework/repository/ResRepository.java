package ru.ivan.coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ivan.coursework.entity.Res;

import java.util.List;

@Repository
public interface ResRepository extends JpaRepository<Res, Integer> {
    Res findByNumberOfTime(int numberOfTime);
    List<Res> findAllByNumberOfTimeLessThan(int time);
    Res findResByNumberOfTimeGreaterThan(int time);
    Res findById(int id);
}
