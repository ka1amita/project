package com.gfa.services;

import com.gfa.dtos.responsedtos.TodoResponseDTO;
import com.gfa.models.AppUser;
import com.gfa.models.Todo;
import com.gfa.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Todo saveTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Todo createNewTodo(String title, String description, LocalDateTime dueDate, AppUser appUser) {
        return new Todo(title,description,dueDate,appUser);
    }

    @Override
    public List<TodoResponseDTO> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(TodoResponseDTO::new)
                .collect(Collectors.toList());
    }
}
