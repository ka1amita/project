package com.gfa.controllers;

import static com.gfa.utils.Endpoint.USERS_API;

import com.gfa.config.PaginationProperties;
import com.gfa.dtos.responsedtos.AppUserResponseDTO;
import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.requestdtos.UpdateAppUserDTO;
import com.gfa.dtos.responsedtos.RegisterResponseDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import com.gfa.services.AppUserService;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping(USERS_API)
public class UserRestController {
  private final PaginationProperties paginationProperties;
  private final AppUserService appUserService;

  private final MessageSource messageSource;

  @Autowired
  public UserRestController(PaginationProperties paginationProperties, AppUserService appUserService, MessageSource messageSource) {
    this.paginationProperties = paginationProperties;
    this.appUserService = appUserService;
      this.messageSource = messageSource;
  }
    @GetMapping("")
    public ResponseEntity<List<? extends ResponseDTO>> index(@RequestParam(required = false,name = "page")
                                   Optional<Integer> optPage,
                                   @RequestParam(required = false,name = "size")
                                   Optional<Integer> optSize,
                                   @RequestParam(required = false,name = "by")
                                   Optional<String> optSortBy,
                                   @RequestParam(required = false,name = "direction")
                                   Optional<Sort.Direction> optSortDirection) {
        PageRequest request = getPageRequest(optPage, optSize, optSortBy, optSortDirection);
        Page<AppUserResponseDTO> users = appUserService.pageAppUserDtos(request);
        return ResponseEntity.ok(users.toList());
  }

    @PostMapping("")
    public ResponseEntity<? extends ResponseDTO> store(@Valid @RequestBody RegisterRequestDTO request) throws MessagingException {
        appUserService.registerUser(request);
        return ResponseEntity.ok(new RegisterResponseDTO(messageSource.getMessage("dto.register.response", null, LocaleContextHolder.getLocale())));
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<? extends ResponseDTO>> indexDeleted(@RequestParam(required = false,name = "page")
                                                                        Optional<Integer> optPage,
                                                                    @RequestParam(required = false,name = "size")
                                                                        Optional<Integer> optSize,
                                                                    @RequestParam(required = false,name = "by")
                                                                        Optional<String> optSortBy,
                                                                    @RequestParam(required = false,name = "direction")
                                                                        Optional<Sort.Direction> optSortDirection) {
        PageRequest request = getPageRequest(optPage, optSize, optSortBy, optSortDirection);
        return ResponseEntity.ok(appUserService.pageDeletedAppUserDtos(request).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ResponseDTO> show(@PathVariable Long id) {
        return ResponseEntity.ok(appUserService.fetchUserApi(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends ResponseDTO> update(@Valid @RequestBody(required = false) UpdateAppUserDTO request,
                                                      @PathVariable Long id) throws MessagingException {
        return ResponseEntity.ok(appUserService.updateAppUserApi(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends ResponseDTO> destroy(@PathVariable Long id) {
        appUserService.removeAppUser(id);
        return ResponseEntity.status(201).build();
    }

    @NotNull
    private PageRequest getPageRequest(@NotNull Optional<Integer> optPage,
                                       @NotNull Optional<Integer> optSize,
                                       @NotNull Optional<String> optSortBy,
                                       @NotNull Optional<Sort.Direction> optSortDirection) {
        Integer page = optPage.orElse(1) - 1; // converts to a zero-based page index
        Integer size = optSize.orElse(paginationProperties.getPageSizeDefault());
        String sortBy = optSortBy.orElse(paginationProperties.getSortBy());
        Sort.Direction sortDirection = optSortDirection.orElse(paginationProperties.getSortOrder());

        Assert.isTrue(size <= paginationProperties.getPageSizeMax(), messageSource.getMessage("assert.pagination", null, LocaleContextHolder.getLocale()) + paginationProperties.getPageSizeMax());

        Sort sort = Sort.by(sortDirection,sortBy);
        return PageRequest.of(page, size, sort);
    }
}