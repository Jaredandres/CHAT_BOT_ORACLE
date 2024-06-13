package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoUser;
import com.springboot.MyTodoList.repository.ToDoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;

@Service
public class ToDoUserService {

    @Autowired
    private ToDoUserRepository toDoUserRepository;
    

    public List<ToDoUser> getAllToDoUsers() {
        return toDoUserRepository.findAll();
    }

    public ToDoUser getToDoUserById(int id) {
        return toDoUserRepository.findById(id).orElse(null);
    }

    public ToDoUser addToDoUser(ToDoUser toDoUser) {
        return toDoUserRepository.save(toDoUser);
    }

    public void deleteToDoUser(int id) {
        toDoUserRepository.deleteById(id);
    }

    public Optional<ToDoUser> findUserByPhoneNumber(String phoneNumber) {
        return toDoUserRepository.findByNumeroTelefono(phoneNumber);
    }

    public ToDoUser updateToDoUser(ToDoUser toDoUser) {
        return toDoUserRepository.save(toDoUser);
    }
}
