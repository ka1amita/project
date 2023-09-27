package com.gfa.services;

import com.gfa.config.SoftDeleteConfig;
import com.gfa.dtos.requestdtos.TodoCreateRequestDTO;
import com.gfa.dtos.requestdtos.UpdateTodoDTO;
import com.gfa.dtos.responsedtos.SuccessTodoCreationResponseDTO;
import com.gfa.dtos.responsedtos.TodoResponseDTO;
import com.gfa.exceptions.role.NoPermissionForRequestException;
import com.gfa.exceptions.todo.TodoAlreadyExistsWithThisNameForThisUserException;
import com.gfa.exceptions.todo.TodoNotFoundException;
import com.gfa.models.AppUser;
import com.gfa.models.Todo;
import com.gfa.repositories.TodoRepository;
import com.gfa.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final AppUserService appUserService;
    private final SoftDeleteConfig softDeleteConfig;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository, AppUserService appUserService, SoftDeleteConfig softDeleteConfig) {
        this.todoRepository = todoRepository;
        this.appUserService = appUserService;
        this.softDeleteConfig = softDeleteConfig;
    }

    @Override
    public Todo saveTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public SuccessTodoCreationResponseDTO createNewTodo(Authentication authentication, TodoCreateRequestDTO request) {
        Utils.isJSONBodyPresent(request);
        if (!Utils.hasAdminRole(authentication) && !authentication.getName().equals(request.getAppUser())) {
            throw new NoPermissionForRequestException("Permission denied");
        }
        AppUser appUser = appUserService.findUserByUsername(request.getAppUser());
        checkIfTodoWithTitleExistsForUser(appUser, request.title);

        Todo todo = new Todo(request.getTitle(),
                request.getDescription(),
                LocalDateTime.parse(request.getDueDate()),
                appUser);
        appUser.getTodos().

                add(todo);

        saveTodo(todo);

        return new SuccessTodoCreationResponseDTO("Todo creation successful");
    }

    @Override
    public TodoResponseDTO fetchTodoById(Authentication authentication, Long id) {
        Todo todo = inputDataAndPermissionValidation(authentication, id);
        return new TodoResponseDTO(todo);
    }

    @Override
    public TodoResponseDTO updateTodo(Authentication authentication, Long id, UpdateTodoDTO request) {
        Utils.isJSONBodyPresent(request);
        AppUser appUser = appUserService.findUserByUsername(authentication.getName());
        Todo todo = inputDataAndPermissionValidation(authentication, id);

        if (Utils.isNotNullOrEmpty(request.getTitle())) {
            checkIfTodoWithTitleExistsForUser(appUser, request.title);
            todo.setTitle(request.getTitle());
        }

        if (Utils.isNotNullOrEmpty(request.getDescription())) {
            todo.setDescription(request.getDescription());
        }

        if (Utils.isNotNullOrEmpty(request.getDueDate())) {
            todo.setDueDate(LocalDateTime.parse(request.getDueDate()));
        }

        if (Utils.isNotNullOrEmpty(request.getAppUser())) {
            todo.setAppUser(appUserService.findUserByUsername(request.getAppUser()));
        }
        saveTodo(todo);
        return new TodoResponseDTO(todo);
    }

    @Override
    public void deleteTodo(Authentication authentication, Long id) {
        Todo todo = inputDataAndPermissionValidation(authentication, id);
        if (softDeleteConfig.isEnabled()) {
            todo.setDeleted(true);
            saveTodo(todo);
        } else {
            todoRepository.deleteById(id);
        }
    }

    @Override
    public void markAsCompleted(Todo todo) {
        todo.setCompleted(true);
        saveTodo(todo);
    }

    @Override
    public List<TodoResponseDTO> getAllTodosBasedOnRole(Authentication authentication) {
        Stream<Todo> todoStream;
        if (Utils.hasAdminRole(authentication)) {
            todoStream = todoRepository.findAll().stream();
        } else {
            todoStream = todoRepository.findByAppUser_Username(authentication.getName()).stream();
        }
        return todoStream.map(TodoResponseDTO::new).collect(Collectors.toList());
    }

    private void checkIfTodoWithTitleExistsForUser(AppUser appUser, String title) {
        if (appUser.getTodos().stream()
                .anyMatch(todo -> todo.getTitle().equals(title))) {
            throw new TodoAlreadyExistsWithThisNameForThisUserException("Todo already exists with this name for this user");
        }
    }

    private Todo inputDataAndPermissionValidation(Authentication authentication, Long id) {
        Utils.IsProvidedIdValid(id);

        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException("Todo not found"));
        AppUser todoOwner = appUserService.findUserByUsername(todo.getAppUser().getUsername());

        if (!Utils.hasAdminRole(authentication) && !authentication.getName().equals(todoOwner.getUsername())) {
            throw new NoPermissionForRequestException("Permission denied");
        }
        return todo;
    }
}
