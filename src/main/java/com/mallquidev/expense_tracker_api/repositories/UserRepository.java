package com.mallquidev.expense_tracker_api.repositories;

import com.mallquidev.expense_tracker_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //1
    Optional<User> findByUserName(String userName); //busca un usuario por su nombre, puede devolver vacio si no lo encuentra
    boolean existsByUserName(String userName); //Verifica si existe un usuario con ese nombre que le damos
}
