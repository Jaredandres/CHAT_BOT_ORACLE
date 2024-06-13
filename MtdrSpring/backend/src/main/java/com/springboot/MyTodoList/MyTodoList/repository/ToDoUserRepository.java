package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.ToDoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ToDoUserRepository extends JpaRepository<ToDoUser, Integer> {
    Optional<ToDoUser> findByNumeroTelefono(String numeroTelefono);
}
