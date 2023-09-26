package com.gfa.services;

import com.gfa.dtos.responsedtos.TodoResponseDTO;
import com.gfa.models.AppUser;
import com.gfa.models.Todo;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoService {

    Todo saveTodo(Todo todo);

    Todo createNewTodo(String title, String description, LocalDateTime dueDate, AppUser appUser);

    List<TodoResponseDTO> getAllTodos();
}
