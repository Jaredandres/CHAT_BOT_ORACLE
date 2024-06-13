package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoTeam;
import com.springboot.MyTodoList.repository.ToDoTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoTeamService {

    @Autowired
    private ToDoTeamRepository toDoTeamRepository;

    public List<ToDoTeam> findAll() {
        return toDoTeamRepository.findAll();
    }

    public ToDoTeam findById(int id) {
        return toDoTeamRepository.findById(id).orElse(null);
    }

    public ToDoTeam save(ToDoTeam toDoTeam) {
        return toDoTeamRepository.save(toDoTeam);
    }

    public void deleteById(int id) {
        toDoTeamRepository.deleteById(id);
    }

    public List<ToDoTeam> findAllTeams() {
        return toDoTeamRepository.findAll();
    }
}
