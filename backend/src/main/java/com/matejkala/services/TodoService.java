package com.matejkala.services;

import com.matejkala.dtos.requestdtos.TodoCreateRequestDTO;
import com.matejkala.dtos.requestdtos.UpdateTodoDTO;
import com.matejkala.dtos.responsedtos.SuccessTodoCreationResponseDTO;
import com.matejkala.dtos.responsedtos.TodoResponseDTO;
import com.matejkala.models.Todo;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TodoService {

    Todo saveTodo(Todo todo);

    SuccessTodoCreationResponseDTO createNewTodo(Authentication authentication, TodoCreateRequestDTO request);

    TodoResponseDTO fetchTodoById(Authentication authentication, Long id);

    TodoResponseDTO updateTodo(Authentication authentication, Long id, UpdateTodoDTO request);

    void deleteTodo(Authentication authentication, Long id);

    void markAsCompleted(Todo todo);

    List<TodoResponseDTO> getAllTodosBasedOnRole(Authentication authentication);
}
