package ru.ivan.coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ivan.coursework.entity.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);
}