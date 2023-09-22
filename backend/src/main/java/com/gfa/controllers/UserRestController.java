package com.gfa.controllers;

import static com.gfa.utils.Endpoint.USERS;

import com.gfa.services.AppUserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(USERS)
public class UserRestController {
  @Value("${pagination.size.default}")
  private Integer defaultSize;
  @Value("${pagination.size.max}")
  private Integer maxSize;
  @Value("${pagination.sort.by}")
  private String defaultSortBy;
  @Value("${pagination.sort.order}")
  private Sort.Direction defaultSortOrder;
  public final AppUserService appUserService;

  @Autowired
  public UserRestController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> destroy(@PathVariable Long id) {
    appUserService.removeAppUser(id);
    return ResponseEntity.status(201).build();
  }
  @PostMapping("/list")
  public ResponseEntity<?> getPagedActiveNonDeletedUsers(@RequestParam(required = false,name = "page")
                                                           Optional<Integer> optPage,
                                                         @RequestParam(required = false,name = "size")
                                                           Optional<Integer> optSize,
                                                         @RequestParam(required = false,name = "sortBy")
                                                           Optional<String> optSortBy,
                                                         @RequestParam(required = false,name = "optSortDirection")
                                                           Optional<Sort.Direction> optSortDirection) {

    Integer page = optPage.orElse(1) - 1; // converts to a zero-based page index
    Integer size = optSize.orElse(defaultSize);
    String sortBy = optSortBy.orElse(defaultSortBy);
    Sort.Direction sortDirection = optSortDirection.orElse(defaultSortOrder);

    Assert.isTrue(size <= maxSize, "Page size must not exceed limit of " + maxSize);
    // other cases covered by the Pageable class

    Sort sort = Sort.by(sortDirection,sortBy);
    PageRequest pageRequest = PageRequest.of(page, size, sort);
    // Page<UserDTO> usersPage = appUserService.getAllAppUsers(pageRequest);
    // List<UserDTO> usersList = userPage.toList();
// List, Slice or Page<UserDTO extends ResponseDTO> users = appUserService.findAll(Pageable pageRequest)
//     return ResponseEntity.ok(usersList);
    return null;
  }


}
