package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.ToDoTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoTeamRepository extends JpaRepository<ToDoTeam, Integer> {
}
