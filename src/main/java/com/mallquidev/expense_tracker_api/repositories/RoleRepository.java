package com.mallquidev.expense_tracker_api.repositories;

import com.mallquidev.expense_tracker_api.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    //2
    Optional<Role> findByName(String name);
}
