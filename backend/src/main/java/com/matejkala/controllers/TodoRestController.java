package com.matejkala.controllers;

import com.matejkala.dtos.requestdtos.TodoCreateRequestDTO;
import com.matejkala.dtos.requestdtos.UpdateTodoDTO;
import com.matejkala.dtos.responsedtos.ResponseDTO;
import com.matejkala.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.matejkala.utils.Endpoint.TODO_API;

@RestController
@RequestMapping(TODO_API)
public class TodoRestController {
    private final TodoService todoService;
    @Autowired
    public TodoRestController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("")
    public ResponseEntity<List<? extends ResponseDTO>> index(Authentication authentication) {
        return ResponseEntity.ok(todoService.getAllTodosBasedOnRole(authentication));
    }

    @PostMapping("")
    public ResponseEntity<? extends ResponseDTO> create(Authentication authentication,
                                                         @Valid @RequestBody(required = false) TodoCreateRequestDTO request) {
        return ResponseEntity.ok(todoService.createNewTodo(authentication, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ResponseDTO> show(Authentication authentication,
                                                      @PathVariable Long id) {
        return ResponseEntity.ok(todoService.fetchTodoById(authentication, id));
    }

    @PatchMapping("{id}")
    public ResponseEntity<? extends ResponseDTO> update(Authentication authentication,
                                                        @PathVariable Long id,
                                                        @RequestBody(required = false) UpdateTodoDTO request) {
        return ResponseEntity.ok(todoService.updateTodo(authentication, id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<? extends ResponseDTO> destroy(Authentication authentication,
                                                         @PathVariable Long id) {
        todoService.deleteTodo(authentication,id);
        return ResponseEntity.status(201).build();
    }
}
